using System;
using System.Collections.Generic;
using System.Linq;

// Demo 16 — Multi-Agent Pull Request Quality Gate
// ===============================================
// This file is the IMPLEMENTATION submitted in a pull request. It is the target
// of a three-agent, read-only quality gate:
//
//   pr-quality-gate  (coordinator)
//     ├── requirements-analyst   → extracts testable acceptance criteria
//     └── implementation-reviewer → maps code + tests to criteria, runs tests
//
// The PR adds promo-code redemption at checkout. It compiles and the existing
// tests pass — but it contains TWO INTENTIONAL defects the quality gate should
// catch by comparing the code against the acceptance criteria in PR.md:
//
//   * AC-04 (case-insensitive matching): the code matches promo codes with an
//     ordinal, case-sensitive comparison, so "save10" fails to match "SAVE10".
//   * AC-05 (minimum-subtotal boundary): the code uses ">" instead of ">=", so a
//     promo is wrongly rejected at exactly its MinimumSubtotal.
//
// There is also a robustness finding: an unknown code makes First() throw an
// opaque InvalidOperationException instead of a clear "promo not found" error.
//
// Demo flow (see the language README for the full script):
//   1. Put this folder's changes on a feature branch and open a Pull Request
//   2. Install the three agents into .github/agents/
//   3. Run the `pr-quality-gate` agent against the PR
//   4. Read the evidence-based decision (expected: BLOCK)

namespace MultiAgentPrReview;

/// <summary>A promo code that can be redeemed at checkout.</summary>
public record PromoCode(
    string Code,
    decimal Value,
    bool IsPercentage,
    decimal MinimumSubtotal,
    DateOnly ExpiresOn);

/// <summary>Validates and applies promo codes to an order subtotal.</summary>
public class PromoService
{
    private readonly IReadOnlyList<PromoCode> _promos;
    private readonly DateOnly _today;

    public PromoService(IEnumerable<PromoCode> promos, DateOnly today)
    {
        _promos = promos.ToList();
        _today = today;
    }

    /// <summary>
    /// Applies the promo identified by <paramref name="code"/> to
    /// <paramref name="subtotal"/> and returns the discounted total.
    /// </summary>
    public decimal Apply(string code, decimal subtotal)
    {
        // Robustness gap: throws an opaque InvalidOperationException when the code
        // is unknown. Combined with the case-sensitive match below, a valid promo
        // typed in lower case looks like a "missing" promo.
        // AC-04 defect: ordinal, case-sensitive comparison — "save10" != "SAVE10".
        PromoCode promo = _promos.First(p => p.Code == code);

        // Reject expired promos.
        if (promo.ExpiresOn < _today)
            throw new InvalidOperationException($"Promo {code} expired on {promo.ExpiresOn}.");

        // AC-05 defect: should be ">=". At exactly the minimum subtotal the promo
        // is wrongly rejected and the customer pays full price.
        if (subtotal > promo.MinimumSubtotal)
        {
            decimal discount = promo.IsPercentage
                ? subtotal * promo.Value / 100m
                : promo.Value;

            decimal total = subtotal - discount;
            return total < 0 ? 0 : total; // Total never goes below zero.
        }

        return subtotal;
    }
}

public static class Program
{
    public static void Main()
    {
        var promos = new[]
        {
            new PromoCode("SAVE10", 10m, IsPercentage: true, MinimumSubtotal: 50m, ExpiresOn: new DateOnly(2030, 12, 31)),
            new PromoCode("WELCOME", 5m, IsPercentage: false, MinimumSubtotal: 0m, ExpiresOn: new DateOnly(2030, 12, 31)),
        };

        var service = new PromoService(promos, today: new DateOnly(2026, 7, 27));

        Console.WriteLine($"SAVE10 on 150.00 -> {service.Apply("SAVE10", 150m):C}");   // 135.00 (correct)
        Console.WriteLine($"WELCOME on 20.00 -> {service.Apply("WELCOME", 20m):C}");    // 15.00  (correct)

        Console.WriteLine();
        Console.WriteLine("The quality gate should flag these two cases against PR.md:");
        Console.WriteLine("  service.Apply(\"save10\", 150m)  // AC-04: throws — matching is case-sensitive");
        Console.WriteLine("  service.Apply(\"SAVE10\", 50m)   // AC-05: returns 50.00 — off-by-one at the minimum");
    }
}
