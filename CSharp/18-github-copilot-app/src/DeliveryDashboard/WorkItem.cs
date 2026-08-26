namespace DeliveryDashboard;

public enum WorkItemPriority
{
    Low,
    Normal,
    High,
    Critical
}

public enum WorkItemStatus
{
    Draft,
    Ready,
    InProgress,
    Done
}

public sealed record WorkItem(
    string Id,
    string Title,
    WorkItemPriority Priority,
    WorkItemStatus Status,
    int Estimate,
    DateTimeOffset CreatedAt,
    DateTimeOffset? DueAt);

public sealed record SprintPlan(IReadOnlyList<WorkItem> Items, int TotalEstimate);

public enum RiskLevel
{
    DueSoon,
    Overdue
}

public sealed record AtRiskItem(WorkItem Item, RiskLevel Risk);