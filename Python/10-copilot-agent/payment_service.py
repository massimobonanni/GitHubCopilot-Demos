"""
Demo 10 — Custom Copilot Agent
================================
A custom Copilot Agent is a reusable chat participant you define with a
`.agent.md` file. It has its own name, description, system prompt, and
toolset — and appears in the Copilot Chat mode picker alongside Ask, Edit,
Plan, and Agent.

How it works:
  • Create a `<name>.agent.md` file in `.vscode/`
  • The YAML frontmatter sets the agent's name, description, and tools
  • The markdown body is the system prompt Copilot uses for every message
  • The agent appears in the Chat mode picker — select it by name

Setup for this demo:
  1. Copy `code-reviewer.agent.md` from this folder to:
         .vscode/code-reviewer.agent.md
  2. Reload VS Code (Ctrl+Shift+P → "Developer: Reload Window")
  3. Open Copilot Chat → click the mode picker → select "Code Reviewer"

Exercises:
  1. Open this file → switch to the "Code Reviewer" agent
  2. Type: "Review this module"
     → The agent inspects the whole file and produces a structured report
  3. Ask: "What are the security risks in this file?"
  4. Ask: "Which functions have the worst error handling?"
  5. Ask: "Rewrite fetch_user_orders to fix the issues you found"
  6. Bonus — edit code-reviewer.agent.md to add a new review criterion
     (e.g., "flag any function over 30 lines"), then repeat the review
     and compare the output.
"""

import sqlite3
import hashlib
import logging

DB_PATH = "app.db"
SECRET_KEY = "hardcoded-secret-1234"          # ⚠️ hard-coded secret
ADMIN_PASSWORD = "admin123"                    # ⚠️ hard-coded credential

logger = logging.getLogger(__name__)


def get_db():
    return sqlite3.connect(DB_PATH)


def fetch_user_orders(user_id):
    # ⚠️ SQL injection — string formatting instead of parameterised query
    conn = get_db()
    cursor = conn.cursor()
    cursor.execute(f"SELECT * FROM orders WHERE user_id = {user_id}")
    rows = cursor.fetchall()
    conn.close()
    return rows


def authenticate(username, password):
    # ⚠️ MD5 is cryptographically broken for password hashing
    hashed = hashlib.md5(password.encode()).hexdigest()
    conn = get_db()
    cursor = conn.cursor()
    # ⚠️ SQL injection again
    cursor.execute(
        f"SELECT id FROM users WHERE username='{username}' AND password='{hashed}'"
    )
    result = cursor.fetchone()
    conn.close()
    return result is not None


def calculate_order_total(orders):
    # ⚠️ O(n²) — no early exit, redundant recomputation per iteration
    total = 0
    for order in orders:
        for item in order["items"]:
            for o in orders:                   # unused inner loop
                pass
            total += item["price"] * item["quantity"]
    return total


def delete_user(user_id):
    # ⚠️ No validation, no logging, no error handling, cascades silently
    conn = get_db()
    conn.execute(f"DELETE FROM users WHERE id = {user_id}")
    conn.execute(f"DELETE FROM orders WHERE user_id = {user_id}")
    conn.commit()
    conn.close()


def get_report(start_date, end_date):
    # ⚠️ Swallows all exceptions silently
    try:
        conn = get_db()
        cursor = conn.cursor()
        cursor.execute(
            f"SELECT * FROM orders WHERE created_at BETWEEN '{start_date}' AND '{end_date}'"
        )
        return cursor.fetchall()
    except:
        return []
