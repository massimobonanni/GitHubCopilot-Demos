# Sprint planning dashboard

Complete the two unfinished services under `src/DeliveryDashboard` without
changing their public APIs.

## Workstream A: `SprintPlanner.BuildPlan`

- Reject a negative `capacity` with `ArgumentOutOfRangeException`.
- Consider only items whose status is `Ready` and whose estimate is positive.
- Order candidates by priority: `Critical`, `High`, `Normal`, then `Low`.
- Within the same priority, order by `CreatedAt` oldest first, then by `Id`
  using ordinal string comparison.
- Add an item when its estimate fits in the remaining capacity. If an item does
  not fit, continue checking later candidates.
- Return the selected items and their total estimate.
- Do not mutate the collection supplied to the constructor.

## Workstream B: `RiskAnalyzer.FindAtRisk`

- Reject a negative `warningWindowDays` with `ArgumentOutOfRangeException`.
- Exclude completed items and items without a due date.
- Mark an item `Overdue` when its due date is earlier than `asOf`.
- Mark an item `DueSoon` when its due date is from `asOf` through
  `asOf.AddDays(warningWindowDays)`, inclusive.
- Exclude items due after the warning window.
- Sort `Overdue` before `DueSoon`, then by due date ascending, then by `Id`
  using ordinal string comparison.
- Do not mutate the collection supplied to the constructor.

## Acceptance criteria

- [ ] Both workstreams meet all filtering, ordering, and boundary requirements.
- [ ] Existing tests pass with
  `dotnet test tests/DeliveryDashboard.Tests/DeliveryDashboard.Tests.csproj`.
- [ ] Tests are added for important uncovered edge cases.
- [ ] Public APIs remain unchanged.
- [ ] No generated build output is committed.
