const { PromoService } = require('./promoService');

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

function createService() {
  return new PromoService(
    [
      { code: 'SAVE10', value: 10, isPercentage: true, minimumSubtotal: 50, expiresOn: new Date('2030-12-31') },
      { code: 'EXPIRED', value: 10, isPercentage: true, minimumSubtotal: 0, expiresOn: new Date('2020-01-01') },
    ],
    new Date('2026-07-27'),
  );
}

describe('PromoService', () => {
  test('percentage promo discounts the subtotal', () => {
    expect(createService().apply('SAVE10', 150)).toBe(135);
  });

  test('total never goes below zero', () => {
    const service = new PromoService(
      [{ code: 'HUGE', value: 999, isPercentage: false, minimumSubtotal: 0, expiresOn: new Date('2030-12-31') }],
      new Date('2026-07-27'),
    );
    expect(service.apply('HUGE', 100)).toBe(0);
  });

  test('expired promo is rejected', () => {
    expect(() => createService().apply('EXPIRED', 100)).toThrow();
  });

  // Coverage gap (AC-04): no test asserts that "save10" matches "SAVE10".
  // Coverage gap (AC-05): no test asserts behavior at subtotal === minimumSubtotal (50).
});
