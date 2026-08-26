"""Sprint-planning workstream for the Copilot desktop app demo."""

from work_items import SprintPlan, WorkItem


class SprintPlanner:
    """Builds a priority-ordered plan within a sprint capacity."""

    def __init__(self, items: list[WorkItem]) -> None:
        if items is None:
            raise TypeError("items cannot be None")
        self._items = tuple(items)

    def build_plan(self, capacity: int) -> SprintPlan:
        """Return ready work selected within the available capacity."""
        # TODO (Copilot app, workstream A): implement the behavior in TASK.md.
        raise NotImplementedError