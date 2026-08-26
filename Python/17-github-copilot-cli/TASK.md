# Ticket triage feature

Complete the two unfinished methods in `issue_desk.py` without changing the
public API.

## Requirements

### `order_for_triage`

- Return every ticket exactly once.
- Sort by priority: `CRITICAL`, `HIGH`, `NORMAL`, then `LOW`.
- Within the same priority, sort oldest `created_at` first.
- Break any remaining tie by `ticket_id`, using case-sensitive string ordering.
- Do not mutate the collection supplied to the constructor.

### `get_owner_workload`

- Group owner names case-insensitively.
- Treat a null, empty, or whitespace-only owner as `Unassigned`.
- Preserve the first non-blank spelling encountered for each owner.
- Return the ticket count for each owner.
- Sort by count descending, then owner name alphabetically (case-insensitive).

## Acceptance criteria

- [ ] Both methods meet all ordering and grouping requirements.
- [ ] Existing tests pass with `python -m pytest -q`.
- [ ] Tests are added for any important edge case not already covered.
- [ ] No generated cache or test output is committed.
