package com.githubcopilot.demo;

import java.util.List;
import java.util.Objects;

public final class SprintPlanner {

    public record SprintPlan(List<WorkItem> items, int totalEstimate) {
    }

    private final List<WorkItem> items;

    public SprintPlanner(List<WorkItem> items) {
        Objects.requireNonNull(items, "items cannot be null");
        this.items = List.copyOf(items);
    }

    public SprintPlan buildPlan(int capacity) {
        // TODO (Copilot app, workstream A): implement the behavior in TASK.md.
        throw new UnsupportedOperationException("Not implemented");
    }
}