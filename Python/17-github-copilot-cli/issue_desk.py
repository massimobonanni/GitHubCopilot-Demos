"""Starter code for the GitHub Copilot CLI demo."""

from dataclasses import dataclass
from datetime import datetime
from enum import Enum


class TicketPriority(Enum):
    """Priority assigned to a support ticket."""

    LOW = "low"
    NORMAL = "normal"
    HIGH = "high"
    CRITICAL = "critical"


@dataclass(frozen=True)
class SupportTicket:
    """A support ticket waiting to be triaged."""

    ticket_id: str
    title: str
    priority: TicketPriority
    created_at: datetime
    owner: str | None


@dataclass(frozen=True)
class OwnerWorkload:
    """The number of tickets assigned to an owner."""

    owner: str
    ticket_count: int


class TicketQueue:
    """Provides triage ordering and owner workload information."""

    def __init__(self, tickets: list[SupportTicket]) -> None:
        if tickets is None:
            raise TypeError("tickets cannot be None")
        self._tickets = tuple(tickets)

    def order_for_triage(self) -> list[SupportTicket]:
        """Return tickets in the order in which they should be handled."""
        # TODO (Copilot CLI): implement the behavior specified in TASK.md.
        raise NotImplementedError

    def get_owner_workload(self) -> list[OwnerWorkload]:
        """Return ticket counts grouped and sorted by owner."""
        # TODO (Copilot CLI): implement the behavior specified in TASK.md.
        raise NotImplementedError
