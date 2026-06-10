"""
Demo 5 — Prompt Engineering: The 4 S's
========================================
This file demonstrates how prompt quality affects Copilot output.
Each section shows a BAD prompt (vague) and a GOOD prompt (4 S's).

The 4 S's:
  • Single  — One task per prompt
  • Specific — Explicit, detailed instructions
  • Short   — Concise without overloading
  • Surround — Descriptive names, related files open

Exercises:
  1. Delete the implementation under each section
  2. Try the BAD prompt first (uncomment it), see what Copilot suggests
  3. Then try the GOOD prompt — compare the quality of suggestions
  4. Experiment: tweak the prompts and see how output changes
"""


# =====================================================================
# Example A: String manipulation
# =====================================================================

# ❌ BAD PROMPT (vague, multiple tasks):
# TODO: do string stuff

# ✅ GOOD PROMPT (Single, Specific, Short):
# Write a function called slugify that takes a string title and returns
# a URL-safe slug: lowercase, spaces replaced with hyphens, only
# alphanumeric characters and hyphens allowed, no leading/trailing hyphens.

import re

def slugify(title: str) -> str:
    slug = title.lower().strip()
    slug = re.sub(r"[^\w\s-]", "", slug)
    slug = re.sub(r"[\s_]+", "-", slug)
    slug = slug.strip("-")
    return slug


# =====================================================================
# Example B: Data validation
# =====================================================================

# ❌ BAD PROMPT:
# TODO: validate email

# ✅ GOOD PROMPT:
# Write a function called validate_email that takes a string and returns
# True if it matches a basic email pattern (user@domain.tld), or False
# otherwise. Do not use third-party libraries.

def validate_email(email: str) -> bool:
    pattern = r"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"
    return bool(re.match(pattern, email))


# =====================================================================
# Example C: Data transformation
# =====================================================================

# ❌ BAD PROMPT:
# TODO: process the data

# ✅ GOOD PROMPT:
# Write a function called summarize_sales that takes a list of dicts
# with keys "product" (str), "amount" (float), and "region" (str).
# Return a dict mapping each region to its total sales amount, rounded
# to 2 decimal places.

def summarize_sales(transactions: list[dict]) -> dict[str, float]:
    totals: dict[str, float] = {}
    for tx in transactions:
        region = tx["region"]
        totals[region] = round(totals.get(region, 0) + tx["amount"], 2)
    return totals


# =====================================================================
# Example D: Context matters ("Surround")
# =====================================================================

# Below, the descriptive function name + type hints + surrounding code
# give Copilot all the context it needs. Try placing your cursor after
# the function signature and let Copilot complete the body.

from dataclasses import dataclass


@dataclass
class Employee:
    name: str
    department: str
    salary: float
    years_of_service: int


def get_senior_employees_by_department(
    employees: list[Employee],
    min_years: int = 5,
) -> dict[str, list[str]]:
    """Group employees with >= min_years of service by department.

    Returns a dict mapping department name → list of employee names.
    """
    # TODO: Let Copilot complete this — the name + types + docstring
    # give it excellent context (the "Surround" principle).
    result: dict[str, list[str]] = {}
    for emp in employees:
        if emp.years_of_service >= min_years:
            result.setdefault(emp.department, []).append(emp.name)
    return result


# =====================================================================
# Quick self-test
# =====================================================================
if __name__ == "__main__":
    print(slugify("  Hello, World! This is a Test.  "))
    # → "hello-world-this-is-a-test"

    print(validate_email("user@example.com"))   # True
    print(validate_email("not-an-email"))        # False

    sales = [
        {"product": "Widget", "amount": 120.50, "region": "EMEA"},
        {"product": "Gadget", "amount": 89.99, "region": "APAC"},
        {"product": "Widget", "amount": 200.00, "region": "EMEA"},
    ]
    print(summarize_sales(sales))
    # → {'EMEA': 320.5, 'APAC': 89.99}
