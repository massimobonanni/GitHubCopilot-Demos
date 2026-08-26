package com.githubcopilot.demo;

import static com.githubcopilot.demo.WorkItem.Priority.CRITICAL;
import static com.githubcopilot.demo.WorkItem.Priority.HIGH;
import static com.githubcopilot.demo.WorkItem.Priority.LOW;
import static com.githubcopilot.demo.WorkItem.Priority.NORMAL;
import static com.githubcopilot.demo.WorkItem.Status.DONE;
import static com.githubcopilot.demo.WorkItem.Status.READY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.githubcopilot.demo.WorkItem.Priority;
import com.githubcopilot.demo.WorkItem.Status;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Test;

class SprintPlannerTest {

    private static final Instant START = Instant.parse("2026-08-01T09:00:00Z");

    @Test
    void filtersOrdersAndUsesAvailableCapacity() {
        List<WorkItem> items = List.of(
                item("LOW-1", LOW, READY, 2, 0),
                item("CRIT-1", CRITICAL, READY, 5, 3),
                item("HIGH-2", HIGH, READY, 4, 2),
                item("HIGH-1", HIGH, READY, 3, 1),
                item("DONE-1", CRITICAL, DONE, 1, 0),
                item("ZERO-1", CRITICAL, READY, 0, 0));

        SprintPlanner.SprintPlan result = new SprintPlanner(items).buildPlan(10);

        assertEquals(
                List.of("CRIT-1", "HIGH-1", "LOW-1"),
                result.items().stream().map(WorkItem::id).toList());
        assertEquals(10, result.totalEstimate());
    }

    @Test
    void breaksEqualTiesByCaseSensitiveId() {
        List<WorkItem> items = List.of(
                item("item-2", NORMAL, READY, 1, 0),
                item("Item-1", NORMAL, READY, 1, 0));

        SprintPlanner.SprintPlan result = new SprintPlanner(items).buildPlan(2);

        assertEquals(
                List.of("Item-1", "item-2"),
                result.items().stream().map(WorkItem::id).toList());
    }

    @Test
    void rejectsNegativeCapacity() {
        SprintPlanner planner = new SprintPlanner(List.of());

        assertThrows(IllegalArgumentException.class, () -> planner.buildPlan(-1));
    }

    private static WorkItem item(
            String id,
            Priority priority,
            Status status,
            int estimate,
            int ageInHours) {
        return new WorkItem(
                id,
                "Item " + id,
                priority,
                status,
                estimate,
                START.plus(ageInHours, ChronoUnit.HOURS),
                null);
    }
}