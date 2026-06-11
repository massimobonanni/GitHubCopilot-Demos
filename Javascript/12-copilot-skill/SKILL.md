---
name: document-api
description: 'Generate standardized JSDoc API documentation for JavaScript classes and functions following team documentation standards. Use when asked to document a module, add JSDoc comments, generate API documentation, or create documentation following team standards.'
---

# document-api

Generate comprehensive, standardized JSDoc documentation for JavaScript public APIs.

## When to Use This Skill

Use this skill when:
- A user asks to document a class, file, or module
- You need to add `/** ... */` JSDoc comments
- You want consistent documentation across the entire team
- Keywords: document, JSDoc, add docs, document this, API documentation, add comments

## Documentation Standards

Follow the team standards defined in [references/doc-standards.md](references/doc-standards.md).

## Workflow

### Step 1: Analyze

Scan all exported classes, exported functions, and significant public class methods. Identify those missing JSDoc or with incomplete documentation.

### Step 2: Generate Documentation

**Module header (top of every file):**
```
/**
 * @module moduleName
 * @description [Single-sentence description of what this module provides.]
 */
```

**Class:**
```
/**
 * [Single-sentence description of what the class represents.]
 *
 * @example
 * const service = new CustomerService();
 * const customer = await service.create({ name: 'Alice', email: 'alice@example.com' });
 */
```

**Method / Function:**
```
/**
 * [Action phrase describing what the function does.]
 *
 * @param {type} paramName - [Description. Include valid values or constraints.]
 * @returns {Promise<type>} [What is returned and its meaning. Include null returns.]
 * @throws {ErrorType} [When this error is thrown and why.]
 */
```

**Getter property:**
```
/**
 * Gets [what the property represents].
 *
 * @type {type}
 */
```

### Step 3: Verify

- Every exported function or public method parameter has a `@param` tag with a type
- Every function has a `@returns` tag (unless the return type is `void`/`undefined`)
- All thrown errors are documented with `@throws`
- Types use `{TypeName}` syntax (e.g., `{number}`, `{string|null}`, `{Promise<Customer>}`, `{Array<Customer>}`)

## Rules

- Use **imperative mood**: "Retrieve a customer" not "Retrieves a customer"
- `@returns` must describe the semantic value: "The matched customer, or null if not found"
- For async functions, use `{Promise<type>}` as the return type
- Include a `@example` block for any non-trivial API
- Do **not** document private class fields (prefixed with `#`) or internal helpers

## Good Example

```javascript
/**
 * Retrieve a customer by their unique identifier.
 *
 * @param {number} customerId - The unique ID of the customer. Must be a positive integer.
 * @returns {Promise<Customer|null>} The customer with the given ID, or null if not found.
 * @throws {RangeError} When customerId is not a positive integer.
 * @example
 * const customer = await service.getById(42);
 * console.log(customer?.name);
 */
```

## What to Avoid

- ❌ `@param {*} x` — always use a specific type
- ❌ Missing `@throws` for documented error conditions
- ❌ `@returns {Promise}` — describe the resolved value: `{Promise<Customer|null>}`
- ❌ Documenting private `#fields` or unexported internal functions
