package com.githubcopilot.demo;

import java.time.Instant;

public record WorkItem(
        String id,
        String title,
        Priority priority,
        Status status,
        int estimate,
        Instant createdAt,
        Instant dueAt) {

    public enum Priority {
        LOW,
        NORMAL,
        HIGH,
        CRITICAL
    }

    public enum Status {
        DRAFT,
        READY,
        IN_PROGRESS,
        DONE
    }
}