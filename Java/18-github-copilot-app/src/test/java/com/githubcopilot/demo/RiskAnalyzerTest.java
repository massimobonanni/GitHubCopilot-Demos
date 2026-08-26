package com.githubcopilot.demo;

import static com.githubcopilot.demo.RiskAnalyzer.RiskLevel.DUE_SOON;
import static com.githubcopilot.demo.RiskAnalyzer.RiskLevel.OVERDUE;
import static com.githubcopilot.demo.WorkItem.Priority.NORMAL;
import static com.githubcopilot.demo.WorkItem.Status.DONE;
import static com.githubcopilot.demo.WorkItem.Status.IN_PROGRESS;
import static com.githubcopilot.demo.WorkItem.Status.READY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.githubcopilot.demo.WorkItem.Status;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Test;

class RiskAnalyzerTest {

    private static final Instant NOW = Instant.parse("2026-08-20T12:00:00Z");

    @Test
    void classifiesFiltersAndOrdersItems() {
        List<WorkItem> items = List.of(
                item("SOON-2", IN_PROGRESS, NOW.plus(2, ChronoUnit.DAYS)),
                item("OVER-2", READY, NOW.minus(1, ChronoUnit.DAYS)),
                item("SOON-1", READY, NOW),
                item("OVER-1", READY, NOW.minus(2, ChronoUnit.DAYS)),
                item("LATER-1", READY, NOW.plus(4, ChronoUnit.DAYS)),
                item("DONE-1", DONE, NOW.minus(3, ChronoUnit.DAYS)),
                item("NO-DUE", READY, null));

        List<RiskAnalyzer.AtRiskItem> result = new RiskAnalyzer(items).findAtRisk(NOW, 3);

        assertEquals(
                List.of("OVER-1", "OVER-2", "SOON-1", "SOON-2"),
                result.stream().map(entry -> entry.item().id()).toList());
        assertEquals(
                List.of(OVERDUE, OVERDUE, DUE_SOON, DUE_SOON),
                result.stream().map(RiskAnalyzer.AtRiskItem::risk).toList());
    }

    @Test
    void includesWarningBoundaryAndBreaksTiesById() {
        Instant boundary = NOW.plus(3, ChronoUnit.DAYS);
        List<WorkItem> items = List.of(
                item("B-2", READY, boundary),
                item("B-1", READY, boundary));

        List<RiskAnalyzer.AtRiskItem> result = new RiskAnalyzer(items).findAtRisk(NOW, 3);

        assertEquals(
                List.of("B-1", "B-2"),
                result.stream().map(entry -> entry.item().id()).toList());
    }

    @Test
    void rejectsNegativeWarningWindow() {
        RiskAnalyzer analyzer = new RiskAnalyzer(List.of());

        assertThrows(IllegalArgumentException.class, () -> analyzer.findAtRisk(NOW, -1));
    }

    private static WorkItem item(String id, Status status, Instant dueAt) {
        return new WorkItem(
                id,
                "Item " + id,
                NORMAL,
                status,
                1,
                NOW.minus(10, ChronoUnit.DAYS),
                dueAt);
    }
}