/**
 * Demo 14 — Issue to Pull Request (the full Copilot workflow)
 * ===========================================================
 * This file contains an INTENTIONAL bug. It is the starting point for an
 * end-to-end demo: file a GitHub Issue → let Copilot fix it → open a Pull
 * Request → review → merge.
 *
 * The bug:
 *   applyDiscount() treats a PERCENTAGE discount as a flat amount. A "10%"
 *   coupon subtracts 10 currency units instead of 10% of the subtotal.
 *   See ISSUE.md in this folder for the bug report to copy into GitHub.
 *
 * Demo flow (see the language README for the full script):
 *   1. Run: node shoppingCart.js → observe the wrong total
 *   2. Create a GitHub Issue from ISSUE.md
 *   3. Assign it to the Copilot coding agent (or use Chat → "Fix issue #N")
 *   4. Review the generated branch + Pull Request
 *   5. Merge the PR and close the issue
 */

class ShoppingCart {
  constructor() {
    /** @type {{ name: string, unitPrice: number, quantity: number }[]} */
    this.items = [];
    /** @type {{ code: string, value: number, isPercentage: boolean } | null} */
    this.coupon = null;
  }

  addItem(name, unitPrice, quantity) {
    if (!name || !name.trim()) {
      throw new Error('Name is required');
    }
    if (unitPrice < 0) {
      throw new Error('Unit price cannot be negative');
    }
    if (quantity <= 0) {
      throw new Error('Quantity must be positive');
    }
    this.items.push({ name, unitPrice, quantity });
  }

  applyCoupon(coupon) {
    this.coupon = coupon;
  }

  getSubtotal() {
    return this.items.reduce((sum, item) => sum + item.unitPrice * item.quantity, 0);
  }

  /**
   * Applies the active coupon to the subtotal.
   */
  applyDiscount(subtotal) {
    if (!this.coupon) {
      return subtotal;
    }

    // BUG: a percentage coupon subtracts the raw value (e.g. 10) instead of
    // computing value percent OF the subtotal (subtotal * value / 100).
    const discounted = subtotal - this.coupon.value;

    return discounted < 0 ? 0 : discounted;
  }

  getTotal() {
    return this.applyDiscount(this.getSubtotal());
  }
}

function main() {
  const cart = new ShoppingCart();
  cart.addItem('Mechanical Keyboard', 120.0, 1);
  cart.addItem('USB-C Cable', 15.0, 2);

  cart.applyCoupon({ code: 'SAVE10', value: 10, isPercentage: true });

  const subtotal = cart.getSubtotal(); // 150.00
  const total = cart.getTotal();

  console.log(`Subtotal: ${subtotal.toFixed(2)}`);
  console.log('Coupon:   SAVE10 (10%)');
  console.log(`Total:    ${total.toFixed(2)}\n`);
  console.log(`Expected total with 10% off: ${(subtotal * 0.9).toFixed(2)}`);
  console.log(`Actual total (buggy):        ${total.toFixed(2)}`);
}

if (require.main === module) {
  main();
}

module.exports = { ShoppingCart };
