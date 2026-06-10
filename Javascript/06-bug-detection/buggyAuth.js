/**
 * Demo 6 — Bug Detection & Code Review
 * ======================================
 * This file contains INTENTIONAL BUGS and security issues.
 * Use Copilot Chat to find and fix them.
 *
 * Exercises:
 *   1. Select the whole file → Chat → "Review this code for bugs and security issues"
 *   2. Select hashPassword() → Inline Chat → "Is this secure? How should I fix it?"
 *   3. Ask Chat: "What OWASP Top 10 issues exist in this code?"
 *   4. After fixing, ask: "Did I miss anything?"
 *
 * ⚠️  There are at least 6 issues hidden below. Can Copilot find them all?
 */

const crypto = require("crypto");
const { Database } = require("better-sqlite3"); // conceptual import for demo

// 🐛 Bug 1: Passwords are hashed with MD5 (insecure, no salt)
function hashPassword(password) {
  return crypto.createHash("md5").update(password).digest("hex");
}

// 🐛 Bug 2: SQL injection vulnerability
function findUser(db, username) {
  const query = `SELECT id, username, password_hash FROM users WHERE username = '${username}'`;
  const row = db.prepare(query).get();
  if (row) {
    return { id: row.id, username: row.username, passwordHash: row.password_hash };
  }
  return null;
}

// 🐛 Bug 3: Timing attack — string comparison leaks information
function verifyPassword(storedHash, providedPassword) {
  const providedHash = hashPassword(providedPassword);
  return storedHash === providedHash;
}

// 🐛 Bug 4: No rate limiting + no account lockout
function login(db, username, password) {
  const user = findUser(db, username);
  if (!user) {
    return { success: false, error: "Invalid credentials" };
  }

  if (verifyPassword(user.passwordHash, password)) {
    // 🐛 Bug 5: Token is predictable (timestamp-based)
    const tokenData = `${username}${Date.now()}`;
    const token = crypto.createHash("sha256").update(tokenData).digest("hex");

    return { success: true, token, userId: user.id };
  }

  return { success: false, error: "Invalid credentials" };
}

// 🐛 Bug 6: Sensitive data in log output
function logLoginAttempt(username, password, success) {
  const status = success ? "SUCCESS" : "FAILED";
  console.log(`[AUTH] ${status}: user=${username}, password=${password}`);
}

module.exports = { hashPassword, findUser, verifyPassword, login, logLoginAttempt };

// ─────────────────────────────────────────────────────────────────────
// BONUS: Ask Copilot to rewrite this entire module following security
// best practices (bcrypt, parameterised queries, crypto.timingSafeEqual,
// crypto.randomUUID, proper logging — no sensitive data).
// ─────────────────────────────────────────────────────────────────────
