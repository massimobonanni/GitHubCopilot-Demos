namespace DeliveryDashboard;

public sealed class SprintPlanner
{
    private readonly IReadOnlyList<WorkItem> _items;

    public SprintPlanner(IEnumerable<WorkItem> items)
    {
        ArgumentNullException.ThrowIfNull(items);
        _items = items.ToList();
    }

    public SprintPlan BuildPlan(int capacity)
    {
        // TODO (Copilot app, workstream A): implement the behavior in TASK.md.
        throw new NotImplementedException();
    }
}