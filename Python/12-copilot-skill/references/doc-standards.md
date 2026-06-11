# Python API Documentation Standards (Google-style Docstrings)

These standards define how all public APIs must be documented in this project.
The `document-api` skill reads this file to produce consistent output across all team members.

## Format

This project uses **Google-style docstrings** as defined in the
[Google Python Style Guide](https://google.github.io/styleguide/pyguide.html#38-comments-and-docstrings).

## Mandatory Sections by Member Type

| Member Type | Required Sections |
|---|---|
| Module | Single-line or multi-line summary |
| Class | Summary, `Attributes:` (for public instance attributes) |
| Public Method / Function | Summary, `Args:`, `Returns:` (non-None), `Raises:` (if exceptions raised) |
| Property | Single-line description |

## Writing Style

- First line: concise **imperative** action phrase ending with a period ("Retrieve a customer." not "Retrieves")
- `Args:` — list every parameter except `self` and `cls`, with type-consistent description
- `Returns:` — describe the value and its meaning; for `Optional` types, explicitly state the `None` case
- `Raises:` — list every exception type that can propagate to the caller, with the condition

## Example: Method

```python
def get_by_id(self, customer_id: int) -> Optional[Customer]:
    """Retrieve a customer by their unique identifier.

    Args:
        customer_id: The unique ID of the customer. Must be a positive integer.

    Returns:
        The Customer with the given ID, or None if not found.

    Raises:
        ValueError: If customer_id is not a positive integer.
    """
```

## Example: Class

```python
class CustomerService:
    """Service for managing customer records.

    Provides CRUD operations with in-memory storage, including paginated search.

    Attributes:
        count: The number of customers currently stored.
    """
```

## Example: Module Header

```python
"""Customer management service module.

Provides the CustomerService class for creating, retrieving, updating,
and deleting customer records.
"""
```

## Type Hints

- Type hints in function signatures are **mandatory** and take precedence over types mentioned in docstrings
- Use `Optional[X]` (or `X | None` in Python 3.10+) for nullable values
- Use `list[X]` not `List[X]` for Python 3.9+

## What NOT to Document

- Private methods (prefixed with `_`) — unless they contain complex logic warranting explanation
- Simple properties where the name is fully self-explanatory
- `__init__` if the class docstring already covers all constructor parameters
