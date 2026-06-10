---
applyTo: "**/*.py"
---
# Python Coding Standards

This file is automatically read by GitHub Copilot and applied to all Python files.
Place this file at `.vscode/instructions/coding-standards.instructions.md` or at
`.github/copilot-instructions.md` (without the frontmatter) for repo-wide scope.

## Style
- Use **type hints** on all function signatures and class attributes
- Follow PEP 8: `snake_case` for functions/variables, `PascalCase` for classes, `UPPER_SNAKE_CASE` for constants
- Maximum line length: **100 characters**
- Prefer **f-strings** over `.format()` or `%` formatting
- Use `@dataclass` for data structures; avoid plain `dict` for structured data

## Error Handling
- Catch **specific exception types** — never bare `except:` or `except Exception:`
- Always log exceptions with context: use `logger.exception()` inside an `except` block
- Return a **typed result object** rather than raising for expected/business-rule failures
- Never silently swallow errors

## Functions & Methods
- Every public function and method must have a **docstring**
- Use **early returns** to avoid deep nesting (max 2 levels of indentation preferred)
- Keep functions focused on a **single responsibility**
- Avoid magic numbers — use named constants (`UPPER_SNAKE_CASE`)
- Default to **immutable** data (tuples, frozen dataclasses) for value objects

## Async
- Use `async`/`await` for all I/O-bound operations
- Do **not** use `time.sleep()` — use `asyncio.sleep()` instead
- Prefer `asyncio.gather()` for concurrent independent operations

## Logging
- Use the module-level pattern: `logger = logging.getLogger(__name__)`
- Log at the appropriate level:
  - `DEBUG` — internal state and flow
  - `INFO` — significant business events (order confirmed, user logged in)
  - `WARNING` — degraded state, recoverable issues
  - `ERROR` — failures that require attention
- Always include relevant context (IDs, values) in log messages
