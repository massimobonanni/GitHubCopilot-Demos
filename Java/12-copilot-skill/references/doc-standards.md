# Java API Documentation Standards (JavaDoc)

These standards define how all public APIs must be documented in this project.
The `document-api` skill reads this file to produce consistent output across all team members.

## Format

This project uses **JavaDoc comments** as defined in the
[Oracle JavaDoc Tool Guide](https://docs.oracle.com/javase/tools/windows/javadoc.html)
and [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html#s7-javadoc).

## Mandatory Sections by Member Type

| Member Type | Required Sections |
|---|---|
| Class | Summary, description (for complex classes) |
| Public Method / Function | Summary, `@param` (for each parameter), `@return` (if non-void), `@throws` (if exceptions raised) |
| Public Field | Summary |
| Property / Getter | Summary |

## Writing Style

- First line: concise **imperative** action phrase ending with a period ("Retrieve a customer." not "Retrieves")
- `@param` — list every parameter with type-consistent description; include valid range or constraints
- `@return` — describe the value and its meaning; for nullable return types, explicitly state the null case
- `@throws` — list every exception type that can propagate to the caller, with the condition that triggers it
- Use `{@link ClassName}`, `{@link #methodName()}`, and `{@code value}` for cross-references and inline code

## Example: Method

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

## Example: Class

```java
/**
 * Service for managing customer records.
 *
 * Provides CRUD operations with in-memory storage, including paginated search.
 * This service maintains an auto-incrementing ID counter for newly created customers.
 */
public class CustomerService {
    // implementation
}
```

## Example: Immutable Class

```java
/**
 * Represents a customer record with immutable data.
 *
 * Once created, a Customer's fields cannot be modified. All customer
 * information is accessed through getters and is guaranteed to remain
 * consistent throughout the object's lifetime.
 */
final class Customer {
    // implementation
}
```

## Type Information

- Method signatures define the contract; documentation should be consistent with types in the signature
- For nullable/optional parameters, state this clearly: "optional phone number"
- For collection parameters, describe what types of elements are expected: "a list of product IDs"
- For return types, use `{@link ClassName}` to link to related types when appropriate

## What NOT to Document

- Private methods (marked `private`) — unless they contain complex logic warranting explanation
- Trivial getters where the getter name fully describes the value returned
- Constructors if the class JavaDoc already covers the initialization contract
- Obvious implementation details (e.g., "increments a counter")

## Special Tags

- `@deprecated` — include reason and suggested replacement: `@deprecated use {@link NewClass#newMethod()} instead`
- `@since` — document when a method was added (e.g., `@since 1.0`)
- `@see` — link to related methods: `@see #delete(int)`
- `@author` — include author only if project standards require it

## Validation Checklist

- [ ] All public classes have JavaDoc
- [ ] All public methods have JavaDoc with `@param` and `@return`
- [ ] All `@throws` tags match exceptions actually thrown
- [ ] No spelling errors or typos in documentation
- [ ] Links use `{@link}` tags correctly
- [ ] Inline code uses `{@code}` tags
- [ ] First line of each comment ends with a period
