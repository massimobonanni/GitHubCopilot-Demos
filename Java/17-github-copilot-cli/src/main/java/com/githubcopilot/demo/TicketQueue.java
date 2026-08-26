package com.githubcopilot.demo;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public final class TicketQueue {

    public enum TicketPriority {
        LOW,
        NORMAL,
        HIGH,
        CRITICAL
    }

    public record SupportTicket(
            String id,
            String title,
            TicketPriority priority,
            Instant createdAt,
            String owner) {
    }

    public record OwnerWorkload(String owner, int ticketCount) {
    }

    private final List<SupportTicket> tickets;

    public TicketQueue(List<SupportTicket> tickets) {
        Objects.requireNonNull(tickets, "tickets cannot be null");
        this.tickets = List.copyOf(tickets);
    }

    public List<SupportTicket> orderForTriage() {
        // TODO (Copilot CLI): implement the behavior specified in TASK.md.
        throw new UnsupportedOperationException("Not implemented");
    }

    public List<OwnerWorkload> getOwnerWorkload() {
        // TODO (Copilot CLI): implement the behavior specified in TASK.md.
        throw new UnsupportedOperationException("Not implemented");
    }
}
