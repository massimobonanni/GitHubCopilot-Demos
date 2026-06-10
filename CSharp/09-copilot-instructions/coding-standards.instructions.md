---
applyTo: "**/*.cs"
---
# C# Coding Standards

This file is automatically read by GitHub Copilot and applied to all C# files.
Place this file at `.vscode/instructions/coding-standards.instructions.md` or at
`.github/copilot-instructions.md` (without the frontmatter) for repo-wide scope.

## Style
- Use **C# 12+** features: primary constructors, collection expressions, `required` properties
- Follow Microsoft conventions: `PascalCase` for types/methods/properties, `camelCase` for locals/parameters, `_camelCase` for private fields
- Use `UPPER_SNAKE_CASE` for constants
- Maximum line length: **120 characters**
- Use `var` when the type is obvious from the right-hand side
- Use `record` for immutable data objects; `record struct` for small value types

## Error Handling
- Catch **specific exception types** — never bare `catch (Exception)` unless re-throwing
- Use `ILogger<T>` for all logging — **never** `Console.WriteLine` in production code
- Return **result objects** (e.g., `ProcessingResult`) for expected failures; reserve exceptions for unexpected/unrecoverable errors
- Never silently swallow errors

## Methods
- Every public method, property, and class must have **XML documentation** (`/// <summary>`)
- Use `async Task` / `async Task<T>` for all I/O-bound operations
- Accept `CancellationToken ct = default` as the **last parameter** on all async public methods
- Avoid magic numbers — use `const` or `static readonly` named constants
- Use **early returns** to reduce nesting

## Dependency Injection
- All external dependencies (database, email, HTTP clients) must be **injected via constructor**
- Depend on **interfaces**, not concrete classes, to support testability and mocking

## Nullability
- Enable **nullable reference types** (`#nullable enable` or project-wide in `.csproj`)
- Annotate all parameters and return types appropriately (`string?`, `T?`)
- Use the null-coalescing operator (`??`, `??=`) and null-conditional operator (`?.`) where appropriate
