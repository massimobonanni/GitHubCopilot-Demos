# 🐞 Bug: Percentage coupons are applied as a flat amount

> Copy this content into a new GitHub Issue to start the demo.

## Summary

A percentage coupon (e.g. `SAVE10` = **10%**) is subtracted from the subtotal as a
flat currency amount instead of as a percentage of the subtotal.

## Steps to reproduce

1. Add a *Mechanical Keyboard* (120.00 × 1) and a *USB-C Cable* (15.00 × 2) to the cart.
2. Apply the coupon `SAVE10` (10%).
3. Read the cart total.

## Expected behavior

A 10% coupon on a 150.00 subtotal should give **135.00** (`150 × 0.90`).

## Actual behavior

The total is **140.00** — only 10 was subtracted, not 10%.

## Suspected location

`ShoppingCart.apply_discount` in [shopping_cart.py](shopping_cart.py) ignores the
`Coupon.is_percentage` flag and always subtracts `Coupon.value` directly.

## Acceptance criteria

- [ ] Percentage coupons subtract `subtotal * value / 100`.
- [ ] Flat coupons keep subtracting `value` directly.
- [ ] The total never goes below zero.
- [ ] A regression test covers both coupon types.
