---
applyTo: "**/*.js"
---
# JavaScript Coding Standards

This file is automatically read by GitHub Copilot and applied to all JavaScript files.
Place this file at `.vscode/instructions/coding-standards.instructions.md` or at
`.github/copilot-instructions.md` (without the frontmatter) for repo-wide scope.

## Style
- Use **ES2022+** features: `async`/`await`, optional chaining (`?.`), nullish coalescing (`??`), logical assignment (`??=`, `||=`)
- Use `const` by default; `let` only when reassignment is necessary; **never** `var`
- `camelCase` for functions and variables, `PascalCase` for classes, `UPPER_SNAKE_CASE` for module-level constants
- Maximum line length: **100 characters**
- Use **JSDoc** comments on all exported functions and classes

## Error Handling
- Always wrap `async` function bodies in `try/catch` — never leave unhandled promise rejections
- Log the error with context before re-throwing or returning an error result
- Prefer **specific error types** (`TypeError`, `RangeError`, custom subclasses) over generic `new Error()`
- Never silently swallow errors

## Functions
- Every exported function and class must have a **JSDoc comment** with `@param` and `@returns` tags
- Use **early returns** to avoid deep nesting (max 2 levels preferred)
- Keep functions focused on a **single responsibility**
- Avoid magic numbers — use named `const` constants at module scope

## Async
- All I/O operations must use `async`/`await` — avoid `.then().catch()` chains
- Use `Promise.all()` (or `Promise.allSettled()`) for parallel independent operations
- Never mix callbacks with promises in the same flow

## Logging
- Use a structured logger (e.g., `winston`, `pino`) — **never** `console.log` in production code
- Log at the appropriate level: `debug`, `info`, `warn`, `error`
- Always include relevant context (IDs, values) in log messages as structured fields
