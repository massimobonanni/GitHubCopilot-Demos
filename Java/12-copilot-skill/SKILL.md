---
name: document-api
description: 'Generate standardized JavaDoc comments for Java classes and methods following team documentation standards. Use when asked to document a module, add JavaDoc, generate API documentation, or create documentation following team standards.'
---

# document-api

Generate comprehensive, standardized JavaDoc comments for Java public APIs.

## When to Use This Skill

Use this skill when:
- A user asks to document a class, file, or module
- You need to add JavaDoc comments to public methods or classes
- You want consistent documentation across the entire team
- Keywords: document, JavaDoc, add docs, document this, API documentation, add comments

## Documentation Standards

Follow the team standards defined in [references/doc-standards.md](references/doc-standards.md).

## Workflow

### Step 1: Analyze

Scan all public classes, methods, and fields (those not prefixed with `_` and not marked `private`). Identify those missing JavaDoc or with incomplete documentation.

### Step 2: Generate Documentation

**Class:**
```java
/**
 * Single-sentence summary of what the class represents.
 *
 * Extended description if needed. Describe the purpose, usage, and key
 * responsibilities of the class.
 */
```

**Method / Function:**
```java
/**
 * Action phrase describing what the method does.
 *
 * Extended description if needed, including context and usage patterns.
 *
 * @param paramName description of the parameter. Include valid range or constraints.
 * @param optionalParam description. Defaults to X if not provided.
 * @return description of the returned value and its meaning.
 *         For nullable returns: "the Customer, or null if not found."
 * @throws IllegalArgumentException when paramName fails validation
 * @throws NoSuchElementException when the resource does not exist
 */
```

**Field:**
```java
/**
 * Description of what the field represents.
 */
```

**Property / Getter:**
```java
/**
 * Get the description of what this property represents.
 *
 * @return the value
 */
```

### Step 3: Verify

- Every public class, method, and field has a JavaDoc comment
- `@param` tags are present for every non-trivial parameter (exclude `this` and `self`)
- `@return` tag is present for non-void return types
- `@throws` tags list every checked exception that can propagate to the caller
- Type information in Javadoc is consistent with method signature
- Links use `{@link ClassName}` or `{@link #methodName(Type)}` format for cross-references

## Rules

- First line (summary) must be a concise, imperative action phrase ending with a period
- Use **imperative mood**: "Retrieve a customer" not "Retrieves a customer"
- Type information should match the method signature exactly
- Do **not** add JavaDoc to private methods unless they contain complex logic
- For deprecated items, include: `@deprecated use {@link NewClassName#newMethod()} instead`
- Property/getter docstrings: concise, imperative description
- Use `{@code ...}` for inline code or literals (e.g., `{@code null}`)
- For immutable/final classes, mention this in the class JavaDoc

## Good Example

```java
/**
 * Retrieve a customer by their unique identifier.
 *
 * @param customerId the unique ID of the customer. Must be a positive integer.
 * @return the Customer with the given ID, or null if not found
 * @throws IllegalArgumentException if customerId is not a positive integer
 */
public Customer getById(int customerId) {
    // implementation
}
```

## References

- [JavaDoc Tool Documentation](https://docs.oracle.com/javase/tools/windows/javadoc.html)
- [Google Java Style Guide - Documentation](https://google.github.io/styleguide/javaguide.html#s7-javadoc)
