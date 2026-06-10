/**
 * Demo 9 — Copilot Instructions
 * ================================
 * Copilot Instructions let you define project-wide coding standards that
 * Copilot always respects — without repeating them in every prompt.
 *
 * How it works:
 *   • Create a `*.instructions.md` file with optional `applyTo` frontmatter
 *   • Place it in `.vscode/instructions/` (workspace) or `.github/` (repo-wide)
 *   • Copilot reads it automatically and applies the rules to every suggestion
 *   • No need to repeat conventions in individual prompts
 *
 * Setup for this demo:
 *   1. Copy `coding-standards.instructions.md` from this folder to:
 *            .vscode/instructions/coding-standards.instructions.md
 *      (or rename it to `copilot-instructions.md` and place it in `.github/`)
 *   2. Reload VS Code (Ctrl+Shift+P → "Developer: Reload Window")
 *   3. Come back to this file and complete the TODO methods below
 *
 * Exercises:
 *   1. First, DELETE the instructions file (or move it away) and complete
 *      one TODO — note the style Copilot uses.
 *   2. Restore the instructions file, reload, and complete the same TODO
 *      again — compare the difference in naming, typing, error handling,
 *      and logging.
 *   3. Open Chat and ask: "Does this class follow our coding standards?"
 *   4. Ask Chat: "Review OrderProcessor and flag any standards violations."
 *   5. Bonus — edit coding-standards.instructions.md to add a new rule,
 *      then ask Copilot to update the existing code to comply.
 */

'use strict';

const MAX_RETRY_ATTEMPTS = 3;
const ORDER_ID_PREFIX = 'ORD';

/** @enum {string} */
const OrderStatus = Object.freeze({
  PENDING: 'pending',
  CONFIRMED: 'confirmed',
  SHIPPED: 'shipped',
  DELIVERED: 'delivered',
  CANCELLED: 'cancelled',
});

/**
 * @typedef {Object} Order
 * @property {string} orderId
 * @property {string} customerEmail
 * @property {string[]} items
 * @property {number} totalAmount
 * @property {string} status
 * @property {Date} createdAt
 */

/**
 * @typedef {Object} ProcessingResult
 * @property {boolean} success
 * @property {string} orderId
 * @property {string} message
 * @property {Date} processedAt
 */

class OrderProcessor {
  /**
   * @param {Object} dbConnection
   * @param {Object} emailClient
   * @param {Object} logger
   */
  constructor(dbConnection, emailClient, logger) {
    this.db = dbConnection;
    this.email = emailClient;
    this.logger = logger;
  }

  // TODO: Implement async validateOrder(order)
  // Rules: orderId must start with ORDER_ID_PREFIX, totalAmount must be > 0,
  // customerEmail must contain '@', items array must not be empty.
  // Return a ProcessingResult object with success: true or an error message.

  // TODO: Implement async confirmOrder(order)
  // Update the order status to CONFIRMED via this.db,
  // send a confirmation email via this.email, and log the event.
  // Return a ProcessingResult object.

  // TODO: Implement async cancelOrder(order, reason)
  // Validate the order is not already DELIVERED or CANCELLED.
  // Update the status to CANCELLED, notify the customer, and log the event.
  // Return a ProcessingResult object.
}

module.exports = { OrderProcessor, OrderStatus };
