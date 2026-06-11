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
| 9 | `09-copilot-instructions` | Customizing Copilot | **Copilot Instructions** — project-wide coding standards | 8 min |
| 10 | `10-copilot-agent` | Customizing Copilot | **Custom Copilot Agent** — reusable chat participant | 8 min |
| 11 | `11-copilot-prompt` | Customizing Copilot | **Copilot Prompt File** — on-demand invocable prompt | 8 min |
| 12 | `12-copilot-skill` | Customizing Copilot | **Copilot Skill** — packaged, discoverable, reusable capability | 8 min |

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

## Demo 9 — Copilot Instructions (`09-copilot-instructions/orderProcessor.js`)

**What it shows:** How `*.instructions.md` files let you define project-wide coding standards
that Copilot automatically applies to every suggestion — without repeating rules in each prompt.

**Files:**
- `orderProcessor.js` — skeleton order processor with TODO methods to complete
- `coding-standards.instructions.md` — the instructions file to install in the workspace

**Setup (do this before the demo):**
1. Copy `coding-standards.instructions.md` to `.vscode/instructions/coding-standards.instructions.md`
2. Reload VS Code (Ctrl+Shift+P → *"Developer: Reload Window"*)
3. Verify the file appears in **Copilot Chat → Manage Instructions**

**How to demo:**
1. Open `orderProcessor.js` — show the TODO methods and the instructions file side by side
2. **Without** the instructions file active: delete it, reload, complete `validateOrder` — note the output style
3. Restore the instructions file, reload, complete the same method again — compare:
   - JSDoc comment generated with `@param` and `@returns`
   - `async`/`await` used throughout
   - Specific error types used
   - Structured logger used instead of `console.log`
4. Open Chat → ask: *"Does this class follow our coding standards?"*
5. Ask Chat: *"Review OrderProcessor and flag any violations of our standards"*
6. Bonus: add a new rule to `coding-standards.instructions.md`, then ask Copilot to update the existing code to comply

**Key talking points:**
- Instructions files are picked up automatically — zero prompt overhead
- `applyTo` frontmatter scopes rules to specific file patterns
- `.vscode/instructions/` = workspace scope; `.github/copilot-instructions.md` = repo-wide
- Great for onboarding: new developers get team standards for free
- Instructions work in both inline completions and Chat

---

## Demo 10 — Custom Copilot Agent (`10-copilot-agent/paymentService.js`)

**What it shows:** How `.agent.md` files let you define a custom Copilot chat participant
with its own name, system prompt, and toolset — selectable from the Chat mode picker.

**Files:**
- `paymentService.js` — a payment service with intentional security and quality issues to review
- `code-reviewer.agent.md` — the custom agent definition to install in the workspace

**Setup (do this before the demo):**
1. Copy `code-reviewer.agent.md` to `.vscode/code-reviewer.agent.md`
2. Reload VS Code (Ctrl+Shift+P → *"Developer: Reload Window"*)
3. Open Copilot Chat → click the mode picker → verify **Code Reviewer** appears

**How to demo:**
1. Open `paymentService.js` — briefly walk through the file, pointing out it "looks like real code"
2. Switch Chat to the **Code Reviewer** agent
3. Type: *"Review this file"*
   → Copilot produces a structured report: Critical Issues, Warnings, Suggestions, Summary
4. Ask: *"What are the security risks in this file?"*
   → Agent focuses on SQL injection, MD5, hard-coded secrets, swallowed errors
5. Ask: *"Rewrite `fetchUserOrders` to fix the issues you found"*
   → Agent generates a corrected version with parameterised queries and error handling
6. Switch back to the default agent and ask the same question — compare the output style
7. Bonus: add a rule to `code-reviewer.agent.md` (e.g., *"flag functions over 20 lines"*),
   reload, and re-run the review

**Key talking points:**
- Custom agents appear in the mode picker like first-class Copilot modes
- The system prompt shapes every response — the agent is always "in role"
- Different agents for different workflows: reviewer, architect, test-writer, documenter…
- Agents can access the codebase, open problems, and recent changes via their `tools` list
- Stored in `.vscode/` — version-controlled and shared with the whole team

---

## Demo 11 — Copilot Prompt File (`11-copilot-prompt/productCatalog.js`)

**What it shows:** How `.prompt.md` files create on-demand, invocable prompt files
distinct from always-on Instructions and persistent Agent modes.

**Files:**
- `productCatalog.js` — a versioned product catalog module (the demo target)
- `generate-changelog.prompt.md` — the prompt file definition to install in the workspace

**How Prompt Files differ from Agents and Instructions:**

| Feature | When active | File location |
|---|---|---|
| Instructions | Always — every suggestion | `.vscode/instructions/*.instructions.md` |
| Agent | While selected in mode picker | `.vscode/*.agent.md` |
| **Prompt File** | **When explicitly invoked** | `.vscode/prompts/*.prompt.md` |

