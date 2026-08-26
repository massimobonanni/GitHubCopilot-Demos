"""Delivery-risk workstream for the Copilot desktop app demo."""

from datetime import datetime

from work_items import AtRiskItem, WorkItem


class RiskAnalyzer:
    """Finds incomplete work that is overdue or due soon."""

    def __init__(self, items: list[WorkItem]) -> None:
        if items is None:
            raise TypeError("items cannot be None")
        self._items = tuple(items)

    def find_at_risk(
        self,
        as_of: datetime,
        warning_window_days: int,
    ) -> list[AtRiskItem]:
        """Return incomplete work due on or before the warning boundary."""
        # TODO (Copilot app, workstream B): implement the behavior in TASK.md.
        raise NotImplementedError