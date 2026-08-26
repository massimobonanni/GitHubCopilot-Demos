# Demo 17 - GitHub Copilot CLI

This starter demonstrates an end-to-end Python coding task from the terminal.
`TicketQueue` imports successfully, but two methods intentionally raise
`NotImplementedError`. The tests and `TASK.md` define the expected behavior.

## Prerequisites

- Python 3.12 or later
- pytest (`python -m pip install pytest`)
- PowerShell 6+ on Windows
- An active GitHub Copilot subscription with Copilot CLI enabled
- GitHub Copilot CLI installed:

  ```powershell
  winget install GitHub.Copilot
  ```

Before presenting, run this from the demo folder to confirm that the starter
fails only because the two methods are unfinished:

```powershell
python -m pytest -q
```

## Live demo

1. Start Copilot CLI in the demo folder:

   ```powershell
   cd Python\17-github-copilot-cli
   copilot
   ```

   Explain the trust prompt: Copilot can read, edit, and run files below the
   trusted directory. Choose session-only trust for a training machine.

2. If needed, authenticate with `/login`. Enter `?` to show available commands,
   then use `/model` to show that the model can be selected without leaving the
   terminal.

3. Demonstrate explicit file context without making changes:

   ```text
   Explain @TASK.md and @issue_desk.py. Identify the unfinished behavior and
   the tests that should prove it. Do not edit files yet.
   ```

4. Press **Shift+Tab** to enter plan mode, then prompt:

   ```text
   Plan the smallest implementation that satisfies @TASK.md and the existing
   pytest tests. Include the validation commands you will run.
   ```

   Review the proposed approach before any code changes. Press **Shift+Tab**
   again to return to the normal interactive mode.

5. Ask Copilot to complete the task:

   ```text
   Implement the approved plan. Keep the public API unchanged, add tests only
   for uncovered edge cases, and run pytest when finished.
   ```

   Pause at the first tool request. Explain one-time approval, approval for the
   rest of the session, and rejection with feedback. Approve only the specific
   file edits and Python commands needed for this task.

6. Run a shell command directly from the Copilot prompt:

   ```text
   !git diff -- issue_desk.py test_issue_desk.py
   ```

   The `!` prefix runs a shell command without a model request. Then ask:

   ```text
   Review this diff against @TASK.md. Report correctness issues and missing test
   cases before suggesting any additional edits.
   ```

7. Show session controls with `/context` and `/usage`, then exit. Resume the
   latest session later with:

   ```powershell
   copilot --continue
   ```

## Reset after the demo

From the repository root, restore the starter implementation and remove any
extra files Copilot created:

```powershell
git restore Python/17-github-copilot-cli
git clean -fd Python/17-github-copilot-cli
```

Review untracked files before using `git clean` on a shared working tree.

## Talking points

- Copilot CLI is the coding agent in a terminal, not the legacy `gh copilot`
  command for command suggestions.
- `@path` makes file context explicit; plan mode separates design from editing.
- Tool approval keeps file changes and command execution under human control.
- Agentic work must end with executable validation, not a plausible-looking diff.
- `!command`, session resume, context usage, and model selection keep the workflow
  terminal-native.
