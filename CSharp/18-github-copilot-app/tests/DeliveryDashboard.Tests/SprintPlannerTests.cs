using DeliveryDashboard;
using Xunit;

namespace DeliveryDashboard.Tests;

public sealed class SprintPlannerTests
{
    private static readonly DateTimeOffset Start =
        new(2026, 8, 1, 9, 0, 0, TimeSpan.Zero);

    [Fact]
    public void BuildPlan_FiltersOrdersAndUsesAvailableCapacity()
    {
        var items = new[]
        {
            Item("LOW-1", WorkItemPriority.Low, WorkItemStatus.Ready, 2, 0),
            Item("CRIT-1", WorkItemPriority.Critical, WorkItemStatus.Ready, 5, 3),
            Item("HIGH-2", WorkItemPriority.High, WorkItemStatus.Ready, 4, 2),
            Item("HIGH-1", WorkItemPriority.High, WorkItemStatus.Ready, 3, 1),
            Item("DONE-1", WorkItemPriority.Critical, WorkItemStatus.Done, 1, 0),
            Item("ZERO-1", WorkItemPriority.Critical, WorkItemStatus.Ready, 0, 0)
        };

        var result = new SprintPlanner(items).BuildPlan(10);

        Assert.Equal(["CRIT-1", "HIGH-1", "LOW-1"], result.Items.Select(item => item.Id));
        Assert.Equal(10, result.TotalEstimate);
    }

    [Fact]
    public void BuildPlan_BreaksEqualTiesByOrdinalId()
    {
        var items = new[]
        {
            Item("item-2", WorkItemPriority.Normal, WorkItemStatus.Ready, 1, 0),
            Item("Item-1", WorkItemPriority.Normal, WorkItemStatus.Ready, 1, 0)
        };

        var result = new SprintPlanner(items).BuildPlan(2);

        Assert.Equal(["Item-1", "item-2"], result.Items.Select(item => item.Id));
    }

    [Fact]
    public void BuildPlan_RejectsNegativeCapacity()
    {
        var planner = new SprintPlanner([]);

        Assert.Throws<ArgumentOutOfRangeException>(() => planner.BuildPlan(-1));
    }

    private static WorkItem Item(
        string id,
        WorkItemPriority priority,
        WorkItemStatus status,
        int estimate,
        int ageInHours) =>
        new(id, $"Item {id}", priority, status, estimate, Start.AddHours(ageInHours), null);
}