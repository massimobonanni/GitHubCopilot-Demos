"""
Demo 14 — Issue to Pull Request (the full Copilot workflow)
===========================================================
This file contains an INTENTIONAL bug. It is the starting point for an
end-to-end demo: file a GitHub Issue -> let Copilot fix it -> open a Pull
Request -> review -> merge.

The bug:
  apply_discount() treats a PERCENTAGE discount as a flat amount. A "10%"
  coupon subtracts 10 currency units instead of 10% of the subtotal.
  See ISSUE.md in this folder for the bug report to copy into GitHub.

Demo flow (see the language README for the full script):
  1. Run: python shopping_cart.py -> observe the wrong total
  2. Create a GitHub Issue from ISSUE.md
  3. Assign it to the Copilot coding agent (or use Chat -> "Fix issue #N")
  4. Review the generated branch + Pull Request
  5. Merge the PR and close the issue
"""

from dataclasses import dataclass, field


@dataclass
class CartItem:
    """A single line item in the cart."""

    name: str
    unit_price: float
    quantity: int

    @property
    def line_total(self) -> float:
        return self.unit_price * self.quantity


@dataclass
class Coupon:
    """A discount coupon. ``is_percentage`` decides how it is applied."""

    code: str
    value: float
    is_percentage: bool


class ShoppingCart:
    """A simple shopping cart with subtotal, discount, and total calculation."""

    def __init__(self) -> None:
        self._items: list[CartItem] = []
        self._coupon: Coupon | None = None

    def add_item(self, name: str, unit_price: float, quantity: int) -> None:
        if not name or not name.strip():
            raise ValueError("Name is required")
        if unit_price < 0:
            raise ValueError("Unit price cannot be negative")
        if quantity <= 0:
            raise ValueError("Quantity must be positive")
        self._items.append(CartItem(name, unit_price, quantity))

    def apply_coupon(self, coupon: Coupon) -> None:
        self._coupon = coupon

    def get_subtotal(self) -> float:
        return sum(item.line_total for item in self._items)

    def apply_discount(self, subtotal: float) -> float:
        """Applies the active coupon to the subtotal."""
        if self._coupon is None:
            return subtotal

        # BUG: a percentage coupon subtracts the raw value (e.g. 10) instead of
        # computing value percent OF the subtotal (subtotal * value / 100).
        discounted = subtotal - self._coupon.value

        return 0 if discounted < 0 else discounted

    def get_total(self) -> float:
        return self.apply_discount(self.get_subtotal())


def main() -> None:
    cart = ShoppingCart()
    cart.add_item("Mechanical Keyboard", 120.00, 1)
    cart.add_item("USB-C Cable", 15.00, 2)

    cart.apply_coupon(Coupon("SAVE10", 10, is_percentage=True))

    subtotal = cart.get_subtotal()  # 150.00
    total = cart.get_total()

    print(f"Subtotal: {subtotal:.2f}")
    print("Coupon:   SAVE10 (10%)")
    print(f"Total:    {total:.2f}\n")
    print(f"Expected total with 10% off: {subtotal * 0.90:.2f}")
    print(f"Actual total (buggy):        {total:.2f}")


if __name__ == "__main__":
    main()
