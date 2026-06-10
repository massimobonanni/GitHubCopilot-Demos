# GH-300.S — GitHub Copilot Fundamentals: JavaScript Demo Examples

These demos accompany the **GH-300.S: Scale Delivery with GitHub Copilot** course.
Each folder contains a self-contained JavaScript example designed to showcase a specific
Copilot capability during a live session.

> **Prerequisites:** Node.js 18+, VS Code with GitHub Copilot extension, `jest` for test generation.

---

## Demo Map

| # | Folder | Course Topic | Copilot Feature | Time |
|---|--------|-------------|-----------------|------|
| 1 | `01-code-completions` | Where Copilot Shows Up | **Inline suggestions** (Tab/Esc) | 5 min |
| 2 | `02-chat-and-refactoring` | Suggestions vs. Chat | **Chat & Inline Chat** refactoring | 8 min |
| 3 | `03-test-generation` | Testing & Quality Workflows | **Test generation** (`/tests`) | 8 min |
| 4 | `04-documentation` | Developer Flow → Document | **JSDoc & README generation** | 5 min |
| 5 | `05-prompt-engineering` | Prompt Engineering: The 4 S's | **Prompt quality comparison** | 8 min |
| 6 | `06-bug-detection` | Responsible AI & Validation | **Security review & bug finding** | 8 min |
| 7 | `07-explain-and-debug` | Core Developer Workflows | **Code explanation & optimization** | 5 min |
| 8 | `08-plan-mode` | Suggestions vs. Chat (Modes) | **Plan mode** — research & outline | 8 min |

---

## Demo 1 — Code Completions (`01-code-completions/inventory.js`)

**What it shows:** Copilot's real-time inline suggestions as you type.

**Scenario:** A warehouse product-inventory system with a `Product` class and an
`Inventory` class using a `Map`. Five methods are described as `// TODO` comments but not implemented.

**How to demo:**
1. Open `inventory.js` in VS Code
2. Place your cursor at the end of a `// TODO` comment and press **Enter**
3. Watch Copilot suggest the full method implementation
4. Press **Tab** to accept, **Esc** to dismiss, **Alt+]** to cycle alternatives
5. Repeat for all five methods
6. Bonus: start typing `const demo =` at the bottom — Copilot scaffolds a demo script

