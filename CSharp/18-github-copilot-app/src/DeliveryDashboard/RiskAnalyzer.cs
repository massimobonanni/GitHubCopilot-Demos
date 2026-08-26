namespace DeliveryDashboard;

public sealed class RiskAnalyzer
{
    private readonly IReadOnlyList<WorkItem> _items;

    public RiskAnalyzer(IEnumerable<WorkItem> items)
    {
        ArgumentNullException.ThrowIfNull(items);
        _items = items.ToList();
    }

    public IReadOnlyList<AtRiskItem> FindAtRisk(
        DateTimeOffset asOf,
        int warningWindowDays)
    {
        // TODO (Copilot app, workstream B): implement the behavior in TASK.md.
        throw new NotImplementedException();
    }
}