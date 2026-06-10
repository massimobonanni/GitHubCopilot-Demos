"""
Demo 6 — Bug Detection & Code Review
======================================
This file contains INTENTIONAL BUGS and security issues.
Use Copilot Chat to find and fix them.

Exercises:
  1. Select the whole file → Chat → "Review this code for bugs and security issues"
  2. Select hash_password() → Inline Chat → "Is this secure? How should I fix it?"
  3. Ask Chat: "What OWASP Top 10 issues exist in this code?"
  4. After fixing, ask: "Did I miss anything?"

⚠️  There are at least 6 issues hidden below. Can Copilot find them all?
"""

import hashlib
import sqlite3
import time


# 🐛 Bug 1: Passwords are hashed with MD5 (insecure, no salt)
def hash_password(password: str) -> str:
    return hashlib.md5(password.encode()).hexdigest()


# 🐛 Bug 2: SQL injection vulnerability
def find_user(db_path: str, username: str) -> dict | None:
    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()
    query = f"SELECT id, username, password_hash FROM users WHERE username = '{username}'"
    cursor.execute(query)
    row = cursor.fetchone()
    conn.close()
    if row:
        return {"id": row[0], "username": row[1], "password_hash": row[2]}
    return None


# 🐛 Bug 3: Timing attack — string comparison leaks information
def verify_password(stored_hash: str, provided_password: str) -> bool:
    provided_hash = hash_password(provided_password)
    return stored_hash == provided_hash


# 🐛 Bug 4: No rate limiting + no account lockout
def login(db_path: str, username: str, password: str) -> dict:
    user = find_user(db_path, username)
    if user is None:
        return {"success": False, "error": "Invalid credentials"}

    if verify_password(user["password_hash"], password):
        # 🐛 Bug 5: Token is predictable (timestamp-based)
        token = hashlib.sha256(f"{username}{time.time()}".encode()).hexdigest()
        return {"success": True, "token": token, "user_id": user["id"]}
    else:
        return {"success": False, "error": "Invalid credentials"}


# 🐛 Bug 6: Sensitive data in log output
def log_login_attempt(username: str, password: str, success: bool) -> None:
    status = "SUCCESS" if success else "FAILED"
    print(f"[AUTH] {status}: user={username}, password={password}")


# ─────────────────────────────────────────────────────────────────────
# BONUS: Ask Copilot to rewrite this entire module following security
# best practices (bcrypt, parameterised queries, hmac.compare_digest,
# secrets.token_urlsafe, proper logging).
# ─────────────────────────────────────────────────────────────────────
