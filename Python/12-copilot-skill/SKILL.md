---
name: document-api
description: 'Generate standardized Google-style docstrings for Python classes and functions following team documentation standards. Use when asked to document a module, add docstrings, generate API documentation, or create documentation following team standards.'
---

# document-api

Generate comprehensive, standardized Google-style docstrings for Python public APIs.

## When to Use This Skill

Use this skill when:
- A user asks to document a class, file, or module
- You need to add Google-style docstrings to public functions or classes
- You want consistent documentation across the entire team
- Keywords: document, docstrings, add docs, document this, API documentation, add comments

## Documentation Standards

Follow the team standards defined in [references/doc-standards.md](references/doc-standards.md).

## Workflow

### Step 1: Analyze

Scan all public classes, methods, and functions (those not prefixed with `_`). Identify those missing docstrings or with incomplete documentation.

### Step 2: Generate Documentation

**Module (top of file):**
```
"""Brief description of what this module provides.

Extended description if needed (optional).
"""
```

**Class:**
```
"""Single-sentence summary of what the class represents.

Extended description if needed.

Attributes:
    attribute_name: Description of the public attribute.
"""
```

**Method / Function:**
```
"""Action phrase describing what the function does.

Args:
    param_name: Description. Include valid range or constraints.
    optional_param: Description. Defaults to X.

Returns:
    Description of the returned value and its meaning.
    Include None returns: "The Customer, or None if not found."

Raises:
    ValueError: When param_name fails validation.
    KeyError: When the resource does not exist.
"""
```

**Property:**
```
"""Description of what the property represents."""
```

### Step 3: Verify

- Every public function and method has a docstring
- `Args:` section includes every parameter (except `self` and `cls`)
- `Returns:` section is present for non-`None` return types
- `Raises:` section lists every exception that can propagate to the caller
- Type hints in the signature are consistent with descriptions in the docstring

## Rules

- First line must be a concise action phrase ending with a period
- Use **imperative mood**: "Retrieve a customer" not "Retrieves a customer"
- Type hints in function signatures are **mandatory** and take precedence over types in docstrings
- Do **not** add docstrings to private methods (those prefixed with `_`) unless they contain complex logic
- For async methods, begin: "Asynchronously retrieve…"
- Property docstrings: one concise line describing what the property represents

## Good Example

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

## What to Avoid

- ❌ `"""Gets the thing."""` — too vague
- ❌ Missing `Raises:` section when exceptions can propagate to callers
- ❌ Type annotations in the docstring that contradict the function signature
- ❌ Adding docstrings to private helpers (`_method`) that contain no complex logic
