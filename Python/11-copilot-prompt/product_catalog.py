"""
Demo 11 — Copilot Prompt Files
================================
A Copilot Prompt File is a reusable prompt template stored as a `.prompt.md` file.
Unlike Instructions (always-on rules) or Agents (persistent chat modes), a
Prompt File is something you INVOKE on demand — like a named command.

How it works:
  • Create a `<name>.prompt.md` file in `.vscode/prompts/`
  • The YAML frontmatter sets the mode, description, and tools
  • The markdown body is the prompt sent to Copilot when you invoke it
  • Invoke it by typing `/` in Copilot Chat and selecting the prompt name

How Prompt Files differ from Agents and Instructions:
  ┌─────────────────┬──────────────────────────────┬──────────────────────────────┐
  │ Feature         │ When active                  │ File                         │
  ├─────────────────┼──────────────────────────────┼──────────────────────────────┤
  │ Instructions    │ Always — every suggestion    │ *.instructions.md            │
  │ Agent           │ While selected in mode picker│ .vscode/*.agent.md           │
  │ Prompt File     │ When explicitly invoked      │ .vscode/prompts/*.prompt.md  │
  └─────────────────┴──────────────────────────────┴──────────────────────────────┘

Setup for this demo:
  1. Copy `generate-changelog.prompt.md` from this folder to:
         .vscode/prompts/generate-changelog.prompt.md
  2. Reload VS Code (Ctrl+Shift+P → "Developer: Reload Window")
  3. Open Copilot Chat → type `/` → verify "generate-changelog" appears

Exercises:
  1. Open this file → open Copilot Chat
  2. Type `/generate-changelog` and invoke it
     → Copilot produces a structured CHANGELOG entry for this module
  3. Modify one of the functions below, then invoke the prompt again —
     note how it picks up your changes
  4. Ask in Chat (without the prompt): "What changed in this module?"
     → Compare the output quality with and without the prompt file
  5. Bonus — edit generate-changelog.prompt.md to change the output
     format (e.g., add an "Impact" field), reload, and run it again
"""

from dataclasses import dataclass, field
from datetime import datetime
from typing import Optional


# ---------------------------------------------------------------------------
# v2.1.0 — Added low-stock threshold and supplier info
# v2.0.0 — Migrated from dict-based to dataclass-based model
# v1.0.0 — Initial release
# ---------------------------------------------------------------------------

@dataclass
class Supplier:
    supplier_id: str
    name: str
    contact_email: str
    lead_time_days: int


@dataclass
class Product:
    product_id: str
    name: str
    sku: str
    price: float
    stock_quantity: int
    category: str
    supplier: Optional[Supplier] = None
    low_stock_threshold: int = 10
    created_at: datetime = field(default_factory=datetime.utcnow)
    updated_at: datetime = field(default_factory=datetime.utcnow)

    def is_in_stock(self) -> bool:
        return self.stock_quantity > 0

    def is_low_stock(self) -> bool:
        return 0 < self.stock_quantity <= self.low_stock_threshold

    def apply_discount(self, percent: float) -> float:
        if not 0 < percent <= 100:
            raise ValueError(f"Discount must be between 0 and 100, got {percent}")
        return round(self.price * (1 - percent / 100), 2)


class ProductCatalog:
    def __init__(self) -> None:
        self._products: dict[str, Product] = {}

    def add_product(self, product: Product) -> None:
        if product.product_id in self._products:
            raise ValueError(f"Product {product.product_id!r} already exists")
        self._products[product.product_id] = product

    def remove_product(self, product_id: str) -> None:
        if product_id not in self._products:
            raise KeyError(f"Product {product_id!r} not found")
        del self._products[product_id]

    def get_product(self, product_id: str) -> Product:
        if product_id not in self._products:
            raise KeyError(f"Product {product_id!r} not found")
        return self._products[product_id]

    def search_by_category(self, category: str) -> list[Product]:
        return [p for p in self._products.values() if p.category == category]

    def get_low_stock_products(self) -> list[Product]:
        return [p for p in self._products.values() if p.is_low_stock()]

    def restock(self, product_id: str, quantity: int) -> None:
        if quantity <= 0:
            raise ValueError("Restock quantity must be positive")
        product = self.get_product(product_id)
        product.stock_quantity += quantity
        product.updated_at = datetime.utcnow()

    def update_price(self, product_id: str, new_price: float) -> None:
        if new_price < 0:
            raise ValueError("Price cannot be negative")
        product = self.get_product(product_id)
        product.price = new_price
        product.updated_at = datetime.utcnow()

    def total_inventory_value(self) -> float:
        return sum(p.price * p.stock_quantity for p in self._products.values())
