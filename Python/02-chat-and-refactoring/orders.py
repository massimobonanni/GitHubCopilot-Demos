"""
Demo 2 — Copilot Chat & Refactoring
====================================
This file contains messy but working code. Use Copilot Chat or Inline
Chat (Ctrl+I / Cmd+I) to refactor, simplify, and modernise it.

Suggested exercises:
  1. Select process_order() → Inline Chat → "Refactor using early returns"
  2. Select calculate_shipping() → Chat → "Simplify with a dictionary lookup"
  3. Ask Chat: "Add type hints to all functions in this file"
  4. Ask Chat: "Convert these functions into an OrderProcessor class"
"""

import json
from datetime import datetime


def process_order(order):
    """Process an incoming order dict — validate, apply discounts, return result."""
    result = {}
    if order is not None:
        if "items" in order:
            if len(order["items"]) > 0:
                subtotal = 0
                for item in order["items"]:
                    if "price" in item and "qty" in item:
                        subtotal += item["price"] * item["qty"]
                    else:
                        result["error"] = "Invalid item: missing price or qty"
                        return result

                # Apply discount
                if "discount_code" in order:
                    if order["discount_code"] == "SAVE10":
                        subtotal = subtotal * 0.9
                    elif order["discount_code"] == "SAVE20":
                        subtotal = subtotal * 0.8
                    elif order["discount_code"] == "HALF":
                        subtotal = subtotal * 0.5
                    else:
                        result["warning"] = "Unknown discount code"

                result["subtotal"] = round(subtotal, 2)
                result["tax"] = round(subtotal * 0.08, 2)
                result["total"] = round(subtotal * 1.08, 2)
                result["status"] = "processed"
                result["timestamp"] = datetime.now().isoformat()
            else:
                result["error"] = "Order has no items"
        else:
            result["error"] = "Missing items key"
    else:
        result["error"] = "Order is None"
    return result


def calculate_shipping(weight, destination):
    """Calculate shipping cost based on weight (kg) and destination zone."""
    cost = 0
    if destination == "domestic":
        if weight <= 1:
            cost = 5.99
        elif weight <= 5:
            cost = 9.99
        elif weight <= 20:
            cost = 14.99
        else:
            cost = 24.99
    elif destination == "europe":
        if weight <= 1:
            cost = 12.99
        elif weight <= 5:
            cost = 22.99
        elif weight <= 20:
            cost = 39.99
        else:
            cost = 59.99
    elif destination == "international":
        if weight <= 1:
            cost = 19.99
        elif weight <= 5:
            cost = 34.99
        elif weight <= 20:
            cost = 54.99
        else:
            cost = 79.99
    else:
        raise ValueError(f"Unknown destination: {destination}")
    return cost


def format_receipt(order_result, customer_name):
    """Build a plain-text receipt string."""
    lines = []
    lines.append("=" * 40)
    lines.append("RECEIPT")
    lines.append("=" * 40)
    lines.append("Customer: " + customer_name)
    lines.append("Date: " + order_result.get("timestamp", "N/A"))
    lines.append("-" * 40)
    lines.append("Subtotal: $" + str(order_result.get("subtotal", 0)))
    lines.append("Tax:      $" + str(order_result.get("tax", 0)))
    lines.append("Total:    $" + str(order_result.get("total", 0)))
    lines.append("=" * 40)
    return "\n".join(lines)


# Try it: run this file, then ask Copilot Chat to improve it.
if __name__ == "__main__":
    sample_order = {
        "items": [
            {"name": "Keyboard", "price": 79.99, "qty": 1},
            {"name": "Mouse", "price": 29.99, "qty": 2},
            {"name": "USB-C Cable", "price": 12.49, "qty": 3},
        ],
        "discount_code": "SAVE10",
    }

    result = process_order(sample_order)
    print(format_receipt(result, "Alice Johnson"))
    print(f"\nShipping (3 kg, domestic): ${calculate_shipping(3, 'domestic')}")
