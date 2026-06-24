---
applyTo: "**/*.java"
---
# Java Coding Standards

This file is automatically read by GitHub Copilot and applied to all Java files.
Place this file at `.vscode/instructions/java-coding-standards.instructions.md` or at
`.github/copilot-instructions.md` (without the frontmatter) for repo-wide scope.

## Style
- Use **explicit type declarations** on all variables, parameters, and return types
- Follow Java conventions: `camelCase` for methods/variables, `PascalCase` for classes/interfaces, `UPPER_SNAKE_CASE` for constants
- Maximum line length: **120 characters**
- Prefer **String.format()** or **StringBuilder** for complex string construction
- Use **records** for immutable data structures; avoid plain `Object` fields for structured data
- Declare constants as `private static final` with descriptive names

## Error Handling
- Catch **specific exception types** — never bare `catch (Exception e)` unless absolutely necessary
- Always log exceptions with full context: use `LOGGER.log(Level.SEVERE, message, exception)` inside a `catch` block
- Return a **typed result object** (e.g., `ProcessingResult`) rather than throwing for expected/business-rule failures
- Never silently swallow errors; always log or propagate

## Methods & Classes
- Every public method and class must have a **JavaDoc comment**
- Use **early returns** to avoid deep nesting (max 2 levels of indentation preferred)
- Keep methods focused on a **single responsibility**
- Avoid magic numbers — use named constants (`UPPER_SNAKE_CASE`)
- Prefer **immutable objects** for value objects; use final fields and private constructors

## Logging
- Use the class-level pattern: `private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName())`
- Log at the appropriate level:
  - `FINE`/`FINER` — internal state and flow
  - `INFO` — significant business events (order confirmed, user logged in)
  - `WARNING` — degraded state, recoverable issues
  - `SEVERE` — failures that require attention
- Always include relevant context (IDs, values, timestamps) in log messages
- Use `LOGGER.log(Level.SEVERE, message, exception)` to include exception stack traces

## Dependencies
- Use constructor injection for external dependencies (databases, email clients, etc.)
- Declare dependencies as instance fields with descriptive names
- Avoid static singletons; prefer dependency injection

## Null Safety
- Always check for null on method parameters: use early returns or throw `IllegalArgumentException`
- Use `Objects.requireNonNull()` for critical dependencies
- Document null behavior in JavaDoc with `@throws IllegalArgumentException` if null is not allowed
