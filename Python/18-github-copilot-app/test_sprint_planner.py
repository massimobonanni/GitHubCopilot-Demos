"""Acceptance criteria for the sprint-planning workstream."""

from datetime import datetime, timedelta, timezone

import pytest

from sprint_planner import SprintPlanner
from work_items import (
    WorkItem,
    WorkItemPriority,
    WorkItemStatus,
)


START = datetime(2026, 8, 1, 9, 0, tzinfo=timezone.utc)


def make_item(
    item_id: str,
    priority: WorkItemPriority,
    status: WorkItemStatus,
    estimate: int,
    age_in_hours: int,
) -> WorkItem:
    return WorkItem(
        item_id,
        f"Item {item_id}",
        priority,
        status,
        estimate,
        START + timedelta(hours=age_in_hours),
        None,
    )


def test_build_plan_filters_orders_and_uses_available_capacity() -> None:
    items = [
        make_item("LOW-1", WorkItemPriority.LOW, WorkItemStatus.READY, 2, 0),
        make_item("CRIT-1", WorkItemPriority.CRITICAL, WorkItemStatus.READY, 5, 3),
        make_item("HIGH-2", WorkItemPriority.HIGH, WorkItemStatus.READY, 4, 2),
        make_item("HIGH-1", WorkItemPriority.HIGH, WorkItemStatus.READY, 3, 1),
        make_item("DONE-1", WorkItemPriority.CRITICAL, WorkItemStatus.DONE, 1, 0),
        make_item("ZERO-1", WorkItemPriority.CRITICAL, WorkItemStatus.READY, 0, 0),
    ]

    result = SprintPlanner(items).build_plan(10)

    assert [item.item_id for item in result.items] == ["CRIT-1", "HIGH-1", "LOW-1"]
    assert result.total_estimate == 10


def test_build_plan_breaks_equal_ties_by_case_sensitive_id() -> None:
    items = [
        make_item("item-2", WorkItemPriority.NORMAL, WorkItemStatus.READY, 1, 0),
        make_item("Item-1", WorkItemPriority.NORMAL, WorkItemStatus.READY, 1, 0),
    ]

    result = SprintPlanner(items).build_plan(2)

    assert [item.item_id for item in result.items] == ["Item-1", "item-2"]


def test_build_plan_rejects_negative_capacity() -> None:
    with pytest.raises(ValueError):
        SprintPlanner([]).build_plan(-1)