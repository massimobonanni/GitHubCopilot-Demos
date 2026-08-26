"""Acceptance criteria for the delivery-risk workstream."""

from datetime import datetime, timedelta, timezone

import pytest

from risk_analyzer import RiskAnalyzer
from work_items import (
    RiskLevel,
    WorkItem,
    WorkItemPriority,
    WorkItemStatus,
)


NOW = datetime(2026, 8, 20, 12, 0, tzinfo=timezone.utc)


def make_item(
    item_id: str,
    status: WorkItemStatus,
    due_at: datetime | None,
) -> WorkItem:
    return WorkItem(
        item_id,
        f"Item {item_id}",
        WorkItemPriority.NORMAL,
        status,
        1,
        NOW - timedelta(days=10),
        due_at,
    )


def test_find_at_risk_classifies_filters_and_orders_items() -> None:
    items = [
        make_item("SOON-2", WorkItemStatus.IN_PROGRESS, NOW + timedelta(days=2)),
        make_item("OVER-2", WorkItemStatus.READY, NOW - timedelta(days=1)),
        make_item("SOON-1", WorkItemStatus.READY, NOW),
        make_item("OVER-1", WorkItemStatus.READY, NOW - timedelta(days=2)),
        make_item("LATER-1", WorkItemStatus.READY, NOW + timedelta(days=4)),
        make_item("DONE-1", WorkItemStatus.DONE, NOW - timedelta(days=3)),
        make_item("NO-DUE", WorkItemStatus.READY, None),
    ]

    result = RiskAnalyzer(items).find_at_risk(NOW, 3)

    assert [entry.item.item_id for entry in result] == [
        "OVER-1",
        "OVER-2",
        "SOON-1",
        "SOON-2",
    ]
    assert [entry.risk for entry in result] == [
        RiskLevel.OVERDUE,
        RiskLevel.OVERDUE,
        RiskLevel.DUE_SOON,
        RiskLevel.DUE_SOON,
    ]


def test_find_at_risk_includes_boundary_and_breaks_ties_by_id() -> None:
    boundary = NOW + timedelta(days=3)
    items = [
        make_item("B-2", WorkItemStatus.READY, boundary),
        make_item("B-1", WorkItemStatus.READY, boundary),
    ]

    result = RiskAnalyzer(items).find_at_risk(NOW, 3)

    assert [entry.item.item_id for entry in result] == ["B-1", "B-2"]


def test_find_at_risk_rejects_negative_warning_window() -> None:
    with pytest.raises(ValueError):
        RiskAnalyzer([]).find_at_risk(NOW, -1)