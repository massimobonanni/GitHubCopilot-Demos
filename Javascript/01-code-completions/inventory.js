/**
 * Demo 1 — Code Completions
 * ==========================
 * GitHub Copilot suggests code as you type. Open this file in VS Code,
 * place your cursor after each "// TODO" comment, and press Enter to see
 * Copilot's inline suggestions. Accept with Tab, dismiss with Esc.
 *
 * Scenario: A small product-inventory system for a warehouse.
 */

class Product {
  /**
   * @param {string} sku
   * @param {string} name
   * @param {number} price
   * @param {number} quantity
   */
  constructor(sku, name, price, quantity = 0) {
    this.sku = sku;
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.createdAt = new Date();
  }
}

class Inventory {
  constructor() {
    /** @type {Map<string, Product>} */
    this._products = new Map();
  }

  /**
   * Add a new product or update quantity if it already exists.
   * @param {Product} product
   */
  addProduct(product) {
    if (this._products.has(product.sku)) {
      this._products.get(product.sku).quantity += product.quantity;
    } else {
      this._products.set(product.sku, product);
    }
  }

  // -----------------------------------------------------------------
  // ✋ STOP HERE — let Copilot complete each method from the comment.
  // Place your cursor at the end of each comment and press Enter.
  // -----------------------------------------------------------------

  // TODO: Write a method called removeProduct that removes a product by SKU
  // and throws an Error if the SKU doesn't exist.

  // TODO: Write a method called getProduct that returns a Product by SKU
  // or undefined if not found.

  // TODO: Write a method called totalValue that returns the sum of
  // price * quantity for every product in the inventory.

  // TODO: Write a method called lowStockReport that takes a threshold (number)
  // and returns an array of Products whose quantity is below that threshold.

  // TODO: Write a method called applyDiscount that takes a SKU and a
  // percentage (number between 0 and 100) and reduces that product's price.
}

// -----------------------------------------------------------------
// Bonus: place your cursor on the blank line below and start typing
// "const demo" — watch Copilot scaffold an entire demo script.
// -----------------------------------------------------------------

module.exports = { Product, Inventory };
