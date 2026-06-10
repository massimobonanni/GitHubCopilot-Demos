# GH-300.S — GitHub Copilot Fundamentals: Python Demo Examples

These demos accompany the **GH-300.S: Scale Delivery with GitHub Copilot** course.
Each folder contains a self-contained Python example designed to showcase a specific
Copilot capability during a live session.

> **Prerequisites:** Python 3.12+, VS Code with GitHub Copilot extension, `pytest` installed.

---

## Demo Map

| # | Folder | Course Topic | Copilot Feature | Time |
|---|--------|-------------|-----------------|------|
| 1 | `01-code-completions` | Where Copilot Shows Up | **Inline suggestions** (Tab/Esc) | 5 min |
| 2 | `02-chat-and-refactoring` | Suggestions vs. Chat | **Chat & Inline Chat** refactoring | 8 min |
| 3 | `03-test-generation` | Testing & Quality Workflows | **Test generation** (`/tests`) | 8 min |
| 4 | `04-documentation` | Developer Flow → Document | **Docstring & README generation** | 5 min |
| 5 | `05-prompt-engineering` | Prompt Engineering: The 4 S's | **Prompt quality comparison** | 8 min |
| 6 | `06-bug-detection` | Responsible AI & Validation | **Security review & bug finding** | 8 min |
| 7 | `07-explain-and-debug` | Core Developer Workflows | **Code explanation & optimization** | 5 min |

---

## Demo 1 — Code Completions (`01-code-completions/inventory.py`)

**What it shows:** Copilot's real-time inline suggestions as you type.

**Scenario:** A warehouse product-inventory system with a `Product` dataclass and an
`Inventory` class. Five methods are described as `# TODO` comments but not implemented.

**How to demo:**
1. Open `inventory.py` in VS Code
2. Place your cursor at the end of a `# TODO` comment and press **Enter**
3. Watch Copilot suggest the full method implementation
4. Press **Tab** to accept, **Esc** to dismiss, **Alt+]** to cycle alternatives
5. Repeat for all five methods
6. Bonus: start typing `if __name__` at the bottom — Copilot scaffolds a demo script

