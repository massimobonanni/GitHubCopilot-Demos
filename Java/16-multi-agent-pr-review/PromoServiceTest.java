import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

/*
 * These are the tests submitted WITH the pull request. They all pass against the
 * current implementation — which is exactly the point of this demo: a green test
 * run does not prove the acceptance criteria are met.
 *
 * The tests cover only the happy paths (valid percentage, clamped-to-zero total,
 * expired promo). They deliberately DO NOT cover:
 *   * AC-04 — case-insensitive matching
 *   * AC-05 — the minimum-subtotal boundary at exactly minimumSubtotal
 *
 * The implementation-reviewer agent should report these as MISSING COVERAGE and
 * still mark AC-04 / AC-05 from the code, not from the (passing) tests.
 */
class PromoServiceTest {

    private PromoService createService() {
        return new PromoService(
                List.of(
                        new PromoService.PromoCode("SAVE10", 10, true, 50, LocalDate.of(2030, 12, 31)),
                        new PromoService.PromoCode("EXPIRED", 10, true, 0, LocalDate.of(2020, 1, 1))),
                LocalDate.of(2026, 7, 27));
    }

    @Test
    void percentagePromoDiscountsTheSubtotal() {
        PromoService service = createService();
        assertEquals(135.0, service.apply("SAVE10", 150), 0.001);
    }

    @Test
    void totalNeverGoesBelowZero() {
        PromoService service = new PromoService(
                List.of(new PromoService.PromoCode("HUGE", 999, false, 0, LocalDate.of(2030, 12, 31))),
                LocalDate.of(2026, 7, 27));
        assertEquals(0.0, service.apply("HUGE", 100), 0.001);
    }

    @Test
    void expiredPromoIsRejected() {
        PromoService service = createService();
        assertThrows(IllegalStateException.class, () -> service.apply("EXPIRED", 100));
    }

    // Coverage gap (AC-04): no test asserts that "save10" matches "SAVE10".
    // Coverage gap (AC-05): no test asserts behavior at subtotal == minimumSubtotal (50).
}
