/**
 * Demo 11 — Copilot Prompt Files
 * ==================================
 * A Copilot Prompt File is a reusable prompt template stored as a `.prompt.md` file.
 * Unlike Instructions (always-on rules) or Agents (persistent chat modes), a
 * Prompt File is something you INVOKE on demand — like a named command.
 *
 * How it works:
 *   • Create a `<name>.prompt.md` file in `.vscode/prompts/`
 *   • The YAML frontmatter sets the mode, description, and tools
 *   • The markdown body is the prompt sent to Copilot when you invoke it
 *   • Invoke it by typing `/` in Copilot Chat and selecting the prompt name
 *
 * How Prompt Files differ from Agents and Instructions:
 *   ┌─────────────────┬──────────────────────────────┬──────────────────────────────┐
 *   │ Feature         │ When active                  │ File                         │
 *   ├─────────────────┼──────────────────────────────┼──────────────────────────────┤
 *   │ Instructions    │ Always — every suggestion    │ *.instructions.md            │
 *   │ Agent           │ While selected in mode picker│ .vscode/*.agent.md           │
 *   │ Prompt File     │ When explicitly invoked      │ .vscode/prompts/*.prompt.md  │
 *   └─────────────────┴──────────────────────────────┴──────────────────────────────┘
 *
 * Setup for this demo:
 *   1. Copy `generate-changelog.prompt.md` from this folder to:
 *            .vscode/prompts/generate-changelog.prompt.md
 *   2. Reload VS Code (Ctrl+Shift+P → "Developer: Reload Window")
 *   3. Open Copilot Chat → type `/` → verify "generate-changelog" appears
 *
 * Exercises:
 *   1. Open this file → open Copilot Chat
 *   2. Type `/generate-changelog` and invoke it
 *      → Copilot produces a structured CHANGELOG entry for this module
 *   3. Modify one of the methods below, then invoke the prompt again —
 *      note how it picks up your changes
 *   4. Ask in Chat (without the prompt): "What changed in this module?"
 *      → Compare the output quality with and without the prompt file
 *   5. Bonus — edit generate-changelog.prompt.md to change the output
 *      format (e.g., add an "Impact" field), reload, and run it again
 */

'use strict';

// ---------------------------------------------------------------------------
// v2.1.0 — Added lowStockThreshold and supplier info
// v2.0.0 — Migrated from plain-object to class-based model
// v1.0.0 — Initial release
// ---------------------------------------------------------------------------

class Supplier {
  /** @param {string} supplierId @param {string} name @param {string} contactEmail @param {number} leadTimeDays */
  constructor(supplierId, name, contactEmail, leadTimeDays) {
    this.supplierId = supplierId;
    this.name = name;
    this.contactEmail = contactEmail;
    this.leadTimeDays = leadTimeDays;
  }
}

class Product {
  /**
   * @param {string} productId
   * @param {string} name
   * @param {string} sku
   * @param {number} price
   * @param {number} stockQuantity
   * @param {string} category
   */
  constructor(productId, name, sku, price, stockQuantity, category) {
    this.productId = productId;
    this.name = name;
    this.sku = sku;
    this.price = price;
    this.stockQuantity = stockQuantity;
    this.category = category;
    /** @type {Supplier|null} */
    this.supplier = null;
    this.lowStockThreshold = 10;
    this.createdAt = new Date();
    this.updatedAt = new Date();
  }

  isInStock() {
    return this.stockQuantity > 0;
  }

  isLowStock() {
    return this.stockQuantity > 0 && this.stockQuantity <= this.lowStockThreshold;
  }

  /**
   * @param {number} percent — discount percentage (0–100)
   * @returns {number}
   */
  applyDiscount(percent) {
    if (percent <= 0 || percent > 100) {
      throw new RangeError(`Discount must be between 0 and 100, got ${percent}`);
    }
    return Math.round(this.price * (1 - percent / 100) * 100) / 100;
  }
}

class ProductCatalog {
  constructor() {
    /** @type {Map<string, Product>} */
    this._products = new Map();
  }

  /** @param {Product} product */
  addProduct(product) {
    if (this._products.has(product.productId)) {
      throw new Error(`Product '${product.productId}' already exists`);
    }
    this._products.set(product.productId, product);
  }

  /** @param {string} productId */
  removeProduct(productId) {
    if (!this._products.delete(productId)) {
      throw new Error(`Product '${productId}' not found`);
    }
  }

  /** @param {string} productId @returns {Product} */
  getProduct(productId) {
    const product = this._products.get(productId);
    if (!product) throw new Error(`Product '${productId}' not found`);
    return product;
  }

  /** @param {string} category @returns {Product[]} */
  searchByCategory(category) {
    return [...this._products.values()].filter(p => p.category === category);
  }

  /** @returns {Product[]} */
  getLowStockProducts() {
    return [...this._products.values()].filter(p => p.isLowStock());
  }

  /**
   * @param {string} productId
   * @param {number} quantity
   */
  restock(productId, quantity) {
    if (quantity <= 0) throw new RangeError('Restock quantity must be positive');
    const product = this.getProduct(productId);
    product.stockQuantity += quantity;
    product.updatedAt = new Date();
  }

  /**
   * @param {string} productId
   * @param {number} newPrice
   */
  updatePrice(productId, newPrice) {
    if (newPrice < 0) throw new RangeError('Price cannot be negative');
    const product = this.getProduct(productId);
    product.price = newPrice;
    product.updatedAt = new Date();
  }

  /** @returns {number} */
  totalInventoryValue() {
    let total = 0;
    for (const p of this._products.values()) {
      total += p.price * p.stockQuantity;
    }
    return Math.round(total * 100) / 100;
  }
}

module.exports = { Product, ProductCatalog, Supplier };
