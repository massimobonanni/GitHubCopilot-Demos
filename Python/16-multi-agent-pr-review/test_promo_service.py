"""
These are the tests submitted WITH the pull request. They all pass against the
current implementation -- which is exactly the point of this demo: a green test
run does not prove the acceptance criteria are met.

The tests cover only the happy paths (valid percentage, clamped-to-zero total,
expired promo). They deliberately DO NOT cover:
  * AC-04 -- case-insensitive matching
  * AC-05 -- the minimum-subtotal boundary at exactly minimum_subtotal

The implementation-reviewer agent should report these as MISSING COVERAGE and
still mark AC-04 / AC-05 from the code, not from the (passing) tests.
"""

from datetime import date

import pytest

from promo_service import PromoCode, PromoService


def create_service() -> PromoService:
    return PromoService(
        [
            PromoCode("SAVE10", 10, is_percentage=True, minimum_subtotal=50, expires_on=date(2030, 12, 31)),
            PromoCode("EXPIRED", 10, is_percentage=True, minimum_subtotal=0, expires_on=date(2020, 1, 1)),
        ],
        today=date(2026, 7, 27),
    )


def test_percentage_promo_discounts_the_subtotal() -> None:
    assert create_service().apply("SAVE10", 150) == 135


def test_total_never_goes_below_zero() -> None:
    service = PromoService(
        [PromoCode("HUGE", 999, is_percentage=False, minimum_subtotal=0, expires_on=date(2030, 12, 31))],
        today=date(2026, 7, 27),
    )
    assert service.apply("HUGE", 100) == 0


def test_expired_promo_is_rejected() -> None:
    with pytest.raises(ValueError):
        create_service().apply("EXPIRED", 100)


# Coverage gap (AC-04): no test asserts that "save10" matches "SAVE10".
# Coverage gap (AC-05): no test asserts behavior at subtotal == minimum_subtotal (50).
