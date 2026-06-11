---
name: document-api
description: 'Generate standardized XML API documentation for C# public classes and methods following team documentation standards. Use when asked to document a module, add XML docs, generate API documentation, or create documentation following team standards.'
---

# document-api

Generate comprehensive, standardized XML documentation for C# public APIs.

## When to Use This Skill

Use this skill when:
- A user asks to document a class, file, or module
- You need to add `/// <summary>` XML doc comments
- You want consistent documentation across the entire team
- Keywords: document, XML docs, add docs, document this, API documentation, add comments

## Documentation Standards

Follow the team standards defined in [references/doc-standards.md](references/doc-standards.md).

## Workflow

### Step 1: Analyze

Scan all `public` and `protected` members in the file. Identify those missing XML documentation or with incomplete docs.

### Step 2: Generate Documentation

For each undocumented public member, generate:

**Classes and Interfaces:**
```
/// <summary>
/// [Single-sentence description of what the class represents or does.]
/// </summary>
/// <remarks>
/// [Optional: additional context, usage notes, or thread-safety considerations.]
/// </remarks>
```

**Methods:**
```
/// <summary>
/// [Verb phrase describing what the method does, e.g., "Retrieves a customer by ID".]
/// </summary>
/// <param name="[paramName]">[Description. Include valid range or constraints.]</param>
/// <returns>[What is returned and what it represents. Include null return meaning.]</returns>
/// <exception cref="[ExceptionType]">[When this exception is thrown.]</exception>
```

**Properties:**
```
/// <summary>
/// Gets [or "Gets or sets"] [what the property represents].
/// </summary>
```

### Step 3: Verify

- Every `public` method parameter has a matching `<param>` tag
- Every `public` non-void method has a `<returns>` tag
- All thrown exceptions are documented with `<exception cref="...">`
- `<param>` names exactly match the actual parameter names in the signature

## Rules

- Write in **third person** ("Retrieves" not "Retrieve")
- For async methods, begin the summary with "Asynchronously": "Asynchronously retrieves…"
- Omit `<remarks>` if it adds no value beyond the `<summary>`
- Do **not** document `private` or `internal` members
- Use `<see cref="..."/>` to cross-reference related types
- Use `<c>null</c>` (not the word "null") when referring to a null value
- Use `<paramref name="..."/>` when referring to a parameter inside a tag

## Good Example

```csharp
/// <summary>
/// Asynchronously retrieves a customer by their unique identifier.
/// </summary>
/// <param name="customerId">The unique identifier of the customer. Must be a positive integer.</param>
/// <param name="cancellationToken">A token to cancel the asynchronous operation.</param>
/// <returns>
/// The <see cref="Customer"/> with the specified identifier,
/// or <c>null</c> if no customer with that ID exists.
/// </returns>
/// <exception cref="ArgumentOutOfRangeException">
/// Thrown when <paramref name="customerId"/> is less than or equal to zero.
/// </exception>
public async Task<Customer?> GetByIdAsync(int customerId, CancellationToken cancellationToken = default)
```

## What to Avoid

- ❌ `/// <summary>Gets the thing.</summary>` — too vague
- ❌ Missing `<param>` tags for any public method parameter
- ❌ `/// <returns>Returns a Task.</returns>` — describe the wrapped value, not the container type
- ❌ Documenting private or internal members
