# Ticket triage feature

Complete the two unfinished methods in
`src/IssueDesk/TicketQueue.cs` without changing the public API.

## Requirements

### `OrderForTriage`

- Return every ticket exactly once.
- Sort by priority: `Critical`, `High`, `Normal`, then `Low`.
- Within the same priority, sort oldest `CreatedAt` first.
- Break any remaining tie by `Id`, using ordinal string comparison.
- Do not mutate the collection supplied to the constructor.

### `GetOwnerWorkload`

- Group owner names case-insensitively.
- Treat a null, empty, or whitespace-only owner as `Unassigned`.
- Preserve the first non-blank spelling encountered for each owner.
- Return the ticket count for each owner.
- Sort by count descending, then owner name alphabetically (case-insensitive).

## Acceptance criteria

- [ ] Both methods meet all ordering and grouping requirements.
- [ ] Existing tests pass with `dotnet test tests/IssueDesk.Tests/IssueDesk.Tests.csproj`.
- [ ] Tests are added for any important edge case not already covered.
- [ ] No generated build output is committed.