**Key talking points:**
- Context: Copilot reads the class, dataclass, and existing methods
- The better the comment, the better the suggestion (4 S's preview)
- Always review before accepting

---

## Demo 2 — Chat & Refactoring (`02-chat-and-refactoring/orders.py`)

**What it shows:** Using Copilot Chat and Inline Chat to refactor messy code.

**Scenario:** An order-processing module with deeply nested if/else, repetitive
shipping-cost logic, and string-concatenation receipt formatting. It works but is hard
to maintain.

**How to demo:**
1. Open `orders.py` — point out the deeply nested `process_order()` function
2. Select `process_order()` → **Ctrl+I** (Inline Chat) → type:
   *"Refactor using early returns to reduce nesting"*
3. Select `calculate_shipping()` → Chat → type:
   *"Simplify this with a dictionary/table lookup"*
4. Ask Chat: *"Add type hints to all functions in this file"*
5. Run `python orders.py` before and after to show it still works

**Key talking points:**
- Chat modes: Ask (explain) vs. Agent (edit files)
- Copilot understands intent, not just syntax
- Always run the code after refactoring

---

## Demo 3 — Test Generation (`03-test-generation/calculator.py`)

**What it shows:** Copilot generates comprehensive test suites instantly.

**Scenario:** A `Calculator` class with arithmetic, memory, and history features, plus
a `UnitConverter` with temperature/weight/distance conversions. Fully implemented, zero
tests.

**How to demo:**
1. Open `calculator.py` → Copilot Chat → type `/tests` or
   *"Generate pytest tests for this file, including edge cases"*
2. Copilot creates `test_calculator.py` with organized test classes
3. Run: `pytest test_calculator.py -v`
4. Select `divide()` → Inline Chat → *"What edge cases am I missing?"*
5. Copilot suggests: negative numbers, very large numbers, float precision

**Key talking points:**
- Copilot generates edge cases you might forget (zero division, negatives, boundary)
- Tests still need human review — does the expected value make sense?
- Use Copilot for the boilerplate, add domain-specific assertions yourself

---

## Demo 4 — Documentation (`04-documentation/weather_client.py`)

**What it shows:** Copilot generates docstrings, type hints, and README files.

**Scenario:** A weather-forecast client that calls the Open-Meteo API. It works but
has zero documentation — no docstrings, no comments, no README.

**How to demo:**
1. Open `weather_client.py` → Chat → *"Add Google-style docstrings to every method"*
2. Select `get_temperature()` → Inline Chat → *"Add a detailed docstring with Args, Returns, Raises"*
3. Ask Chat: *"Write a README.md explaining how to use this module with examples"*
4. Ask Chat: *"Add type hints to all parameters and return values"*

**Key talking points:**
- Documentation is where Copilot saves the most time with the least risk
- The generated docs are a starting point — verify the descriptions are accurate
- Great for onboarding: generate docs for legacy code new team members need to learn

---

## Demo 5 — Prompt Engineering: The 4 S's (`05-prompt-engineering/prompts.py`)

**What it shows:** How prompt quality directly affects Copilot's suggestions.

**Scenario:** Four examples, each with a BAD prompt (vague) and a GOOD prompt
(following the 4 S's: Single, Specific, Short, Surround). The implementations are
provided so you can compare.

**How to demo:**
1. Open `prompts.py` — walk through the 4 S's framework
2. **Live experiment:** Delete the `slugify()` body, uncomment the BAD prompt
   (`# TODO: do string stuff`) → see what Copilot suggests
3. Now uncomment the GOOD prompt instead → Copilot produces exactly `slugify()`
4. Show Example D ("Surround"): descriptive function name + type hints + docstring
   give Copilot perfect context even without a comment
5. Run `python prompts.py` to verify all examples work

**Key talking points:**
- Single: one task per prompt, not "do everything"
- Specific: "URL-safe slug" vs. "string stuff"
- Short: detailed ≠ long — be precise, not verbose
- Surround: good names and types are implicit prompts

---

## Demo 6 — Bug Detection & Code Review (`06-bug-detection/buggy_auth.py`)

**What it shows:** Copilot as a code reviewer that catches security issues.

**Scenario:** An authentication module with **6 intentional security vulnerabilities**:
MD5 password hashing, SQL injection, timing attack, no rate limiting, predictable
tokens, and password logging.

**How to demo:**
1. Open `buggy_auth.py` → Chat → *"Review this code for bugs and security issues"*
2. Watch Copilot identify most (or all) of the 6 bugs
3. Select `hash_password()` → Inline Chat → *"Is this secure?"*
4. Ask Chat: *"Rewrite this module following OWASP best practices"*
5. Compare the rewritten version — discuss what changed and why

**Key talking points:**
- Copilot output is a starting point, not final truth (Responsible AI)
- Developer accountability remains — always validate
- This demo maps directly to the "Key Risks" slide: incorrect logic, security gaps,
  over-reliance

---

## Demo 7 — Explain Code & Debug (`07-explain-and-debug/legacy_parser.py`)

**What it shows:** Copilot explains unfamiliar code and finds performance issues.

**Scenario:** A log-file parser with regex patterns, datetime parsing, and a subtle
O(n²) duplicate-detection bug. The code works but is hard to read and slow at scale.

**How to demo:**
1. Open `legacy_parser.py` → select the regex → Inline Chat →
   *"What does this regex match? Explain each group."*
2. Select `analyze_log_file()` → Chat →
   *"This is slow for large files. Find the performance issue."*
3. Copilot identifies the O(n²) nested loop in duplicate detection
4. Ask Chat: *"Fix it using a set for O(n) duplicate detection"*
5. Run `python legacy_parser.py` to show output before and after

**Key talking points:**
- Copilot is invaluable when onboarding to a new codebase (~25% speed increase)
- Regex explanation alone saves significant debugging time
- Performance optimization: Copilot spots algorithmic issues humans easily miss

---

## Tips for the Presenter

1. **Run each file first** to make sure everything works in your environment
2. **Keep Copilot Chat open** in the sidebar so the audience can see the conversation
3. **Don't script the exact output** — Copilot is non-deterministic, so results may
   vary. That's part of the demo: show iteration and prompt refinement
4. **Connect back to the slides** — reference the 4 S's, Responsible AI principles,
   and Validation Workflow after each demo
5. **Let it fail sometimes** — if Copilot gives a wrong answer, that's a great
   teaching moment about the Validation Mindset

---

## Quick Start

```bash
# Clone/copy the folder, then:
cd GH-300-Demos
pip install pytest

# Verify all examples run:
python 01-code-completions/inventory.py
python 02-chat-and-refactoring/orders.py
python 03-test-generation/calculator.py
python 05-prompt-engineering/prompts.py
python 07-explain-and-debug/legacy_parser.py

# Demo 4 calls a live API — needs internet:
python -c "from importlib import import_module; print('weather_client imports OK')"
```
