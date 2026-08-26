package com.githubcopilot.demo;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public final class RiskAnalyzer {

    public enum RiskLevel {
        DUE_SOON,
        OVERDUE
    }

    public record AtRiskItem(WorkItem item, RiskLevel risk) {
    }

    private final List<WorkItem> items;

    public RiskAnalyzer(List<WorkItem> items) {
        Objects.requireNonNull(items, "items cannot be null");
        this.items = List.copyOf(items);
    }

    public List<AtRiskItem> findAtRisk(Instant asOf, int warningWindowDays) {
        // TODO (Copilot app, workstream B): implement the behavior in TASK.md.
        throw new UnsupportedOperationException("Not implemented");
    }
}