**Key talking points:**
- Context: Copilot reads the class, constructor, and existing methods
- The better the comment, the better the suggestion (4 S's preview)
- Always review before accepting

---

## Demo 2 — Chat & Refactoring (`02-chat-and-refactoring/orders.js`)

**What it shows:** Using Copilot Chat and Inline Chat to refactor messy code.

**Scenario:** An order-processing module with deeply nested if/else, repetitive
shipping-cost logic, and string-concatenation receipt formatting.

**How to demo:**
1. Open `orders.js` — point out the deeply nested `processOrder()` function
2. Select `processOrder()` → **Ctrl+I** (Inline Chat) → type:
   *"Refactor using early returns and guard clauses"*
3. Select `calculateShipping()` → Chat → type:
   *"Simplify this with an object/lookup table"*
4. Ask Chat: *"Convert this to ES modules with arrow functions"*
5. Run `node orders.js` before and after to show it still works

**Key talking points:**
- Chat modes: Ask (explain) vs. Agent (edit files)
- Copilot suggests modern JS idioms: destructuring, `??`, optional chaining
- Always run the code after refactoring

---

## Demo 3 — Test Generation (`03-test-generation/calculator.js`)

**What it shows:** Copilot generates comprehensive test suites instantly.

**Scenario:** A `Calculator` class with arithmetic, memory, and history features, plus
a `UnitConverter` object with conversion methods. Fully implemented, zero tests.

**How to demo:**
1. Open `calculator.js` → Copilot Chat → type `/tests` or
   *"Generate Jest tests for this file, including edge cases"*
2. Copilot creates `calculator.test.js` with `describe`/`it` blocks
3. Run: `npx jest calculator.test.js`
4. Select `divide()` → Inline Chat → *"What edge cases am I missing?"*
5. Copilot suggests: zero division, NaN, Infinity, negative numbers

**Key talking points:**
- Copilot generates edge cases you might forget (division by zero, NaN, boundaries)
- Tests still need human review — does the expected value make sense?
- Use Copilot for the boilerplate, add domain-specific assertions yourself

---

## Demo 4 — Documentation (`04-documentation/weatherClient.js`)

**What it shows:** Copilot generates JSDoc comments, type annotations, and README files.

**Scenario:** A weather-forecast client that calls the Open-Meteo API using `fetch`.
It works but has zero documentation — no JSDoc, no comments, no README.

**How to demo:**
1. Open `weatherClient.js` → Chat → *"Add JSDoc comments to every method"*
2. Select `getTemperature()` → Inline Chat → *"Add a detailed JSDoc with @param, @returns, @throws"*
3. Ask Chat: *"Write a README.md explaining how to use this module with examples"*
4. Ask Chat: *"Convert this to TypeScript with full type annotations"*

**Key talking points:**
- Documentation is where Copilot saves the most time with the least risk
- The generated docs are a starting point — verify the descriptions are accurate
- Great for onboarding: generate docs for legacy code new team members need to learn

---

## Demo 5 — Prompt Engineering: The 4 S's (`05-prompt-engineering/prompts.js`)

**What it shows:** How prompt quality directly affects Copilot's suggestions.

**Scenario:** Four examples, each with a BAD prompt (vague) and a GOOD prompt
(following the 4 S's: Single, Specific, Short, Surround).

**How to demo:**
1. Open `prompts.js` — walk through the 4 S's framework
2. **Live experiment:** Delete the `slugify()` body, uncomment the BAD prompt
   (`// TODO: do string stuff`) → see what Copilot suggests
3. Now uncomment the GOOD prompt instead → Copilot produces exactly `slugify()`
4. Show Example D ("Surround"): descriptive function name + JSDoc `@typedef`
   give Copilot perfect context even without a comment
5. Run `node prompts.js` to verify all examples work

**Key talking points:**
- Single: one task per prompt, not "do everything"
- Specific: "URL-safe slug" vs. "string stuff"
- Short: detailed ≠ long — be precise, not verbose
- Surround: good names, JSDoc types, and typedefs are implicit prompts

---

## Demo 6 — Bug Detection & Code Review (`06-bug-detection/buggyAuth.js`)

**What it shows:** Copilot as a code reviewer that catches security issues.

**Scenario:** An authentication module with **6 intentional security vulnerabilities**:
MD5 password hashing, SQL injection, timing attack, no rate limiting, predictable
tokens, and password logging.

**How to demo:**
1. Open `buggyAuth.js` → Chat → *"Review this code for bugs and security issues"*
2. Watch Copilot identify most (or all) of the 6 bugs
3. Select `hashPassword()` → Inline Chat → *"Is this secure?"*
4. Ask Chat: *"Rewrite this module following OWASP best practices"*
5. Compare the rewritten version — discuss `bcrypt`, parameterised queries,
   `crypto.timingSafeEqual`, `crypto.randomUUID`, proper logging

**Key talking points:**
- Copilot output is a starting point, not final truth (Responsible AI)
- Developer accountability remains — always validate
- Maps to the "Key Risks" slide: incorrect logic, security gaps, over-reliance

---

## Demo 7 — Explain Code & Debug (`07-explain-and-debug/legacyParser.js`)

**What it shows:** Copilot explains unfamiliar code and finds performance issues.

**Scenario:** A log-file parser with regex patterns, Date parsing, and a subtle
O(n²) duplicate-detection bug. The code works but is hard to read and slow at scale.

**How to demo:**
1. Open `legacyParser.js` → select the regex → Inline Chat →
   *"What does this regex match? Explain each capture group."*
2. Select `analyzeLogFile()` → Chat →
   *"This is slow for large files. Find the performance issue."*
3. Copilot identifies the O(n²) nested loop in duplicate detection
4. Ask Chat: *"Fix it using a Set for O(n) duplicate detection"*
5. Run `node legacyParser.js` to show output before and after

**Key talking points:**
- Copilot is invaluable when onboarding to a new codebase (~25% speed increase)
- Regex explanation alone saves significant debugging time
- Performance optimization: Copilot spots algorithmic issues humans easily miss

---

## Demo 8 — Plan Mode (`08-plan-mode/taskManager.js`)

**What it shows:** Plan mode researches your codebase and outlines a multi-step
implementation plan BEFORE making any changes.

**Scenario:** A working CLI task manager — add, complete, delete, search tasks. It
runs entirely in memory with no persistence, no validation, no API layer. It's
deliberately "ready for the next step" so Plan mode has something meaningful to plan.

**How to demo:**
1. Open `taskManager.js` — quickly show the working CLI (`node taskManager.js`, add a task, quit)
2. Open Copilot Chat → switch to **Plan** mode (mode picker at the top)
3. Type: *"Add SQLite persistence using better-sqlite3 so tasks survive a restart. Keep the CLI."*
4. Copilot outlines a step-by-step plan (install package, create DB, add storage layer…)
   **without changing any files**
5. Refine: *"Also add input validation — titles 3-100 chars, priority must be low/medium/high"*
6. The plan updates incrementally
7. Switch to **Agent** mode → *"Implement the plan"* → Copilot executes everything

**Key talking points:**
- Plan mode = think first, code later
- You control the architecture before any code is written
- The plan is iterative — refine it before committing
- Perfect for complex features, onboarding, and multi-file changes
- Maps to the slide: "Plan (Research and outline multi-step plans)"

---

## Tips for the Presenter

1. **Run each file first** (`node <file>`) to make sure everything works
2. **Keep Copilot Chat open** in the sidebar so the audience can see the conversation
3. **Don't script the exact output** — Copilot is non-deterministic, results may vary.
   That's part of the demo: show iteration and prompt refinement
4. **Connect back to the slides** — reference the 4 S's, Responsible AI principles,
   and Validation Workflow after each demo
5. **Let it fail sometimes** — if Copilot gives a wrong answer, that's a great
   teaching moment about the Validation Mindset

---

## Quick Start

```bash
cd GH-300-Demos/javascript

# Verify all runnable examples:
node 02-chat-and-refactoring/orders.js
node 05-prompt-engineering/prompts.js
node 07-explain-and-debug/legacyParser.js

# For test generation demo:
npm init -y && npm install --save-dev jest
```
