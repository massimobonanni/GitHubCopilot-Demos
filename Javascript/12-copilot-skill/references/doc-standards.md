# JavaScript API Documentation Standards (JSDoc)

These standards define how all public APIs must be documented in this project.
The `document-api` skill reads this file to produce consistent output across all team members.

## Mandatory Tags by Member Type

| Member Type | Required Tags |
|---|---|
| Module (file) | `@module`, `@description` at the top |
| Class | Description block, `@example` for non-trivial usage |
| Public Method / Function | `@param` (each), `@returns` (non-void), `@throws` (each error) |
| Getter Property | `@type` |

## Writing Style

- Use **imperative mood**: "Retrieve a customer" not "Retrieves a customer"
- `@returns` must describe the semantic value, not just the type:
  - ✅ `{Promise<Customer|null>} The customer with the given ID, or null if not found`
  - ❌ `{Promise<Customer|null>} The result`
- `@param` must state the type and description — constraints included when relevant

## Type Syntax

- Always use specific types: `{number}`, `{string}`, `{boolean}`
- Nullable values: `{string|null}` or `{?string}`
- Arrays: `{Array<Customer>}` (not `{Customer[]}`)
- Async return: `{Promise<Customer>}`
- Optional parameters: `[paramName]` or `{number} [page=1]`

## Error Documentation

Every error condition that propagates to callers must be listed:

```javascript
/**
 * @throws {RangeError} When customerId is not a positive integer.
 * @throws {Error} When no customer with the given ID exists.
 */
```

## Module Header Template

Every file must begin with:

```javascript
/**
 * @module customerService
 * @description Provides CRUD operations for managing customer records.
 */
```

## Usage Examples

Include `@example` for any non-trivial API:

```javascript
/**
 * @example
 * const service = new CustomerService();
 * const customer = await service.create({ name: 'Alice', email: 'alice@example.com' });
 * console.log(customer.id);
 */
```

## What NOT to Document

- Private class fields (`#field`)
- Unexported internal helper functions
- Simple one-liner getters where the name is self-explanatory (use `@type` at minimum)
