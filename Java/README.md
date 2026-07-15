# Java Demo Examples

Each folder contains a self-contained Java example designed to showcase a specific
Copilot capability during a live session.

> **Prerequisites:** JDK 17+, VS Code with GitHub Copilot extension and Extension Pack for Java.

---

<a id="demo-map"></a>

## Demo Map

| # | Folder | Course Topic | Copilot Feature | Time |
|---|--------|-------------|-----------------|------|
| [1](#demo-1) | `01-code-completions` | Where Copilot Shows Up | **Inline suggestions** (Tab/Esc) | 5 min |
| [2](#demo-2) | `02-chat-and-refactoring` | Suggestions vs. Chat | **Chat & Inline Chat** refactoring | 8 min |
| [3](#demo-3) | `03-test-generation` | Testing & Quality Workflows | **Test generation** (`/tests` for JUnit) | 8 min |
| [4](#demo-4) | `04-documentation` | Developer Flow → Document | **JavaDoc & README generation** | 5 min |
| [5](#demo-5) | `05-prompt-engineering` | Prompt Engineering: The 4 S's | **Prompt quality comparison** | 8 min |
| [6](#demo-6) | `06-bug-detection` | Responsible AI & Validation | **Security review & bug finding** | 8 min |
| [7](#demo-7) | `07-explain-and-debug` | Core Developer Workflows | **Code explanation & optimization** | 5 min |
| [8](#demo-8) | `08-plan-mode` | Suggestions vs. Chat (Modes) | **Plan mode** — research & outline | 8 min |
| [9](#demo-9) | `09-copilot-instructions` | Customizing Copilot | **Copilot Instructions** — project-wide coding standards | 8 min |
| [10](#demo-10) | `10-copilot-agent` | Customizing Copilot | **Custom Copilot Agent** — reusable chat participant | 8 min |
| [11](#demo-11) | `11-copilot-prompt` | Customizing Copilot | **Copilot Prompt File** — on-demand invocable prompt | 8 min |
| [12](#demo-12) | `12-copilot-skill` | Customizing Copilot | **Copilot Skill** — packaged, discoverable, reusable capability | 8 min |
| [13](#demo-13) | `13-copilot-hooks` | Customizing Copilot | **Copilot Hooks** — lifecycle automation, security guardrails & audit logging | 8 min |
| [14](#demo-14) | `14-issue-to-pr` | Core Developer Workflows | **Issue to Pull Request** — fix a bug end-to-end (issue → fix → PR → merge) | 12 min |
| [15](#demo-15) | `15-github-agent-task` | Core Developer Workflows | **Agent task on GitHub.com** — delegate work to the coding agent from the browser | 10 min |

---

<a id="demo-1"></a>

## Demo 1 — Code Completions (`01-code-completions/Inventory.java`)

**What it shows:** Copilot's real-time inline suggestions as you type.

**Scenario:** A warehouse product-inventory system with an `Inventory` class. Five methods 
are described as `// TODO` comments but not implemented.

**How to demo:**
1. Open `Inventory.java` in VS Code
2. Place your cursor at the end of a `// TODO` comment and press **Enter**
3. Watch Copilot suggest the full method implementation
4. Press **Tab** to accept, **Esc** to dismiss, **Alt+]** to cycle alternatives
5. Repeat for all five methods
6. Bonus: start typing `public static void main` at the bottom — Copilot scaffolds a demo script

**Key talking points:**
- Context: Copilot reads the class structure, existing methods, and type signatures
- The better the comment, the better the suggestion (4 S's preview)
- Always review before accepting — especially important in a statically-typed language


[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-2"></a>

## Demo 2 — Chat & Refactoring (`02-chat-and-refactoring/Orders.java`)

**What it shows:** Using Copilot Chat and Inline Chat to refactor messy code.

**Scenario:** An order-processing module with deeply nested if/else, verbose domain logic,
and repetitive shipping-cost calculations. It works but is hard to maintain.

**How to demo:**
1. Open `Orders.java` — point out the deeply nested `processOrder()` method
2. Select `processOrder()` → **Ctrl+I** (Inline Chat) → type:
   *"Refactor using early returns to reduce nesting"*
3. Select `calculateShipping()` → Chat → type:
   *"Simplify this with a map-based lookup"*
4. Ask Chat: *"Add JavaDoc comments to all public methods in this file"*
5. Compile with **javac** before and after to show it still works

**Key talking points:**
- Chat modes: Ask (explain) vs. Agent (edit files)
- Copilot understands intent, not just syntax
- Always compile after refactoring to catch type errors early


[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-3"></a>

## Demo 3 — Test Generation (`03-test-generation/Calculator.java`)

**What it shows:** Copilot generates comprehensive test suites instantly.

**Scenario:** A `Calculator` class with arithmetic, memory, and history features, plus
a `UnitConverter` with temperature/weight/distance conversions. Fully implemented, zero
tests.

**How to demo:**
1. Open `Calculator.java` → Copilot Chat → type `/tests` or
   *"Generate JUnit 5 tests for this file, including edge cases and null checks"*
2. Copilot creates `CalculatorTest.java` with organized test classes using `@ParameterizedTest`
3. Run: **Ctrl+Shift+D** (Run Tests) or `mvn test`
4. Select `divide()` → Inline Chat → *"What edge cases am I missing?"*
5. Copilot suggests: division by zero, NaN, infinity, null parameters

**Key talking points:**
- Copilot generates edge cases you might forget (null checks, boundary conditions, exceptions)
- Tests still need human review — does the expected value make sense?
- Use Copilot for the boilerplate, add domain-specific assertions yourself
- Java's strong typing helps Copilot produce more accurate test code than dynamic languages


[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-4"></a>

## Demo 4 — Documentation (`04-documentation/WeatherClient.java`)

**What it shows:** Copilot generates JavaDoc, type hints, and README files.

**Scenario:** A weather-forecast client that calls the Open-Meteo API. It works but
has zero JavaDoc — no method documentation, no class summary, no README.

**How to demo:**
1. Open `WeatherClient.java` → Chat → *"Add comprehensive JavaDoc to every public method"*
2. Select `getTemperature()` → Inline Chat → *"Add detailed JavaDoc with @param, @return, @throws"*
3. Ask Chat: *"Write a README.md explaining how to use this module with code examples"*
4. Ask Chat: *"Are there any null pointer risks I should document?"*

**Key talking points:**
- JavaDoc is where Copilot saves the most time with the least risk
- The generated docs are a starting point — verify the descriptions are accurate
- Great for onboarding: generate docs for legacy code new team members need to learn
- Static typing makes documentation generation safer (fewer type ambiguities)


[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-5"></a>

## Demo 5 — Prompt Engineering: The 4 S's (`05-prompt-engineering/Prompts.java`)

**What it shows:** How prompt quality directly affects Copilot's suggestions.

**Scenario:** Four examples, each with a BAD prompt (vague) and a GOOD prompt
(following the 4 S's: Single, Specific, Short, Surround). The implementations are
provided so you can compare.

**How to demo:**
1. Open `Prompts.java` — walk through the 4 S's framework with Java examples
2. **Live experiment:** Delete the `slugify()` body, uncomment the BAD prompt
   (`// TODO: do string stuff`) → see what Copilot suggests
3. Now uncomment the GOOD prompt instead → Copilot produces exactly `slugify()`
4. Show Example D ("Surround"): descriptive method name + explicit return type + JavaDoc
   give Copilot perfect context even without a comment
5. Compile `javac Prompts.java` to verify all examples work

**Key talking points:**
- Single: one task per prompt, not "do everything"
- Specific: "URL-safe slug" vs. "string stuff"
- Short: detailed ≠ long — be precise, not verbose
- Surround: good names and types are implicit prompts
- In Java, type signatures are already part of the "surround" context


[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-6"></a>

## Demo 6 — Bug Detection & Code Review (`06-bug-detection/BuggyAuth.java`)

**What it shows:** Copilot as a code reviewer that catches security issues.

**Scenario:** An authentication module with **6 intentional security vulnerabilities**:
MD5 password hashing, SQL injection, timing attack, no rate limiting, predictable
tokens, and password logging.

**How to demo:**
1. Open `BuggyAuth.java` → Chat → *"Review this code for security vulnerabilities and bugs"*
2. Watch Copilot identify most (or all) of the 6 vulnerabilities
3. Select `hashPassword()` → Inline Chat → *"Is this secure? What should I use instead?"*
4. Ask Chat: *"Rewrite this module following OWASP authentication best practices"*
5. Compare the rewritten version — discuss what changed and why

**Key talking points:**
- Copilot output is a starting point, not final truth (Responsible AI)
- Developer accountability remains — always validate
- This demo maps directly to the "Key Risks" slide: incorrect logic, security gaps, over-reliance
- Static typing doesn't prevent logic vulnerabilities — security review is still essential


[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-7"></a>

## Demo 7 — Explain Code & Debug (`07-explain-and-debug/LegacyParser.java`)

**What it shows:** Copilot explains unfamiliar code and finds performance issues.

**Scenario:** A log-file parser with regex patterns, datetime parsing, and a subtle
O(n²) duplicate-detection bug. The code works but is hard to read and slow at scale.

**How to demo:**
1. Open `LegacyParser.java` → select the regex → Inline Chat →
   *"What does this regex match? Explain each group."*
2. Select `analyzeLogFile()` → Chat →
   *"This is slow for large files. Find the performance issue."*
3. Copilot identifies the O(n²) nested loop in duplicate detection
4. Ask Chat: *"Fix it using a HashSet for O(n) duplicate detection"*
5. Compile and show performance difference with large test files

**Key talking points:**
- Copilot is invaluable when onboarding to a new codebase (~25% speed increase)
- Regex explanation alone saves significant debugging time
- Performance optimization: Copilot spots algorithmic issues humans easily miss
- Collections API knowledge helps Copilot suggest optimal data structures


[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-8"></a>

## Demo 8 — Plan Mode (`08-plan-mode/TaskManager.java`)

**What it shows:** Plan mode researches your codebase and outlines a multi-step
implementation plan BEFORE making any changes.

**Scenario:** A working CLI task manager — add, complete, delete, search tasks. It
runs entirely in memory with no persistence, no validation, no API layer. It's
deliberately "ready for the next step" so Plan mode has something meaningful to plan.

**How to demo:**
1. Open `TaskManager.java` — quickly show the working CLI (compile, run it, add a task, exit)
2. Open Copilot Chat → switch to **Plan** mode (mode picker at the top)
3. Type: *"Add SQLite persistence so tasks survive a restart. Keep the CLI interface."*
4. Copilot outlines a step-by-step plan (create DB schema, add JDBC connection, migrate class…)
   **without changing any files**
5. Refine: *"Also add input validation — titles 3-100 chars, priority must be LOW/MEDIUM/HIGH"*
6. The plan updates incrementally
7. Switch to **Agent** mode → *"Implement the plan"* → Copilot executes everything

**Key talking points:**
- Plan mode = think first, code later
- You control the architecture before any code is written
- The plan is iterative — refine it before committing
- Perfect for complex features, onboarding, and multi-file changes
- Maps to the slide: "Plan (Research and outline multi-step plans)"


[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-9"></a>

## Demo 9 — Copilot Instructions (`09-copilot-instructions/OrderProcessor.java`)

**What it shows:** How `*.instructions.md` files let you define project-wide coding standards
that Copilot automatically applies to every suggestion — without repeating rules in each prompt.

**Files:**
- `OrderProcessor.java` — skeleton order processor with TODO methods to complete
- `java-coding-standards.instructions.md` — the instructions file to install in the workspace

**Setup (do this before the demo):**
1. Copy `java-coding-standards.instructions.md` to `.github/instructions/java-coding-standards.instructions.md`
2. Reload VS Code (Ctrl+Shift+P → *"Developer: Reload Window"*)
3. Verify the file appears in **Copilot Chat → Manage Instructions**

**How to demo:**
1. Open `OrderProcessor.java` — show the TODO methods and the instructions file side by side
2. **Without** the instructions file active: delete it, reload, complete `validateOrder()` — note the output style
3. Restore the instructions file, reload, complete the same method again — compare:
   - Comprehensive JavaDoc added automatically
   - Proper exception types used (IllegalArgumentException vs. generic Exception)
   - Logger used for warnings and errors
   - Stream API used for collections where appropriate
4. Open Chat → ask: *"Does this class follow our coding standards?"*
5. Ask Chat: *"Review OrderProcessor and flag any violations of our standards"*
6. Bonus: add a new rule to `java-coding-standards.instructions.md` (e.g., "use records for immutable data"), 
   then ask Copilot to update the existing code to comply

**Key talking points:**
- Instructions files are picked up automatically — zero prompt overhead
- `applyTo` frontmatter scopes rules to specific file patterns
- `.github/instructions/` = repo scope; shared with the whole team via version control
- Great for onboarding: new developers get team standards for free
- Instructions work in both inline completions and Chat


[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-10"></a>

## Demo 10 — Custom Copilot Agent (`10-copilot-agent/PaymentService.java`)

**What it shows:** How `.agent.md` files let you define a custom Copilot chat participant
with its own name, system prompt, and toolset — selectable from the Chat mode picker.

**Files:**
- `PaymentService.java` — a payment module with intentional security and quality issues to review
- `code-reviewer.agent.md` — the custom agent definition to install in the workspace

**How Agents differ from Instructions:**

| Feature | When active | File location |
|---|---|---|
| Instructions | Always — every suggestion | `.github/instructions/*.instructions.md` |
| **Agent** | **While selected in mode picker** | `.github/*.agent.md` |

**Setup (do this before the demo):**
1. Copy `code-reviewer.agent.md` to `.github/code-reviewer.agent.md`
2. Reload VS Code (Ctrl+Shift+P → *"Developer: Reload Window"*)
3. Open Copilot Chat → click the mode picker → verify **Code Reviewer** appears

**How to demo:**
1. Open `PaymentService.java` — briefly walk through the file, pointing out it "looks like real code"
2. Switch Chat to the **Code Reviewer** agent
3. Type: *"Review this module"*
   → Copilot produces a structured report: Critical Issues, Warnings, Suggestions, Summary
4. Ask: *"What are the security risks in this file?"*
   → Agent focuses on SQL injection, MD5, hard-coded secrets, null pointer risks
5. Ask: *"Rewrite `fetchUserOrders` to fix the issues you found"*
   → Agent generates a corrected version with parameterized queries, null checks, and error handling
6. Switch back to the default agent and ask the same question — compare the output style
7. Bonus: add a rule to `code-reviewer.agent.md` (e.g., *"flag methods over 30 lines as potentially needing refactoring"*),
   reload, and re-run the review

**Key talking points:**
- Custom agents appear in the mode picker like first-class Copilot modes
- The system prompt shapes every response — the agent is always "in role"
- Different agents for different workflows: reviewer, architect, test-writer, documenter…
- Agents can access the codebase, open problems, and recent changes via their `tools` list
- Stored in `.github/` — version-controlled and shared with the whole team


[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-11"></a>

## Demo 11 — Copilot Prompt File (`11-copilot-prompt/ProductCatalog.java`)

**What it shows:** How `.prompt.md` files create on-demand, invocable prompt files
distinct from always-on Instructions and persistent Agent modes.

**Files:**
- `ProductCatalog.java` — a versioned product catalog module (the demo target)
- `generate-changelog.prompt.md` — the prompt file definition to install in the workspace

**How Prompt Files differ from Agents and Instructions:**

| Feature | When active | File location |
|---|---|---|
| Instructions | Always — every suggestion | `.github/instructions/*.instructions.md` |
| Agent | While selected in mode picker | `.github/*.agent.md` |
| **Prompt File** | **When explicitly invoked** | `.github/prompts/*.prompt.md` |

**Setup (do this before the demo):**
1. Copy `generate-changelog.prompt.md` to `.github/prompts/generate-changelog.prompt.md`
2. Reload VS Code (Ctrl+Shift+P → *"Developer: Reload Window"*)
3. Open Copilot Chat → type `/` → verify **generate-changelog** appears in the list

**How to demo:**
1. Open `ProductCatalog.java` — point out the inline `// v2.1.0` version comments
2. Open Copilot Chat → type `/generate-changelog` and invoke it
   → Copilot produces a structured Keep-a-Changelog entry with Added/Changed/Fixed sections
3. Ask the same question **without** the prompt file: *"What changed in this module?"*
   → Compare the consistency and format of the two responses
4. Modify `restock()` to accept a `note: String` parameter, then invoke `/generate-changelog` again
   → The prompt file picks up the change and updates the entry
5. Bonus: open `generate-changelog.prompt.md`, add an `Impact` field to the output format,
   reload, run the prompt again, and compare

**Key talking points:**
- Prompt files are **invoked on demand** — you decide when to use them
- `/` is the invocation mechanism — prompt files appear alongside built-in Copilot slash commands
- `mode: ask` keeps the output in Chat; `mode: edit` would apply changes directly to files
- Prompt files are version-controlled in `.github/prompts/` and shared with the team
- Build a library of prompt files: `generate-changelog`, `write-adr`, `document-api`, `write-pr-description`…


[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-12"></a>

## Demo 12 — Copilot Skill (`12-copilot-skill/CustomerService.java`)

**What it shows:** How `SKILL.md` files create packaged, discoverable, reusable capabilities
that agents discover automatically and users can invoke via slash commands — with bundled
reference assets that guide consistent, high-quality output.

**Files:**
- `CustomerService.java` — an underdocumented Java service module (the demo target)
- `SKILL.md` — the skill definition to install in the repository
- `references/doc-standards.md` — bundled documentation standards the skill references

**How Skills differ from Agents, Instructions, and Prompt Files:**

| Feature | When active | File location | Invocation |
|---|---|---|---|
| Instructions | Always — every suggestion | `.github/instructions/*.instructions.md` | Automatic |
| Agent | While selected in mode picker | `.github/*.agent.md` | Manual (mode picker) |
| Prompt File | When explicitly invoked | `.github/prompts/*.prompt.md` | Manual (`/command`) |
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
1. Open `CustomerService.java` — walk through the file; note minimal JavaDoc on public members
2. Open Copilot Chat → type `/document-api`
   → Copilot generates complete JavaDoc comments following the bundled standards
   (`@param`, `@return`, `@throws` tags on every public method, class JavaDoc with summary)
3. Open `references/doc-standards.md` — explain this file is bundled inside the skill folder
   and loaded automatically alongside `SKILL.md` to give Copilot richer, project-specific context
4. **Compare with a plain prompt:** Undo the documentation, then ask Chat:
   *"Add JavaDoc to this file"*
   → Note: output is inconsistent — missing `@throws`, wrong JavaDoc style, vague `@return`
5. Open `SKILL.md` — point out the YAML frontmatter: `name` and `description` fields
6. Demonstrate **automatic agent discovery**: switch to **Agent** mode, type
   *"Can you document the CustomerService module for me?"*
   → The agent picks the skill automatically from the `description` — no `/` needed
7. Bonus: add a new rule to `SKILL.md` (e.g., *"Always add a class-level JavaDoc comment"*),
   reload, re-run `/document-api` and compare the output

**Key talking points:**
- Skills are **folders** (not single files) — they package all the context the AI needs
- Agents discover skills **automatically** based on the `description` — the `/` slash command is optional
- Bundled reference files prevent hallucination: the AI reads *your* standards, not its defaults
- Skills follow the open [Agent Skills specification](https://agentskills.io/) — portable across AI tools (Copilot, Claude, etc.)
- Store skills in `.github/skills/` to share with the team, or `~/.copilot/skills/` for personal use
- Install community skills: `gh skill install github/awesome-copilot <skill-name>`
- Skills vs. Prompt Files: prompts need explicit `/` invocation; skills are discovered by agents automatically


[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-13"></a>

## Demo 13 — Copilot Hooks (`13-copilot-hooks`)

**What it shows:** How `.github/hooks/*.json` files execute shell commands at Copilot lifecycle events (`sessionStart`, `preToolUse`, `postToolUse`, `sessionEnd`) to enforce security policies, audit tool use, and log sessions — automatically, with zero user interaction.

**Files:**

| File | Purpose |
|------|---------|
| `NotificationService.java` | Demo target — underdocumented Java notification service (Email / SMS / Push) |
| `dev-guardrails.json` | Hook configuration — wires 4 lifecycle events to scripts |
| `scripts/pre-tool-guard.sh` / `.ps1` | `preToolUse` hook — blocks dangerous shell commands |
| `scripts/session-logger.sh` / `.ps1` | `sessionStart` / `sessionEnd` hook — logs session boundaries |
| `scripts/audit-logger.sh` / `.ps1` | `postToolUse` hook — appends every tool call to an audit log |

**Script Behaviors:**

1. **`pre-tool-guard.sh` / `.ps1`** (`preToolUse` hook)
   - Runs **before** any tool is executed, examining the requested command or operation
   - Checks the tool arguments against a predefined list of blocked patterns (e.g., `rm -rf`, `git reset --hard`, `DROP TABLE`)
   - **Denies the tool call immediately** if a dangerous pattern is detected, preventing the operation and displaying a warning to the user
   - Acts as a security firewall: high-risk operations fail safely closed with no bypass option
   - Allows all other tools to proceed normally

2. **`session-logger.sh` / `.ps1`** (`sessionStart` / `sessionEnd` hooks)
   - Runs at the **beginning** of a Copilot session to record a session start timestamp, user ID, and session context to a log file
   - Runs at the **end** of a session to record the session end timestamp and summary
   - Creates an audit trail showing who used Copilot, when, and for how long
   - Useful for compliance tracking and understanding tool adoption across the team

3. **`audit-logger.sh` / `.ps1`** (`postToolUse` hook)
   - Runs **after** each tool call completes, recording detailed information about what was executed
   - Logs the tool name (e.g., `view_file`, `edit_file`, `run_in_terminal`), arguments, result status, and session ID in JSON format to an audit log
   - Creates a complete record of all file reads, edits, and command executions performed by Copilot during the session
   - Enables security teams to audit and replay tool usage for compliance or incident investigation

**Copilot customizations at a glance:**

| Mechanism | Activation | Best for |
|-----------|-----------|----------|
| Instructions | Always active | Coding standards & tone |
| Agent | Mode picker | Specialized workflows |
| Prompt File | Explicit `/command` | Reusable tasks |
| Skill | Auto-discovered | Reusable domain knowledge |
| **Hook** | **Automatic at lifecycle events** | **Automation, guardrails, auditing** |

**Setup:**

```bash
mkdir -p .github/hooks/scripts
cp 13-copilot-hooks/dev-guardrails.json .github/hooks/
cp 13-copilot-hooks/scripts/* .github/hooks/scripts/
chmod +x .github/hooks/scripts/*.sh
```

**How to demo:**

1. Open `NotificationService.java`, switch to **Agent** mode
2. Ask: *"Add comprehensive JavaDoc to all public members"*
3. While the agent runs, open `logs/tool-audit.jsonl` — watch entries appear for each tool call
4. Open `logs/sessions.log` — show the `sessionStart` record
5. **Trigger the deny:** Ask the agent *"Remove all .class files using rm -rf build/"* — watch `pre-tool-guard` block it
6. Open `dev-guardrails.json` and `scripts/pre-tool-guard.sh` — walk through the `BLOCKED_PATTERNS` array
7. Live edit: add a new pattern, reload, try to trigger it

**Key talking points:**
- Hooks run **automatically** — zero user interaction required; users cannot bypass them
- `preToolUse` is **fail-closed**: if the script crashes the tool call is **denied**, never silently allowed
- Hooks receive full JSON context (tool name, arguments, session ID) via stdin
- Use hooks for **compliance/audit logging**, **security guardrails**, and **CI enforcement**
- `.github/hooks/` = repository-wide; `~/.copilot/hooks/` = personal across all projects
- Hooks are **language-agnostic** — the same JSON config and scripts work for any codebase


[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-14"></a>

## Demo 14 — Issue to Pull Request (`14-issue-to-pr`)

**What it shows:** The complete Copilot-assisted bug-fixing loop — from filing a
GitHub Issue, to letting Copilot implement the fix, to opening, reviewing, and
**merging** a Pull Request. This is the "day in the life" demo that ties every
previous feature together.

**Files:**
- `ShoppingCart.java` — a shopping cart with an intentional discount bug
- `ISSUE.md` — a ready-to-paste bug report to open as a GitHub Issue

**The bug:** `applyDiscount()` treats a percentage coupon as a flat amount — a 10%
coupon subtracts 10 instead of 10% of the subtotal, so a €150 cart totals **€140**
instead of the correct **€135**.

**Prerequisites:**
- This repository pushed to GitHub (so Issues and PRs are available)
- GitHub Copilot enabled; optionally the **Copilot coding agent** for autonomous fixes
- `gh` CLI signed in (optional, for the command-line steps)

**How to demo:**

1. **Reproduce the bug** — compile and run the program, then read the output:
   ```bash
   cd 14-issue-to-pr
   javac ShoppingCart.java && java ShoppingCart
   ```
   Point out the wrong total (**€140.00**) vs the expected **€135.00**.

2. **Create the Issue** — either paste `ISSUE.md` into a new issue in the GitHub UI,
   or use the CLI:
   ```bash
   gh issue create --title "Percentage coupons applied as a flat amount" \
     --body-file Java/14-issue-to-pr/ISSUE.md
   ```

3. **Let Copilot fix it** — choose one path:
   - **Coding agent (autonomous):** open the issue on GitHub → **Assignees → Copilot**.
     Copilot creates a branch, implements the fix, and opens a **draft Pull Request**.
   - **Copilot Chat (interactive):** in VS Code switch to **Agent** mode and ask:
     *"Fix the bug described in `14-issue-to-pr/ISSUE.md` (issue #N): make `applyDiscount`
     honor `Coupon.isPercentage`, and add JUnit tests for both coupon types."*

4. **Review the Pull Request** — open the PR, read the diff, and run the tests with your
   JUnit setup. Then ask Copilot to review it: comment **`@copilot review`** on the PR, or use
   the **Copilot code review** button. Iterate by replying to review comments.

5. **Merge** — approve the PR and choose **Squash and merge**. Because the PR body
   contains `Fixes #N`, merging **automatically closes the issue**. Delete the branch.

**Key talking points:**
- Copilot spans the *entire* workflow — issue triage, implementation, review, and merge —
  not just inline code completion
- The acceptance-criteria checklist in `ISSUE.md` steers both the fix and the tests
- `Fixes #N` / `Closes #N` in the PR description links code to the issue and auto-closes it
- The Copilot coding agent works asynchronously on GitHub; Chat keeps you in the editor —
  show both and contrast them
- **You stay accountable:** always review an AI-authored PR before merging (Responsible AI)


[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-15"></a>

## Demo 15 — Run an Agent Task on GitHub.com (`15-github-agent-task`)

**What it shows:** How to delegate a whole coding task to the GitHub Copilot
**coding agent** directly from the browser at **<https://github.com/copilot/agents>** —
no local editor required. You describe the work in plain language; the agent spins up
a cloud session, creates a branch, edits files, runs the build/tests, and opens a
**Pull Request** for you to review.

**Files:**
- `TextAnalyzer.java` — a small text-analysis class with only word/character counting implemented
- `TASK.md` — a ready-to-paste prompt describing the feature work to hand to the agent

**The task:** implement four missing methods (`countSentences`, `averageWordLength`,
`estimateReadingTimeMinutes`, `topWords`), add JUnit tests, refresh the demo output,
and write a short README — all done autonomously by the agent on GitHub.com.

**How this differs from Demo 14:**

| | Demo 14 — Issue to PR | **Demo 15 — Agent task on GitHub.com** |
|---|---|---|
| Starting point | A filed GitHub **Issue** | A **natural-language prompt** in the browser |
| Where you work | VS Code or the issue page | **github.com/copilot/agents** (no editor) |
| Trigger | Assign the issue to Copilot | Start a task from the Agents page |
| Result | Branch + Pull Request | Branch + Pull Request |

**Prerequisites:**
- This repository pushed to GitHub
- **GitHub Copilot coding agent** enabled for your account/organization
- Write access to the repository (so the agent can push a branch and open a PR)

**How to demo:**

1. **Show the starting point** — compile and run the program so the audience sees what's missing:
   ```bash
   cd 15-github-agent-task
   javac TextAnalyzer.java && java TextAnalyzer
   ```
   Point out that sentences, average word length, reading time, and top words are *not* implemented.

2. **Open the Agents page** — go to **<https://github.com/copilot/agents>** in the browser
   and select this repository (and the `main` branch) as the target.

3. **Start the task** — paste the **Prompt** from `TASK.md` and launch it. Optionally pick a
   different base branch. The agent starts an asynchronous session running on GitHub.

4. **Watch the session** — open the live agent session and narrate what it does: reads the repo,
   plans the change, edits `TextAnalyzer.java`, adds tests, and runs the build/tests. You can keep
   working elsewhere while it runs in the background.

5. **Review the Pull Request** — when the agent finishes it opens a **draft PR**. Open it, read the
   diff and the agent's summary, and check the CI run. Request changes by commenting
   **`@copilot ...`** or using **Copilot code review**; the agent pushes follow-up commits.

6. **Merge** — approve and **Squash and merge**, then delete the branch. Pull locally and re-run
   the program/tests to confirm the new metrics work.

**Key talking points:**
- The coding agent runs **on GitHub.com**, not in your editor — great for delegating work from any device, even a phone
- It works **asynchronously**: start a task, walk away, come back to a finished PR
- You can launch tasks **without an Issue** — a clear prompt is enough (contrast with Demo 14)
- The agent operates in a sandboxed GitHub Actions environment; it can run builds and tests before opening the PR
- **You stay accountable:** the output is a PR you review and approve — never an auto-merge (Responsible AI)
- A precise prompt with **acceptance criteria** (see `TASK.md`) steers both the implementation and the tests


[⬆ Back to Demo Map](#demo-map)

---

## Tips for the Presenter

1. **Compile each file first** to make sure everything works in your environment
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
cd GitHubCopilot-Demos/Java

# Verify Java environment:
javac -version
java -version

# Compile all examples:
for dir in */; do javac "$dir"*.java; done

# Verify all examples compile:
javac 01-code-completions/Inventory.java
javac 02-chat-and-refactoring/Orders.java
javac 03-test-generation/Calculator.java
javac 05-prompt-engineering/Prompts.java
javac 07-explain-and-debug/LegacyParser.java
```
