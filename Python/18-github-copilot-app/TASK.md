# Sprint planning dashboard

Complete the two unfinished services in `sprint_planner.py` and
`risk_analyzer.py` without changing their public APIs.

## Workstream A: `SprintPlanner.build_plan`

- Reject a negative `capacity` with `ValueError`.
- Consider only items whose status is `READY` and whose estimate is positive.
- Order candidates by priority: `CRITICAL`, `HIGH`, `NORMAL`, then `LOW`.
- Within the same priority, order by `created_at` oldest first, then by
  `item_id` using Python's case-sensitive string ordering.
- Add an item when its estimate fits in the remaining capacity. If an item does
  not fit, continue checking later candidates.
- Return the selected items and their total estimate.
- Do not mutate the collection supplied to the constructor.

## Workstream B: `RiskAnalyzer.find_at_risk`

- Reject a negative `warning_window_days` with `ValueError`.
- Exclude completed items and items without a due date.
- Mark an item `OVERDUE` when its due date is earlier than `as_of`.
- Mark an item `DUE_SOON` when its due date is from `as_of` through
  `as_of + timedelta(days=warning_window_days)`, inclusive.
- Exclude items due after the warning window.
- Sort `OVERDUE` before `DUE_SOON`, then by due date ascending, then by
  `item_id` using Python's case-sensitive string ordering.
- Do not mutate the collection supplied to the constructor.

## Acceptance criteria

- [ ] Both workstreams meet all filtering, ordering, and boundary requirements.
- [ ] Existing tests pass with `python -m pytest -q`.
- [ ] Tests are added for important uncovered edge cases.
- [ ] Public APIs remain unchanged.
- [ ] No generated cache or test output is committed.
