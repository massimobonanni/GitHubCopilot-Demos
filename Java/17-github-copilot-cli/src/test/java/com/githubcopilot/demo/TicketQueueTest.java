package com.githubcopilot.demo;

import static com.githubcopilot.demo.TicketQueue.TicketPriority.CRITICAL;
import static com.githubcopilot.demo.TicketQueue.TicketPriority.HIGH;
import static com.githubcopilot.demo.TicketQueue.TicketPriority.LOW;
import static com.githubcopilot.demo.TicketQueue.TicketPriority.NORMAL;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.githubcopilot.demo.TicketQueue.OwnerWorkload;
import com.githubcopilot.demo.TicketQueue.SupportTicket;
import com.githubcopilot.demo.TicketQueue.TicketPriority;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TicketQueueTest {

    private static final Instant START = Instant.parse("2026-08-01T09:00:00Z");

    @Test
    void ordersByPriorityThenAgeThenId() {
        List<SupportTicket> tickets = List.of(
                ticket("LOW-1", LOW, 0, "Lee"),
                ticket("HIGH-2", HIGH, 2, "Sam"),
                ticket("CRIT-1", CRITICAL, 3, null),
                ticket("HIGH-1", HIGH, 1, "Sam"),
                ticket("HIGH-0", HIGH, 1, "Lee"));

        List<String> result = new TicketQueue(tickets).orderForTriage().stream()
                .map(SupportTicket::id)
                .toList();

        assertEquals(List.of("CRIT-1", "HIGH-0", "HIGH-1", "HIGH-2", "LOW-1"), result);
    }

    @Test
    void orderingDoesNotMutateInput() {
        List<SupportTicket> tickets = new ArrayList<>(List.of(
                ticket("LOW-1", LOW, 0, "Lee"),
                ticket("CRIT-1", CRITICAL, 1, "Sam")));

        new TicketQueue(tickets).orderForTriage();

        assertEquals(List.of("LOW-1", "CRIT-1"), tickets.stream().map(SupportTicket::id).toList());
    }

    @Test
    void groupsOwnersAndSortsByCount() {
        List<SupportTicket> tickets = List.of(
                ticket("T-1", NORMAL, 0, "Sam"),
                ticket("T-2", NORMAL, 1, "sam"),
                ticket("T-3", NORMAL, 2, "Lee"),
                ticket("T-4", NORMAL, 3, "Alex"),
                ticket("T-5", NORMAL, 4, "Alex"));

        assertEquals(
                List.of(
                        new OwnerWorkload("Alex", 2),
                        new OwnerWorkload("Sam", 2),
                        new OwnerWorkload("Lee", 1)),
                new TicketQueue(tickets).getOwnerWorkload());
    }

    @ParameterizedTest
    @MethodSource("blankOwners")
    void mapsBlankOwnerToUnassigned(String owner) {
        TicketQueue queue = new TicketQueue(List.of(ticket("T-1", NORMAL, 0, owner)));

        assertEquals(List.of(new OwnerWorkload("Unassigned", 1)), queue.getOwnerWorkload());
    }

    static Stream<Arguments> blankOwners() {
        return Stream.of(Arguments.of((String) null), Arguments.of(""), Arguments.of("   "));
    }

    @Test
    void emptyQueueReturnsEmptyResults() {
        TicketQueue queue = new TicketQueue(List.of());

        assertEquals(List.of(), queue.orderForTriage());
        assertEquals(List.of(), queue.getOwnerWorkload());
    }

    private static SupportTicket ticket(
            String id,
            TicketPriority priority,
            int ageInHours,
            String owner) {
        return new SupportTicket(
                id,
                "Ticket " + id,
                priority,
                START.plus(ageInHours, ChronoUnit.HOURS),
                owner);
    }
}
