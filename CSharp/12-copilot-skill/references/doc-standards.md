# C# API Documentation Standards

These standards define how all public APIs must be documented in this project.
The `document-api` skill reads this file to produce consistent output across all team members.

## Mandatory Tags by Member Type

| Member Type | Required Tags |
|---|---|
| Class / Interface / Record | `<summary>`, optionally `<remarks>` |
| Public Method | `<summary>`, `<param>` (each parameter), `<returns>` (non-void), `<exception>` (each thrown) |
| Public Property | `<summary>` |
| Public Constructor | `<summary>`, `<param>` (each parameter), `<exception>` (if throws) |

## Writing Style

- Use **third person** present tense: "Retrieves" not "Retrieve" or "Gets the thing"
- For `Task`-returning methods, start the summary with "Asynchronously": "Asynchronously retrieves…"
- `<returns>` must describe the semantic value, not just the type:
  - ✅ "The matched customer, or `null` if not found"
  - ❌ "Returns a `Task<Customer?>`"
- `<param>` must state purpose and valid constraints when relevant

## Exception Documentation

Every exception that propagates to the caller must be listed:

```xml
/// <exception cref="ArgumentOutOfRangeException">
/// Thrown when <paramref name="pageSize"/> is less than 1 or greater than 100.
/// </exception>
```

## Cross-References

Use `<see cref="..."/>` to link to related types and members:

```xml
/// <returns>A <see cref="Customer"/> record, or <c>null</c> if not found.</returns>
```

## Optional: Usage Examples

For non-trivial or frequently misused APIs, include an example:

```xml
/// <example>
/// <code>
/// var customer = await service.GetByIdAsync(42, CancellationToken.None);
/// Console.WriteLine(customer?.Name);
/// </code>
/// </example>
```

## What NOT to Document

- `private` members (unless they contain unusually complex logic warranting inline `//` comments)
- `internal` members
- Overriding members that add no new behaviour (`override ToString()` that just calls base)