**Setup (do this before the demo):**
1. Copy `generate-changelog.prompt.md` to `.vscode/prompts/generate-changelog.prompt.md`
2. Reload VS Code (Ctrl+Shift+P → *"Developer: Reload Window"*)
3. Open Copilot Chat → type `/` → verify **generate-changelog** appears in the list

**How to demo:**
1. Open `productCatalog.js` — point out the inline `// v2.1.0` version comments
2. Open Copilot Chat → type `/generate-changelog` and invoke it
   → Copilot produces a structured Keep-a-Changelog entry with Added/Changed/Fixed sections
3. Ask the same question **without** the prompt file: *"What changed in this module?"*
   → Compare the consistency and format of the two responses
4. Modify `restock()` to accept an optional `note` parameter, then invoke `/generate-changelog` again
   → The prompt file picks up the change and updates the entry
5. Bonus: open `generate-changelog.prompt.md`, add an `Impact` field to the output format,
   reload, run the prompt again, and compare

**Key talking points:**
- Prompt files are **invoked on demand** — you decide when to use them
- `/` is the invocation mechanism — prompt files appear alongside built-in Copilot slash commands
- `mode: ask` keeps the output in Chat; `mode: edit` would apply changes directly to files
- Prompt files are version-controlled in `.vscode/prompts/` and shared with the team
- Build a library of prompt files: `generate-changelog`, `write-adr`, `document-api`, `write-pr-description`…

---

## Demo 12 — Copilot Skill (`12-copilot-skill/customerService.js`)

**What it shows:** How `SKILL.md` files create packaged, discoverable, reusable capabilities
that agents discover automatically and users can invoke via slash commands — with bundled
reference assets that guide consistent, high-quality output.

**Files:**
- `customerService.js` — an undocumented JavaScript service module (the demo target)
- `SKILL.md` — the skill definition to install in the repository
- `references/doc-standards.md` — bundled documentation standards the skill references

**How Skills differ from Agents, Instructions, and Prompt Files:**

| Feature | When active | File location | Invocation |
|---|---|---|---|
| Instructions | Always — every suggestion | `.vscode/instructions/*.instructions.md` | Automatic |
| Agent | While selected in mode picker | `.vscode/*.agent.md` | Manual (mode picker) |
| Prompt File | When explicitly invoked | `.vscode/prompts/*.prompt.md` | Manual (`/command`) |
| **Skill** | **When relevant to the task** | `.github/skills/<name>/SKILL.md` | **Automatic or `/command`** |

**Key advantages over Prompt Files:**
- Agents can discover and invoke skills **automatically** from the `description` field — no `/` needed
- Skills bundle additional assets (reference docs, templates, scripts) alongside instructions
- Skills follow the open [Agent Skills specification](https://agentskills.io/) — portable across AI tools
- Community skills can be installed with `gh skill install github/awesome-copilot <skill-name>`

**Setup (do this before the demo):**
1. Create the skill directory: `mkdir -p .github/skills/document-api/references`
2. Copy `SKILL.md` to `.github/skills/document-api/SKILL.md`
3. Copy `references/doc-standards.md` to `.github/skills/document-api/references/doc-standards.md`
4. Reload VS Code (Ctrl+Shift+P → *"Developer: Reload Window"*)
5. Open Copilot Chat → type `/` → verify **document-api** appears in the list

**How to demo:**
1. Open `customerService.js` — walk through the file; note zero JSDoc on all exported members
2. Open Copilot Chat → type `/document-api`
   → Copilot generates complete JSDoc following the bundled standards in `references/doc-standards.md`
   (module header, class description, `@param`/`@returns`/`@throws` on every method, `@example` blocks)
3. Open `references/doc-standards.md` — explain this file is bundled inside the skill folder
   and loaded automatically alongside `SKILL.md` to give Copilot richer, project-specific context
4. **Compare with a plain prompt:** Undo the documentation, then ask Chat:
   *"Add JSDoc comments to this file"*
   → Note: output is inconsistent — missing `@throws`, vague `@returns`, no `@module` header
5. Open `SKILL.md` — point out the YAML frontmatter: `name` and `description` fields
6. Demonstrate **automatic agent discovery**: switch to **Agent** mode, type
   *"Can you document the customerService module for me?"*
   → The agent picks the skill automatically from the `description` — no `/` needed
7. Bonus: add a new rule to `SKILL.md` (e.g., *"Always add `@since` tags with today's date"*),
   reload, re-run `/document-api` and compare the output

**Key talking points:**
- Skills are **folders** (not single files) — they package all the context the AI needs
- Agents discover skills **automatically** based on the `description` — the `/` slash command is optional
- Bundled reference files prevent hallucination: the AI reads *your* standards, not its defaults
- Skills follow the open [Agent Skills specification](https://agentskills.io/) — portable across AI tools (Copilot, Claude, etc.)
- Store skills in `.github/skills/` to share with the team, or `~/.copilot/skills/` for personal use
- Install community skills: `gh skill install github/awesome-copilot <skill-name>`
- Skills vs. Prompt Files: prompts need explicit `/` invocation; skills are discovered by agents automatically

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
