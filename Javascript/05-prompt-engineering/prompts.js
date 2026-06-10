/**
 * Demo 5 — Prompt Engineering: The 4 S's
 * ========================================
 * This file demonstrates how prompt quality affects Copilot output.
 * Each section shows a BAD prompt (vague) and a GOOD prompt (4 S's).
 *
 * The 4 S's:
 *   • Single  — One task per prompt
 *   • Specific — Explicit, detailed instructions
 *   • Short   — Concise without overloading
 *   • Surround — Descriptive names, related files open
 *
 * Exercises:
 *   1. Delete the implementation under each section
 *   2. Try the BAD prompt first (uncomment it), see what Copilot suggests
 *   3. Then try the GOOD prompt — compare the quality
 *   4. Experiment: tweak the prompts and observe how output changes
 */

// =====================================================================
// Example A: String manipulation
// =====================================================================

// ❌ BAD PROMPT (vague, multiple tasks):
// TODO: do string stuff

// ✅ GOOD PROMPT (Single, Specific, Short):
// Write a function called slugify that takes a string title and returns
// a URL-safe slug: lowercase, spaces replaced with hyphens, only
// alphanumeric characters and hyphens allowed, no leading/trailing hyphens.

function slugify(title) {
  return title
    .trim()
    .toLowerCase()
    .replace(/[^\w\s-]/g, "")
    .replace(/[\s_]+/g, "-")
    .replace(/^-+|-+$/g, "");
}

// =====================================================================
// Example B: Data validation
// =====================================================================

// ❌ BAD PROMPT:
// TODO: validate email

// ✅ GOOD PROMPT:
// Write a function called validateEmail that takes a string and returns
// true if it matches a basic email pattern (user@domain.tld), or false
// otherwise. Do not use third-party libraries.

function validateEmail(email) {
  const pattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  return pattern.test(email);
}

// =====================================================================
// Example C: Data transformation
// =====================================================================

// ❌ BAD PROMPT:
// TODO: process the data

// ✅ GOOD PROMPT:
// Write a function called summarizeSales that takes an array of objects
// with keys "product" (string), "amount" (number), and "region" (string).
// Return an object mapping each region to its total sales amount, rounded
// to 2 decimal places.

function summarizeSales(transactions) {
  const totals = {};
  for (const tx of transactions) {
    totals[tx.region] = Math.round(((totals[tx.region] || 0) + tx.amount) * 100) / 100;
  }
  return totals;
}

// =====================================================================
// Example D: Context matters ("Surround")
// =====================================================================

// Below, the descriptive function name + JSDoc + surrounding code
// give Copilot all the context it needs. Try placing your cursor after
// the function signature and let Copilot complete the body.

/**
 * @typedef {Object} Employee
 * @property {string} name
 * @property {string} department
 * @property {number} salary
 * @property {number} yearsOfService
 */

/**
 * Groups employees with >= minYears of service by department.
 * @param {Employee[]} employees
 * @param {number} [minYears=5]
 * @returns {Object<string, string[]>} department name → list of employee names
 */
function getSeniorEmployeesByDepartment(employees, minYears = 5) {
  // TODO: Let Copilot complete this — the name + JSDoc + typedef
  // give it excellent context (the "Surround" principle).
  const result = {};
  for (const emp of employees) {
    if (emp.yearsOfService >= minYears) {
      if (!result[emp.department]) result[emp.department] = [];
      result[emp.department].push(emp.name);
    }
  }
  return result;
}

// =====================================================================
// Quick self-test
// =====================================================================
console.log(slugify("  Hello, World! This is a Test.  "));
// → "hello-world-this-is-a-test"

console.log(validateEmail("user@example.com")); // true
console.log(validateEmail("not-an-email")); // false

const sales = [
  { product: "Widget", amount: 120.5, region: "EMEA" },
  { product: "Gadget", amount: 89.99, region: "APAC" },
  { product: "Widget", amount: 200.0, region: "EMEA" },
];
console.log(summarizeSales(sales));
// → { EMEA: 320.5, APAC: 89.99 }
