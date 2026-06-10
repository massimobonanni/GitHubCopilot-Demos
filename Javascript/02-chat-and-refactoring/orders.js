/**
 * Demo 2 — Copilot Chat & Refactoring
 * ====================================
 * This file contains messy but working code. Use Copilot Chat or Inline
 * Chat (Ctrl+I / Cmd+I) to refactor, simplify, and modernise it.
 *
 * Suggested exercises:
 *   1. Select processOrder() → Inline Chat → "Refactor using early returns"
 *   2. Select calculateShipping() → Chat → "Simplify with an object lookup"
 *   3. Ask Chat: "Add JSDoc comments to all functions"
 *   4. Ask Chat: "Convert this to ES modules and use modern JavaScript features"
 */

function processOrder(order) {
  const result = {};
  if (order !== null && order !== undefined) {
    if (order.items) {
      if (order.items.length > 0) {
        let subtotal = 0;
        for (let i = 0; i < order.items.length; i++) {
          const item = order.items[i];
          if (item.price !== undefined && item.qty !== undefined) {
            subtotal += item.price * item.qty;
          } else {
            result.error = "Invalid item: missing price or qty";
            return result;
          }
        }

        // Apply discount
        if (order.discountCode) {
          if (order.discountCode === "SAVE10") {
            subtotal = subtotal * 0.9;
          } else if (order.discountCode === "SAVE20") {
            subtotal = subtotal * 0.8;
          } else if (order.discountCode === "HALF") {
            subtotal = subtotal * 0.5;
          } else {
            result.warning = "Unknown discount code";
          }
        }

        result.subtotal = Math.round(subtotal * 100) / 100;
        result.tax = Math.round(subtotal * 0.08 * 100) / 100;
        result.total = Math.round(subtotal * 1.08 * 100) / 100;
        result.status = "processed";
        result.timestamp = new Date().toISOString();
      } else {
        result.error = "Order has no items";
      }
    } else {
      result.error = "Missing items key";
    }
  } else {
    result.error = "Order is null";
  }
  return result;
}

function calculateShipping(weight, destination) {
  let cost = 0;
  if (destination === "domestic") {
    if (weight <= 1) cost = 5.99;
    else if (weight <= 5) cost = 9.99;
    else if (weight <= 20) cost = 14.99;
    else cost = 24.99;
  } else if (destination === "europe") {
    if (weight <= 1) cost = 12.99;
    else if (weight <= 5) cost = 22.99;
    else if (weight <= 20) cost = 39.99;
    else cost = 59.99;
  } else if (destination === "international") {
    if (weight <= 1) cost = 19.99;
    else if (weight <= 5) cost = 34.99;
    else if (weight <= 20) cost = 54.99;
    else cost = 79.99;
  } else {
    throw new Error("Unknown destination: " + destination);
  }
  return cost;
}

function formatReceipt(orderResult, customerName) {
  const lines = [];
  lines.push("=".repeat(40));
  lines.push("RECEIPT");
  lines.push("=".repeat(40));
  lines.push("Customer: " + customerName);
  lines.push("Date: " + (orderResult.timestamp || "N/A"));
  lines.push("-".repeat(40));
  lines.push("Subtotal: $" + (orderResult.subtotal || 0));
  lines.push("Tax:      $" + (orderResult.tax || 0));
  lines.push("Total:    $" + (orderResult.total || 0));
  lines.push("=".repeat(40));
  return lines.join("\n");
}

// Try it: run this file, then ask Copilot Chat to improve it.
const sampleOrder = {
  items: [
    { name: "Keyboard", price: 79.99, qty: 1 },
    { name: "Mouse", price: 29.99, qty: 2 },
    { name: "USB-C Cable", price: 12.49, qty: 3 },
  ],
  discountCode: "SAVE10",
};

const result = processOrder(sampleOrder);
console.log(formatReceipt(result, "Alice Johnson"));
console.log(`\nShipping (3 kg, domestic): $${calculateShipping(3, "domestic")}`);
