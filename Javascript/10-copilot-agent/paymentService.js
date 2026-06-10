/**
 * Demo 10 — Custom Copilot Agent
 * ================================
 * A custom Copilot Agent is a reusable chat participant you define with a
 * `.agent.md` file. It has its own name, description, system prompt, and
 * toolset — and appears in the Copilot Chat mode picker alongside Ask, Edit,
 * Plan, and Agent.
 *
 * How it works:
 *   • Create a `<name>.agent.md` file in `.vscode/`
 *   • The YAML frontmatter sets the agent's name, description, and tools
 *   • The markdown body is the system prompt Copilot uses for every message
 *   • The agent appears in the Chat mode picker — select it by name
 *
 * Setup for this demo:
 *   1. Copy `code-reviewer.agent.md` from this folder to:
 *            .vscode/code-reviewer.agent.md
 *   2. Reload VS Code (Ctrl+Shift+P → "Developer: Reload Window")
 *   3. Open Copilot Chat → click the mode picker → select "Code Reviewer"
 *
 * Exercises:
 *   1. Open this file → switch to the "Code Reviewer" agent
 *   2. Type: "Review this file"
 *      → The agent inspects the file and produces a structured report
 *   3. Ask: "What are the security risks in this file?"
 *   4. Ask: "Which functions have the worst error handling?"
 *   5. Ask: "Rewrite fetchUserOrders to fix the issues you found"
 *   6. Bonus — edit code-reviewer.agent.md to add a new rule
 *      (e.g., "flag any function over 30 lines"), then repeat the review
 *      and compare the output.
 */

'use strict';

const { createHash } = require('crypto');
const sqlite3 = require('sqlite3').verbose();

// ⚠️ Hard-coded database path and secret
const DB_PATH = 'app.db';
const SECRET_KEY = 'hardcoded-jwt-secret-9876';    // ⚠️ hard-coded secret
const ADMIN_PASSWORD = 'admin123';                  // ⚠️ hard-coded credential

function getDb() {
  return new sqlite3.Database(DB_PATH);
}

/**
 * @param {string|number} userId
 */
function fetchUserOrders(userId) {
  // ⚠️ SQL injection — template literal in query instead of parameterised statement
  const db = getDb();
  return new Promise((resolve, reject) => {
    db.all(`SELECT * FROM orders WHERE user_id = ${userId}`, (err, rows) => {
      if (err) reject(err);
      else resolve(rows);
    });
  });
}

/**
 * @param {string} username
 * @param {string} password
 */
function authenticate(username, password) {
  // ⚠️ MD5 is cryptographically broken for password hashing
  const hashed = createHash('md5').update(password).digest('hex');

  // ⚠️ SQL injection again
  const db = getDb();
  return new Promise((resolve) => {
    db.get(
      `SELECT id FROM users WHERE username='${username}' AND password='${hashed}'`,
      (err, row) => resolve(!!row)   // ⚠️ error silently ignored
    );
  });
}

/**
 * @param {Array} orders
 */
function calculateOrderTotal(orders) {
  // ⚠️ O(n²) — inner loop over orders is redundant and never used
  let total = 0;
  for (const order of orders) {
    for (const item of order.items) {
      for (const _ of orders) { }   // unused inner loop
      total += item.price * item.quantity;
    }
  }
  return total;
}

/**
 * @param {number} userId
 */
function deleteUser(userId) {
  // ⚠️ No validation, no logging, no error handling, promise not returned
  const db = getDb();
  db.run(`DELETE FROM users WHERE id = ${userId}`);
  db.run(`DELETE FROM orders WHERE user_id = ${userId}`);
}

/**
 * @param {string} startDate
 * @param {string} endDate
 */
function getReport(startDate, endDate) {
  // ⚠️ SQL injection via date strings, errors silently swallowed
  const db = getDb();
  return new Promise((resolve) => {
    db.all(
      `SELECT * FROM orders WHERE created_at BETWEEN '${startDate}' AND '${endDate}'`,
      (err, rows) => resolve(rows || [])   // ⚠️ error ignored, returns empty array
    );
  });
}

module.exports = { fetchUserOrders, authenticate, calculateOrderTotal, deleteUser, getReport };
