"""
Demo 16 — Multi-Agent Pull Request Quality Gate
===============================================
This file is the IMPLEMENTATION submitted in a pull request. It is the target of
a three-agent, read-only quality gate:

  pr-quality-gate  (coordinator)
    |-- requirements-analyst    -> extracts testable acceptance criteria
    |-- implementation-reviewer -> maps code + tests to criteria, runs tests

The PR adds promo-code redemption at checkout. It runs and the existing tests
pass -- but it contains TWO INTENTIONAL defects the quality gate should catch by
comparing the code against the acceptance criteria in PR.md:

  * AC-04 (case-insensitive matching): the code matches promo codes with a
    case-sensitive comparison, so "save10" fails to match "SAVE10".
  * AC-05 (minimum-subtotal boundary): the code uses ">" instead of ">=", so a
    promo is wrongly rejected at exactly its minimum_subtotal.

There is also a robustness finding: an unknown code makes ``next()`` raise an
opaque StopIteration instead of a clear "promo not found" error.

Demo flow (see the language README for the full script):
  1. Put this folder's changes on a feature branch and open a Pull Request
  2. Install the three agents into .github/agents/
  3. Run the ``pr-quality-gate`` agent against the PR
  4. Read the evidence-based decision (expected: BLOCK)
"""

from dataclasses import dataclass
from datetime import date


@dataclass
class PromoCode:
    """A promo code that can be redeemed at checkout."""

    code: str
    value: float
    is_percentage: bool
    minimum_subtotal: float
    expires_on: date


class PromoService:
    """Validates and applies promo codes to an order subtotal."""

    def __init__(self, promos: list[PromoCode], today: date) -> None:
        self._promos = list(promos)
        self._today = today

    def apply(self, code: str, subtotal: float) -> float:
        """Apply the promo identified by ``code`` and return the discounted total."""
        # Robustness gap: raises an opaque StopIteration when the code is unknown.
        # Combined with the case-sensitive match, a valid promo typed in lower
        # case looks like a "missing" promo.
        # AC-04 defect: case-sensitive comparison -- "save10" != "SAVE10".
        promo = next(p for p in self._promos if p.code == code)

        # Reject expired promos.
        if promo.expires_on < self._today:
            raise ValueError(f"Promo {code} expired on {promo.expires_on}.")

        # AC-05 defect: should be ">=". At exactly the minimum subtotal the promo
        # is wrongly rejected and the customer pays full price.
        if subtotal > promo.minimum_subtotal:
            discount = subtotal * promo.value / 100 if promo.is_percentage else promo.value
            total = subtotal - discount
            return 0 if total < 0 else total  # Total never goes below zero.

        return subtotal


def main() -> None:
    promos = [
        PromoCode("SAVE10", 10, is_percentage=True, minimum_subtotal=50, expires_on=date(2030, 12, 31)),
        PromoCode("WELCOME", 5, is_percentage=False, minimum_subtotal=0, expires_on=date(2030, 12, 31)),
    ]

    service = PromoService(promos, today=date(2026, 7, 27))

    print(f"SAVE10 on 150.00 -> {service.apply('SAVE10', 150):.2f}")  # 135.00 (correct)
    print(f"WELCOME on 20.00 -> {service.apply('WELCOME', 20):.2f}")   # 15.00  (correct)

    print()
    print("The quality gate should flag these two cases against PR.md:")
    print("  service.apply('save10', 150)  # AC-04: raises - matching is case-sensitive")
    print("  service.apply('SAVE10', 50)   # AC-05: returns 50.00 - off-by-one at the minimum")


if __name__ == "__main__":
    main()
