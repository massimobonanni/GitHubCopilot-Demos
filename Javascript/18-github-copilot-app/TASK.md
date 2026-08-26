# Sprint planning dashboard

Complete the two unfinished services in `sprintPlanner.js` and
`riskAnalyzer.js` without changing their public APIs.

## Workstream A: `SprintPlanner.buildPlan`

- Reject a negative `capacity` with `RangeError`.
- Consider only items whose status is `READY` and whose estimate is positive.
- Order candidates by priority: `CRITICAL`, `HIGH`, `NORMAL`, then `LOW`.
- Within the same priority, order by `createdAt` oldest first, then by `id`
  using case-sensitive UTF-16 code-unit ordering.
- Add an item when its estimate fits in the remaining capacity. If an item does
  not fit, continue checking later candidates.
- Return the selected items and their total estimate.
- Do not mutate the array supplied to the constructor.

## Workstream B: `RiskAnalyzer.findAtRisk`

- Reject a negative `warningWindowDays` with `RangeError`.
- Exclude completed items and items without a due date.
- Mark an item `OVERDUE` when its due date is earlier than `asOf`.
- Mark an item `DUE_SOON` when its due date is from `asOf` through
  `warningWindowDays` later, inclusive.
- Exclude items due after the warning window.
- Sort `OVERDUE` before `DUE_SOON`, then by due date ascending, then by `id`
  using case-sensitive UTF-16 code-unit ordering.
- Do not mutate the array supplied to the constructor.

## Acceptance criteria

- [ ] Both workstreams meet all filtering, ordering, and boundary requirements.
- [ ] Existing tests pass with `npm test`.
- [ ] Tests are added for important uncovered edge cases.
- [ ] Public APIs remain unchanged.
- [ ] No generated test output is committed.
