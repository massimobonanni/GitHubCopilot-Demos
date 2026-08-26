"""Executable acceptance criteria for the Copilot CLI demo."""

from datetime import datetime, timedelta, timezone

import pytest

from issue_desk import OwnerWorkload, SupportTicket, TicketPriority, TicketQueue


START = datetime(2026, 8, 1, 9, 0, tzinfo=timezone.utc)


def make_ticket(
    ticket_id: str,
    priority: TicketPriority,
    age_in_hours: int,
    owner: str | None,
) -> SupportTicket:
    return SupportTicket(
        ticket_id,
        f"Ticket {ticket_id}",
        priority,
        START + timedelta(hours=age_in_hours),
        owner,
    )


def test_order_for_triage_sorts_by_priority_then_age_then_id() -> None:
    tickets = [
        make_ticket("LOW-1", TicketPriority.LOW, 0, "Lee"),
        make_ticket("HIGH-2", TicketPriority.HIGH, 2, "Sam"),
        make_ticket("CRIT-1", TicketPriority.CRITICAL, 3, None),
        make_ticket("HIGH-1", TicketPriority.HIGH, 1, "Sam"),
        make_ticket("HIGH-0", TicketPriority.HIGH, 1, "Lee"),
    ]

    result = TicketQueue(tickets).order_for_triage()

    assert [ticket.ticket_id for ticket in result] == [
        "CRIT-1",
        "HIGH-0",
        "HIGH-1",
        "HIGH-2",
        "LOW-1",
    ]


def test_order_for_triage_does_not_mutate_input() -> None:
    tickets = [
        make_ticket("LOW-1", TicketPriority.LOW, 0, "Lee"),
        make_ticket("CRIT-1", TicketPriority.CRITICAL, 1, "Sam"),
    ]

    TicketQueue(tickets).order_for_triage()

    assert [ticket.ticket_id for ticket in tickets] == ["LOW-1", "CRIT-1"]


def test_get_owner_workload_groups_owners_and_sorts_by_count() -> None:
    tickets = [
        make_ticket("T-1", TicketPriority.NORMAL, 0, "Sam"),
        make_ticket("T-2", TicketPriority.NORMAL, 1, "sam"),
        make_ticket("T-3", TicketPriority.NORMAL, 2, "Lee"),
        make_ticket("T-4", TicketPriority.NORMAL, 3, "Alex"),
        make_ticket("T-5", TicketPriority.NORMAL, 4, "Alex"),
    ]

    result = TicketQueue(tickets).get_owner_workload()

    assert result == [
        OwnerWorkload("Alex", 2),
        OwnerWorkload("Sam", 2),
        OwnerWorkload("Lee", 1),
    ]


@pytest.mark.parametrize("owner", [None, "", "   "])
def test_get_owner_workload_maps_blank_owner_to_unassigned(owner: str | None) -> None:
    queue = TicketQueue([make_ticket("T-1", TicketPriority.NORMAL, 0, owner)])

    assert queue.get_owner_workload() == [OwnerWorkload("Unassigned", 1)]


def test_empty_queue_returns_empty_results() -> None:
    queue = TicketQueue([])

    assert queue.order_for_triage() == []
    assert queue.get_owner_workload() == []
