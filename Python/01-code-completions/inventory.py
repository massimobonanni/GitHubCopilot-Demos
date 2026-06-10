"""
Demo 1 — Code Completions
==========================
GitHub Copilot suggests code as you type. Open this file in VS Code,
place your cursor after each "# TODO" comment, and press Enter to see
Copilot's inline suggestions. Accept with Tab, dismiss with Esc.

Scenario: A small product-inventory system for a warehouse.
"""

from dataclasses import dataclass, field
from datetime import datetime


@dataclass
class Product:
    sku: str
    name: str
    price: float
    quantity: int = 0
    created_at: datetime = field(default_factory=datetime.now)


class Inventory:
    """Manages a collection of products in a warehouse."""

    def __init__(self):
        self._products: dict[str, Product] = {}

    def add_product(self, product: Product) -> None:
        """Add a new product or update quantity if it already exists."""
        if product.sku in self._products:
            self._products[product.sku].quantity += product.quantity
        else:
            self._products[product.sku] = product

    # -----------------------------------------------------------------
    # ✋ STOP HERE — let Copilot complete each method from the comment.
    # Place your cursor at the end of each comment and press Enter.
    # -----------------------------------------------------------------

    # TODO: Write a method called remove_product that removes a product by SKU
    # and raises a KeyError if the SKU doesn't exist.

    # TODO: Write a method called get_product that returns a Product by SKU
    # or None if not found.

    # TODO: Write a method called total_value that returns the sum of
    # price * quantity for every product in the inventory.

    # TODO: Write a method called low_stock_report that takes a threshold (int)
    # and returns a list of Products whose quantity is below that threshold.

    # TODO: Write a method called apply_discount that takes a SKU and a
    # percentage (float between 0 and 100) and reduces that product's price.


# -----------------------------------------------------------------
# Bonus: place your cursor on the blank line below and start typing
# "if __name__" — watch Copilot scaffold an entire demo script.
# -----------------------------------------------------------------
