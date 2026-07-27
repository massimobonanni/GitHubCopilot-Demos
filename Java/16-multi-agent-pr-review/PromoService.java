import java.time.LocalDate;
import java.util.List;

/**
 * Demo 16 — Multi-Agent Pull Request Quality Gate
 * ===============================================
 * This file is the IMPLEMENTATION submitted in a pull request. It is the target
 * of a three-agent, read-only quality gate:
 *
 *   pr-quality-gate  (coordinator)
 *     ├── requirements-analyst    -> extracts testable acceptance criteria
 *     └── implementation-reviewer -> maps code + tests to criteria, runs tests
 *
 * The PR adds promo-code redemption at checkout. It compiles and the existing
 * tests pass — but it contains TWO INTENTIONAL defects the quality gate should
 * catch by comparing the code against the acceptance criteria in PR.md:
 *
 *   * AC-04 (case-insensitive matching): the code matches promo codes with a
 *     case-sensitive comparison, so "save10" fails to match "SAVE10".
 *   * AC-05 (minimum-subtotal boundary): the code uses ">" instead of ">=", so a
 *     promo is wrongly rejected at exactly its minimumSubtotal.
 *
 * There is also a robustness finding: an unknown code makes findFirst().orElseThrow()
 * raise an opaque NoSuchElementException instead of a clear "promo not found" error.
 *
 * Demo flow (see the language README for the full script):
 *   1. Put this folder's changes on a feature branch and open a Pull Request
 *   2. Install the three agents into .github/agents/
 *   3. Run the `pr-quality-gate` agent against the PR
 *   4. Read the evidence-based decision (expected: BLOCK)
 */
public class PromoService {

    /** A promo code that can be redeemed at checkout. */
    public record PromoCode(
            String code,
            double value,
            boolean isPercentage,
            double minimumSubtotal,
            LocalDate expiresOn) {
    }

    private final List<PromoCode> promos;
    private final LocalDate today;

    public PromoService(List<PromoCode> promos, LocalDate today) {
        this.promos = List.copyOf(promos);
        this.today = today;
    }

    /**
     * Applies the promo identified by {@code code} to {@code subtotal} and
     * returns the discounted total.
     */
    public double apply(String code, double subtotal) {
        // Robustness gap: throws an opaque NoSuchElementException when the code is
        // unknown. Combined with the case-sensitive match, a valid promo typed in
        // lower case looks like a "missing" promo.
        // AC-04 defect: case-sensitive comparison — "save10" != "SAVE10".
        PromoCode promo = promos.stream()
                .filter(p -> p.code().equals(code))
                .findFirst()
                .orElseThrow();

        // Reject expired promos.
        if (promo.expiresOn().isBefore(today)) {
            throw new IllegalStateException("Promo " + code + " expired on " + promo.expiresOn() + ".");
        }

        // AC-05 defect: should be ">=". At exactly the minimum subtotal the promo
        // is wrongly rejected and the customer pays full price.
        if (subtotal > promo.minimumSubtotal()) {
            double discount = promo.isPercentage()
                    ? subtotal * promo.value() / 100.0
                    : promo.value();

            double total = subtotal - discount;
            return total < 0 ? 0 : total; // Total never goes below zero.
        }

        return subtotal;
    }

    public static void main(String[] args) {
        List<PromoCode> promos = List.of(
                new PromoCode("SAVE10", 10, true, 50, LocalDate.of(2030, 12, 31)),
                new PromoCode("WELCOME", 5, false, 0, LocalDate.of(2030, 12, 31)));

        PromoService service = new PromoService(promos, LocalDate.of(2026, 7, 27));

        System.out.printf("SAVE10 on 150.00 -> %.2f%n", service.apply("SAVE10", 150));  // 135.00 (correct)
        System.out.printf("WELCOME on 20.00 -> %.2f%n", service.apply("WELCOME", 20));   // 15.00  (correct)

        System.out.println();
        System.out.println("The quality gate should flag these two cases against PR.md:");
        System.out.println("  service.apply(\"save10\", 150)  // AC-04: throws - matching is case-sensitive");
        System.out.println("  service.apply(\"SAVE10\", 50)   // AC-05: returns 50.00 - off-by-one at the minimum");
    }
}
