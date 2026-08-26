"""Shared delivery dashboard models for the Copilot desktop app demo."""

from dataclasses import dataclass
from datetime import datetime
from enum import Enum


class WorkItemPriority(Enum):
    """Priority assigned to a delivery work item."""

    LOW = "low"
    NORMAL = "normal"
    HIGH = "high"
    CRITICAL = "critical"


class WorkItemStatus(Enum):
    """Current workflow state of a delivery work item."""

    DRAFT = "draft"
    READY = "ready"
    IN_PROGRESS = "in_progress"
    DONE = "done"


@dataclass(frozen=True)
class WorkItem:
    """A unit of work considered during sprint planning."""

    item_id: str
    title: str
    priority: WorkItemPriority
    status: WorkItemStatus
    estimate: int
    created_at: datetime
    due_at: datetime | None


@dataclass(frozen=True)
class SprintPlan:
    """Selected sprint items and their combined estimate."""

    items: tuple[WorkItem, ...]
    total_estimate: int


class RiskLevel(Enum):
    """Delivery risk assigned to a work item."""

    DUE_SOON = "due_soon"
    OVERDUE = "overdue"


@dataclass(frozen=True)
class AtRiskItem:
    """A work item paired with its delivery risk."""

    item: WorkItem
    risk: RiskLevel