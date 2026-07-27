# ✨ Feature: Promo-code redemption at checkout

> Copy this content into the description of the Pull Request you open for this
> demo. The quality-gate agents use it as the specification to review against.

## Summary

Adds a `PromoService` that validates and applies promo codes to an order
subtotal. Percentage and flat promos are supported, expired promos are rejected,
and each promo can require a minimum subtotal.

## Changes

- `promo_service.py` — new `PromoCode` dataclass and `PromoService.apply(code, subtotal)`.
- `test_promo_service.py` — pytest tests for the promo behavior.

## Acceptance criteria

- [ ] **AC-01** — A percentage promo discounts the subtotal by `subtotal × value / 100`.
- [ ] **AC-02** — A flat promo subtracts `value` directly from the subtotal.
- [ ] **AC-03** — The returned total never goes below zero.
- [ ] **AC-04** — Promo codes are matched **case-insensitively** (`save10` == `SAVE10`).
- [ ] **AC-05** — A promo applies only when `subtotal >= minimum_subtotal` (inclusive of the exact minimum).
- [ ] **AC-06** — Expired promos (`expires_on` before today) are rejected.
- [ ] **AC-07** — Unit tests cover valid, expired, below-minimum, boundary, and case-insensitive scenarios.

## Non-functional constraints

- No secrets or full customer data logged.
- An unknown promo code should fail with a clear, actionable error.
- Public behavior is deterministic given a fixed "today" date.
