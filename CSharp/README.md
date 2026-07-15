# C# Demo Examples

Each folder contains a self-contained C# example designed to showcase a specific
Copilot capability during a live session.

> **Prerequisites:** .NET 8+, VS Code with GitHub Copilot & C# Dev Kit extensions, `xUnit` for test generation.

---

<a id="demo-map"></a>

## Demo Map

| # | Folder | Course Topic | Copilot Feature | Time |
| --- | -------- | ------------- | ----------------- | ------ |
| [1](#demo-1) | `01-code-completions` | Where Copilot Shows Up | **Inline suggestions** (Tab/Esc) | 5 min |
| [2](#demo-2) | `02-chat-and-refactoring` | Suggestions vs. Chat | **Chat & Inline Chat** refactoring | 8 min |
| [3](#demo-3) | `03-test-generation` | Testing & Quality Workflows | **Test generation** (`/tests`) | 8 min |
| [4](#demo-4) | `04-documentation` | Developer Flow → Document | **XML doc & README generation** | 5 min |
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

<a id="demo-1"></a>

## Demo 1 — Code Completions (`01-code-completions/Inventory.cs`)

**What it shows:** Copilot's real-time inline suggestions as you type.

**Scenario:** A warehouse product-inventory system with a `Product` record and an
`Inventory` class. Five methods are described as `// TODO` comments but not implemented.

**How to demo:**

1. Open `Inventory.cs` in VS Code
2. Place your cursor at the end of a `// TODO` comment and press **Enter**
3. Watch Copilot suggest the full method implementation
4. Press **Tab** to accept, **Esc** to dismiss, **Alt+]** to cycle alternatives
5. Repeat for all five methods
6. Bonus: start typing `public static class Program` — Copilot scaffolds a demo

**Key talking points:**

- Context: Copilot reads the record, class, and existing methods
- The better the comment, the better the suggestion (4 S's preview)
- Always review before accepting

[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-2"></a>

## Demo 2 — Chat & Refactoring (`02-chat-and-refactoring/Orders.cs`)

**What it shows:** Using Copilot Chat and Inline Chat to refactor messy code.

**Scenario:** An `OrderProcessor` with deeply nested if/else, repetitive shipping-cost
logic, and string-concatenation receipt formatting. It works but is hard to maintain.

**How to demo:**

1. Open `Orders.cs` — point out the deeply nested `ProcessOrder()` method
2. Select `ProcessOrder()` → **Ctrl+I** (Inline Chat) → type:

   ```text

   Refactor using early returns and guard clauses

   ```

3. Select `CalculateShipping()` → Chat → type:

   ```text

   Simplify this with a dictionary/table lookup

   ```

4. Ask Chat:

   ```text

   Convert these static methods into a proper service class with dependency injection

   ```

5. Build before and after to show it still compiles

**Key talking points:**

- Chat modes: Ask (explain) vs. Agent (edit files)
- Copilot understands C# idioms: records, pattern matching, LINQ
- Always build and test after refactoring

[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-3"></a>

## Demo 3 — Test Generation (`03-test-generation/Calculator.cs`)

**What it shows:** Copilot generates comprehensive test suites instantly.

**Scenario:** A `Calculator` class with arithmetic, memory, and history features, plus
a `UnitConverter` with temperature/weight/distance conversions. Fully implemented, zero
tests.

**How to demo:**

1. Open `Calculator.cs` → Copilot Chat → type `/tests` or

   ```text

   Generate xUnit tests for this file, including edge cases

   ```

2. Copilot creates a test class with `[Fact]` and `[Theory]` attributes
3. Run: `dotnet test`
4. Select `Divide()` → Inline Chat →

   ```text

   What edge cases am I missing?

   ```

5. Copilot suggests: zero division, `double.NaN`, `double.MaxValue`, precision

**Key talking points:**

- Copilot generates `[Theory]`/`[InlineData]` for parameterised tests
- Tests still need human review — does the expected value make sense?
- Use Copilot for the boilerplate, add domain-specific assertions yourself

[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-4"></a>

## Demo 4 — Documentation (`04-documentation/WeatherClient.cs`)

**What it shows:** Copilot generates XML doc comments, type annotations, and README files.

**Scenario:** A weather-forecast client that calls the Open-Meteo API using `HttpClient`.
It works but has zero documentation — no XML docs, no comments, no README.

**How to demo:**

1. Open `WeatherClient.cs` → Chat →

   ```text

   Add XML documentation to every public method

   ```

2. Select `GetTemperatureAsync()` → Inline Chat →

   ```text

   Add a detailed `<summary>`, `<param>`, `<returns>`, `<exception>` XML doc

   ```

3. Ask Chat:

   ```text

   Write a README.md explaining how to use this module with examples

   ```

4. Ask Chat:

   ```text

   Are there any methods that should throw documented exceptions?

   ```

**Key talking points:**

- Documentation is where Copilot saves the most time with the least risk
- Generated docs are a starting point — verify accuracy
- Great for onboarding: generate docs for legacy code new developers need to learn

[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-5"></a>

## Demo 5 — Prompt Engineering: The 4 S's (`05-prompt-engineering/Prompts.cs`)

**What it shows:** How prompt quality directly affects Copilot's suggestions.

**Scenario:** Four examples, each with a BAD prompt (vague) and a GOOD prompt
(following the 4 S's: Single, Specific, Short, Surround).

**How to demo:**

1. Open `Prompts.cs` — walk through the 4 S's framework
2. **Live experiment:** Delete the `Slugify()` body, uncomment the BAD prompt

   (`// TODO: do string stuff`) → see what Copilot suggests

3. Now uncomment the GOOD prompt instead → Copilot produces exactly `Slugify()`
4. Show Example D ("Surround"): descriptive method name + record types + XML doc

   give Copilot perfect context without any comment

5. Build and run to verify all examples work

**Key talking points:**

- Single: one task per prompt, not "do everything"
- Specific: "URL-safe slug" vs. "string stuff"
- Short: detailed ≠ long — be precise, not verbose
- Surround: good names, types, and XML docs are implicit prompts

[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-6"></a>

## Demo 6 — Bug Detection & Code Review (`06-bug-detection/BuggyAuth.cs`)

**What it shows:** Copilot as a code reviewer that catches security issues.

**Scenario:** An `AuthService` class with **6 intentional security vulnerabilities**:
MD5 password hashing, SQL injection, timing attack, no rate limiting, predictable
tokens, and password logging.

**How to demo:**

1. Open `BuggyAuth.cs` → Chat →

   ```text

   Review this code for bugs and security issues

   ```

2. Watch Copilot identify most (or all) of the 6 bugs
3. Select `HashPassword()` → Inline Chat →

   ```text

   Is this secure?

   ```

4. Ask Chat:

   ```text

   Rewrite this module following OWASP best practices

   ```

5. Compare the rewritten version — discuss `BCrypt`, parameterised queries,

   `CryptographicOperations.FixedTimeEquals`, `RandomNumberGenerator`

**Key talking points:**

- Copilot output is a starting point, not final truth (Responsible AI)
- Developer accountability remains — always validate
- Maps to the "Key Risks" slide: incorrect logic, security gaps, over-reliance

[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-7"></a>

## Demo 7 — Explain Code & Debug (`07-explain-and-debug/LegacyParser.cs`)

**What it shows:** Copilot explains unfamiliar code and finds performance issues.

**Scenario:** A log-file parser with compiled `Regex`, `DateTime` parsing, and a subtle
O(n²) duplicate-detection bug. The code works but is hard to read and slow at scale.

**How to demo:**

1. Open `LegacyParser.cs` → select the regex → Inline Chat →

   ```text

   What does this regex match? Explain each capture group.

   ```

2. Select `AnalyzeLogFile()` → Chat →

   ```text

   This is slow for large files. Find the performance issue.

   ```

3. Copilot identifies the O(n²) nested loop in duplicate detection
4. Ask Chat:

   ```text

   Fix it using a HashSet for O(n) duplicate detection

   ```

5. Build and run to show output before and after

**Key talking points:**

- Copilot is invaluable when onboarding to a new codebase (~25% speed increase)
- Regex explanation alone saves significant debugging time
- Performance optimization: Copilot spots algorithmic issues humans easily miss

[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-8"></a>

## Demo 8 — Plan Mode (`08-plan-mode/TaskManager.cs`)

**What it shows:** Plan mode researches your codebase and outlines a multi-step
implementation plan BEFORE making any changes.

**Scenario:** A working CLI task manager — add, complete, delete, search tasks. It
runs entirely in memory with no persistence, no validation, no API layer. It's
deliberately "ready for the next step" so Plan mode has something meaningful to plan.

**How to demo:**

1. Open `TaskManager.cs` — quickly show the working CLI (run it, add a task, quit)
2. Open Copilot Chat → switch to **Plan** mode (mode picker at the top)
3. Type:

   ```text

   Add SQLite persistence using Microsoft.Data.Sqlite so tasks survive a restart. Keep the CLI.

   ```

4. Copilot outlines a step-by-step plan (add NuGet package, create DB schema, add repository class…)

   **without changing any files**

5. Refine:

   ```text

   Also add input validation — titles 3-100 chars, priority must be Low/Medium/High

   ```

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

## Demo 9 — Copilot Instructions (`09-copilot-instructions/OrderProcessor.cs`)

**What it shows:** How `*.instructions.md` files let you define project-wide coding standards
that Copilot automatically applies to every suggestion — without repeating rules in each prompt.

**Files:**

- `OrderProcessor.cs` — skeleton order processor with TODO methods to complete
- `coding-standards.instructions.md` — the instructions file to install in the workspace

**Setup (do this before the demo):**

1. Copy `coding-standards.instructions.md` to `.github/instructions/coding-standards.instructions.md`
2. Reload VS Code (Ctrl+Shift+P → *"Developer: Reload Window"*)
3. Verify the file appears in **Copilot Chat → Manage Instructions**

**How to demo:**

1. Open `OrderProcessor.cs` — show the TODO methods and the instructions file side by side
2. **Without** the instructions file active: delete it, reload, complete `ValidateOrderAsync` — note the output style
3. Restore the instructions file, reload, complete the same method again — compare:
   - XML documentation added automatically
   - `CancellationToken` parameter included
   - Specific exception types used
   - `ILogger` used instead of `Console.WriteLine`
4. Open Chat → ask:

   ```text

   Does this class follow our coding standards?

   ```

5. Ask Chat:

   ```text

   Review OrderProcessor and flag any violations of our standards

   ```

6. Bonus: add a new rule to `coding-standards.instructions.md`, then ask Copilot to update the existing code to comply

**Key talking points:**

- Instructions files are picked up automatically — zero prompt overhead
- `applyTo` frontmatter scopes rules to specific file patterns
- `.github/instructions/` = repo scope; shared with the whole team via version control
- Great for onboarding: new developers get team standards for free
- Instructions work in both inline completions and Chat

[⬆ Back to Demo Map](#demo-map)

---

<a id="demo-10"></a>

## Demo 10 — Custom Copilot Agent (`10-copilot-agent/PaymentService.cs`)

**What it shows:** How `.agent.md` files let you define a custom Copilot chat participant
with its own name, system prompt, and toolset — selectable from the Chat mode picker.

**Files:**

- `PaymentService.cs` — a payment service with intentional security and quality issues to review
- `code-reviewer.agent.md` — the custom agent definition to install in the workspace

**How Agents differ from Instructions:**

| Feature | When active | File location |
| --- | --- | --- |
| Instructions | Always — every suggestion | `.github/instructions/*.instructions.md` |
| **Agent** | **While selected in mode picker** | `.github/*.agent.md` |

**Setup (do this before the demo):**

1. Copy `code-reviewer.agent.md` to `.github/code-reviewer.agent.md`
2. Reload VS Code (Ctrl+Shift+P → *"Developer: Reload Window"*)
3. Open Copilot Chat → click the mode picker → verify **Code Reviewer** appears

**How to demo:**

1. Open `PaymentService.cs` — briefly walk through the file, pointing out it "looks like real code"
2. Switch Chat to the **Code Reviewer** agent
3. Type:

   ```text

   Review this file

   ```

   → Copilot produces a structured report: Critical Issues, Warnings, Suggestions, Summary

4. Ask:

   ```text

   What are the security risks in this file?

   ```

   → Agent focuses on SQL injection, MD5, hard-coded connection string

5. Ask:

   ```text

   Rewrite `FetchUserOrders` to fix the issues you found

   ```

   → Agent generates a corrected version with parameterised queries and error handling

6. Switch back to the default agent and ask the same question — compare the output style
7. Bonus: add a rule to `code-reviewer.agent.md` (e.g., *"flag methods over 20 lines"*),

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

## Demo 11 — Copilot Prompt File (`11-copilot-prompt/ProductCatalog.cs`)

**What it shows:** How `.prompt.md` files create on-demand, invocable prompt files
distinct from always-on Instructions and persistent Agent modes.

**Files:**

- `ProductCatalog.cs` — a versioned product catalog module (the demo target)
- `generate-changelog.prompt.md` — the prompt file definition to install in the workspace

**How Prompt Files differ from Agents and Instructions:**

| Feature | When active | File location |
| --- | --- | --- |
| Instructions | Always — every suggestion | `.github/instructions/*.instructions.md` |
| Agent | While selected in mode picker | `.github/*.agent.md` |
| **Prompt File** | **When explicitly invoked** | `.github/prompts/*.prompt.md` |

**Setup (do this before the demo):**

1. Copy `generate-changelog.prompt.md` to `.github/prompts/generate-changelog.prompt.md`
2. Reload VS Code (Ctrl+Shift+P → *"Developer: Reload Window"*)
3. Open Copilot Chat → type `/` → verify **generate-changelog** appears in the list

**How to demo:**

1. Open `ProductCatalog.cs` — point out the inline `// v2.1.0` version comments
2. Open Copilot Chat → type `/generate-changelog` and invoke it

   → Copilot produces a structured Keep-a-Changelog entry with Added/Changed/Fixed sections

3. Ask the same question **without** the prompt file:

   ```text

   What changed in this module?

   ```

   → Compare the consistency and format of the two responses

4. Modify `Restock()` to accept an optional `note` parameter, then invoke `/generate-changelog` again

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

## Demo 12 — Copilot Skill (`12-copilot-skill/CustomerService.cs`)

**What it shows:** How `SKILL.md` files create packaged, discoverable, reusable capabilities
that agents discover automatically and users can invoke via slash commands — with bundled
reference assets that guide consistent, high-quality output.

**Files:**

- `CustomerService.cs` — an undocumented C# service class (the demo target)
- `SKILL.md` — the skill definition to install in the repository
- `references/doc-standards.md` — bundled documentation standards the skill references

**How Skills differ from Agents, Instructions, and Prompt Files:**

| Feature | When active | File location | Invocation |
| --- | --- | --- | --- |
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

1. Open `CustomerService.cs` — walk through the file; note zero XML documentation on all public members
2. Open Copilot Chat → type `/document-api`

   → Copilot generates complete XML documentation following the bundled standards in `references/doc-standards.md`

3. Open `references/doc-standards.md` — explain this file is bundled inside the skill folder

   and loaded automatically alongside `SKILL.md` to give Copilot richer, project-specific context

4. **Compare with a plain prompt:** Undo the documentation, then ask Chat:

   ```text

   Add XML documentation to this file

   ```

   → Note: output is inconsistent — missing `<exception>` tags, vague `<returns>` text, no third-person rule

5. Open `SKILL.md` — point out the YAML frontmatter: `name` and `description` fields
6. Demonstrate **automatic agent discovery**: switch to **Agent** mode, type

   ```text

   Can you document the CustomerService for me?

   ```

   → The agent picks the skill automatically from the `description` — no `/` needed

7. Bonus: add a new rule to `SKILL.md` (e.g., *"Always add `<example>` blocks to public methods"*),

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

**What it shows:** How `.github/hooks/*.json` files execute shell commands at Copilot lifecycle events (`preSendMessage`, `sessionStart`, `preToolUse`, `postToolUse`, `sessionEnd`) to enforce security policies, filter prompts, audit tool use, and log sessions — automatically, with zero user interaction.

**Files:**

| File | Purpose |
| ------ | --------- |
| `NotificationService.cs` | Demo target — undocumented C# service (Email / SMS / Push notifications) |
| `dev-guardrails.json` | Hook configuration — wires 5 lifecycle events to scripts |
| `scripts/prompt-word-filter.sh` / `.ps1` | `preSendMessage` hook — blocks prompts containing prohibited words |
| `scripts/pre-tool-guard.sh` / `.ps1` | `preToolUse` hook — blocks dangerous shell commands |
| `scripts/session-logger.sh` / `.ps1` | `sessionStart` / `sessionEnd` hook — logs session boundaries |
| `scripts/audit-logger.sh` / `.ps1` | `postToolUse` hook — appends every tool call to an audit log |

**Script Behaviors:**

1. **`prompt-word-filter.sh` / `.ps1`** (`preSendMessage` hook)
   - Runs **before** the user's prompt is sent to Copilot
   - Checks the prompt text against a configurable list of blocked words (e.g., `password`, `secret`, `api_key`, `private_key`, `credit_card`, `ssn`)
   - **Denies the message immediately** if a prohibited word is detected, preventing sensitive information from reaching the model
   - Uses case-insensitive word-boundary matching to avoid false positives on substrings
   - Acts as a data-loss-prevention (DLP) filter at the prompt level

2. **`pre-tool-guard.sh` / `.ps1`** (`preToolUse` hook)
   - Runs **before** any tool is executed, examining the requested command or operation
   - Checks the tool arguments against a predefined list of blocked patterns (e.g., `rm -rf`, `git reset --hard`, `DROP TABLE`)
   - **Denies the tool call immediately** if a dangerous pattern is detected, preventing the operation and displaying a warning to the user
   - Acts as a security firewall: high-risk operations fail safely closed with no bypass option
   - Allows all other tools to proceed normally

3. **`session-logger.sh` / `.ps1`** (`sessionStart` / `sessionEnd` hooks)
   - Runs at the **beginning** of a Copilot session to record a session start timestamp, user ID, and session context to a log file
   - Runs at the **end** of a session to record the session end timestamp and summary
   - Creates an audit trail showing who used Copilot, when, and for how long
   - Useful for compliance tracking and understanding tool adoption across the team

4. **`audit-logger.sh` / `.ps1`** (`postToolUse` hook)
   - Runs **after** each tool call completes, recording detailed information about what was executed
   - Logs the tool name (e.g., `view_file`, `edit_file`, `run_in_terminal`), arguments, result status, and session ID in JSON format to an audit log
   - Creates a complete record of all file reads, edits, and command executions performed by Copilot during the session
   - Enables security teams to audit and replay tool usage for compliance or incident investigation

**Copilot customizations at a glance:**

| Mechanism | Activation | Best for |
| ----------- | ----------- | ---------- |
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

1. Open `NotificationService.cs`, switch to **Agent** mode
2. Ask:

   ```text

   Add XML documentation comments to all public members

   ```

3. While the agent runs, open `logs/tool-audit.jsonl` — watch entries appear for each `view_file` / `edit_file` call
4. Open `logs/sessions.log` — show the `sessionStart` record written when the conversation began
5. **Trigger the deny:** Ask the agent *"Delete all .obj files in the bin folder using rm -rf bin/"* — watch `pre-tool-guard` block it and show the deny message to the user
6. **Trigger the prompt filter:** Type a message containing *"show me the password for the database"* — watch `prompt-word-filter` block the prompt before it reaches Copilot
7. Open `dev-guardrails.json` and `scripts/pre-tool-guard.sh` — walk through the `BLOCKED_PATTERNS` array
8. Open `scripts/prompt-word-filter.ps1` — walk through the `$blockedWords` array
9. Live edit: add a new word or pattern, reload, try to trigger it

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

- `ShoppingCart.cs` — a shopping cart with an intentional discount bug
- `ISSUE.md` — a ready-to-paste bug report to open as a GitHub Issue

**The bug:** `ApplyDiscount()` treats a percentage coupon as a flat amount — a 10%
coupon subtracts €10 instead of 10% of the subtotal, so a €150 cart totals **€140**
instead of the correct **€135**.

**Prerequisites:**

- This repository pushed to GitHub (so Issues and PRs are available)
- GitHub Copilot enabled; optionally the **Copilot coding agent** for autonomous fixes
- `gh` CLI signed in (optional, for the command-line steps)

**How to demo:**

1. **Reproduce the bug** — run the program and read the output:

   ```bash
   cd 14-issue-to-pr
   dotnet run

   ```

   Point out `Actual total (buggy): €140.00` vs `Expected: €135.00`.

2. **Create the Issue** — either paste `ISSUE.md` into a new issue in the GitHub UI,

   or use the CLI:

   ```bash
   gh issue create --title "Percentage coupons applied as a flat amount" \
     --body-file 14-issue-to-pr/ISSUE.md

   ```

3. **Let Copilot fix it** — choose one path:
   - **Coding agent (autonomous):** open the issue on GitHub → **Assignees → Copilot**.

     Copilot creates a branch, implements the fix, and opens a **draft Pull Request**.

   - **Copilot Chat (interactive):** in VS Code switch to **Agent** mode and ask:

     *"Fix the bug described in `14-issue-to-pr/ISSUE.md` (issue #N): make `ApplyDiscount`
     honor `Coupon.IsPercentage`, and add xUnit tests for both coupon types."*

4. **Review the Pull Request** — open the PR, read the diff, and run the tests:

   ```bash
   dotnet test

   ```

   Then ask Copilot to review it: comment **`@copilot review`** on the PR, or use the
   **Copilot code review** button. Iterate by replying to review comments.

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

- `TextAnalyzer.cs` — a small text-analysis class with only word/character counting implemented
- `TASK.md` — a ready-to-paste prompt describing the feature work to hand to the agent

**The task:** implement four missing methods (`CountSentences`, `AverageWordLength`,
`EstimateReadingTimeMinutes`, `TopWords`), add xUnit tests, refresh the demo output,
and write a short README — all done autonomously by the agent on GitHub.com.

**How this differs from Demo 14:**

| | Demo 14 — Issue to PR | **Demo 15 — Agent task on GitHub.com** |
| --- | --- | --- |
| Starting point | A filed GitHub **Issue** | A **natural-language prompt** in the browser |
| Where you work | VS Code or the issue page | **github.com/copilot/agents** (no editor) |
| Trigger | Assign the issue to Copilot | Start a task from the Agents page |
| Result | Branch + Pull Request | Branch + Pull Request |

**Prerequisites:**

- This repository pushed to GitHub
- **GitHub Copilot coding agent** enabled for your account/organization
- Write access to the repository (so the agent can push a branch and open a PR)

**How to demo:**

1. **Show the starting point** — run the program locally so the audience sees what's missing:

   ```bash
   cd 15-github-agent-task
   dotnet run

   ```

   Point out that sentences, average word length, reading time, and top words are *not* implemented.

2. **Open the Agents page** — go to **<https://github.com/copilot/agents>** in the browser

   and select this repository (and the `main` branch) as the target.

3. **Start the task** — paste the **Prompt** from `TASK.md` and launch it. Optionally pick a

   different base branch. The agent starts an asynchronous session running on GitHub.

4. **Watch the session** — open the live agent session and narrate what it does: reads the repo,

   plans the change, edits `TextAnalyzer.cs`, adds tests, and runs the build/tests. You can keep
   working elsewhere while it runs in the background.

5. **Review the Pull Request** — when the agent finishes it opens a **draft PR**. Open it, read the

   diff and the agent's summary, and check the CI run. Request changes by commenting
   **`@copilot ...`** or using **Copilot code review**; the agent pushes follow-up commits.

6. **Merge** — approve and **Squash and merge**, then delete the branch. Pull locally and re-run

   `dotnet run` / `dotnet test` to confirm the new metrics work.

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

1. **Build each example first** to ensure everything compiles in your environment
2. **Keep Copilot Chat open** in the sidebar so the audience can see the conversation
3. **Don't script the exact output** — Copilot is non-deterministic, so results may

   vary. That's part of the demo: show iteration and prompt refinement

4. **Connect back to the slides** — reference the 4 S's, Responsible AI principles,

   and Validation Workflow after each demo

5. **Let it fail sometimes** — if Copilot gives a wrong answer, that's a great

   teaching moment about the Validation Mindset

---

## Quick Start

Each folder is standalone — no `.csproj` is included intentionally so you can demo
Copilot scaffolding project creation. To build any example:

```bash
cd 07-explain-and-debug
dotnet new console -n LegacyParser --force
# Replace Program.cs with LegacyParser.cs content, or use top-level statements
dotnet run

```
