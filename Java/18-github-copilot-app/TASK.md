# Sprint planning dashboard

Complete the two unfinished services under `src/main` without changing their
public APIs.

## Workstream A: `SprintPlanner.buildPlan`

- Reject a negative `capacity` with `IllegalArgumentException`.
- Consider only items whose status is `READY` and whose estimate is positive.
- Order candidates by priority: `CRITICAL`, `HIGH`, `NORMAL`, then `LOW`.
- Within the same priority, order by `createdAt` oldest first, then by `id`
  using `String.compareTo` ordering.
- Add an item when its estimate fits in the remaining capacity. If an item does
  not fit, continue checking later candidates.
- Return the selected items and their total estimate.
- Do not mutate the list supplied to the constructor.

## Workstream B: `RiskAnalyzer.findAtRisk`

- Reject a negative `warningWindowDays` with `IllegalArgumentException`.
- Exclude completed items and items without a due date.
- Mark an item `OVERDUE` when its due date is earlier than `asOf`.
- Mark an item `DUE_SOON` when its due date is from `asOf` through
  `asOf.plus(warningWindowDays, ChronoUnit.DAYS)`, inclusive.
- Exclude items due after the warning window.
- Sort `OVERDUE` before `DUE_SOON`, then by due date ascending, then by `id`
  using `String.compareTo` ordering.
- Do not mutate the list supplied to the constructor.

## Acceptance criteria

- [ ] Both workstreams meet all filtering, ordering, and boundary requirements.
- [ ] Existing tests pass with `mvn test`.
- [ ] Tests are added for important uncovered edge cases.
- [ ] Public APIs remain unchanged.
- [ ] No generated build output is committed.
