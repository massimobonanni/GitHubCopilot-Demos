using System;
using Xunit;

// These are the tests submitted WITH the pull request. They all pass against the
// current implementation — which is exactly the point of this demo: a green test
// run does not prove the acceptance criteria are met.
//
// The tests cover only the happy paths (valid percentage, clamped-to-zero total,
// expired promo). They deliberately DO NOT cover:
//   * AC-04 — case-insensitive matching
//   * AC-05 — the minimum-subtotal boundary at exactly MinimumSubtotal
//
// The implementation-reviewer agent should report these as MISSING COVERAGE and
// still mark AC-04 / AC-05 from the code, not from the (passing) tests.

namespace MultiAgentPrReview.Tests;

public class PromoServiceTests
{
    private static PromoService CreateService() => new(
        new[]
        {
            new PromoCode("SAVE10", 10m, IsPercentage: true, MinimumSubtotal: 50m, ExpiresOn: new DateOnly(2030, 12, 31)),
            new PromoCode("EXPIRED", 10m, IsPercentage: true, MinimumSubtotal: 0m, ExpiresOn: new DateOnly(2020, 1, 1)),
        },
        today: new DateOnly(2026, 7, 27));

    [Fact]
    public void Percentage_promo_discounts_the_subtotal()
    {
        var service = CreateService();
        Assert.Equal(135m, service.Apply("SAVE10", 150m));
    }

    [Fact]
    public void Total_never_goes_below_zero()
    {
        var promos = new[]
        {
            new PromoCode("HUGE", 999m, IsPercentage: false, MinimumSubtotal: 0m, ExpiresOn: new DateOnly(2030, 12, 31)),
        };
        var service = new PromoService(promos, today: new DateOnly(2026, 7, 27));
        Assert.Equal(0m, service.Apply("HUGE", 100m));
    }

    [Fact]
    public void Expired_promo_is_rejected()
    {
        var service = CreateService();
        Assert.Throws<InvalidOperationException>(() => service.Apply("EXPIRED", 100m));
    }

    // Coverage gap (AC-04): no test asserts that "save10" matches "SAVE10".
    // Coverage gap (AC-05): no test asserts behavior at subtotal == MinimumSubtotal (50m).
}
