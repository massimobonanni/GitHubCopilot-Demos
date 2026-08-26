using DeliveryDashboard;
using Xunit;

namespace DeliveryDashboard.Tests;

public sealed class RiskAnalyzerTests
{
    private static readonly DateTimeOffset Now =
        new(2026, 8, 20, 12, 0, 0, TimeSpan.Zero);

    [Fact]
    public void FindAtRisk_ClassifiesFiltersAndOrdersItems()
    {
        var items = new[]
        {
            Item("SOON-2", WorkItemStatus.InProgress, Now.AddDays(2)),
            Item("OVER-2", WorkItemStatus.Ready, Now.AddDays(-1)),
            Item("SOON-1", WorkItemStatus.Ready, Now),
            Item("OVER-1", WorkItemStatus.Ready, Now.AddDays(-2)),
            Item("LATER-1", WorkItemStatus.Ready, Now.AddDays(4)),
            Item("DONE-1", WorkItemStatus.Done, Now.AddDays(-3)),
            Item("NO-DUE", WorkItemStatus.Ready, null)
        };

        var result = new RiskAnalyzer(items).FindAtRisk(Now, 3);

        Assert.Equal(
            ["OVER-1", "OVER-2", "SOON-1", "SOON-2"],
            result.Select(entry => entry.Item.Id));
        Assert.Equal(
            [RiskLevel.Overdue, RiskLevel.Overdue, RiskLevel.DueSoon, RiskLevel.DueSoon],
            result.Select(entry => entry.Risk));
    }

    [Fact]
    public void FindAtRisk_IncludesWarningWindowBoundaryAndBreaksTiesById()
    {
        var boundary = Now.AddDays(3);
        var items = new[]
        {
            Item("B-2", WorkItemStatus.Ready, boundary),
            Item("B-1", WorkItemStatus.Ready, boundary)
        };

        var result = new RiskAnalyzer(items).FindAtRisk(Now, 3);

        Assert.Equal(["B-1", "B-2"], result.Select(entry => entry.Item.Id));
    }

    [Fact]
    public void FindAtRisk_RejectsNegativeWarningWindow()
    {
        var analyzer = new RiskAnalyzer([]);

        Assert.Throws<ArgumentOutOfRangeException>(() => analyzer.FindAtRisk(Now, -1));
    }

    private static WorkItem Item(
        string id,
        WorkItemStatus status,
        DateTimeOffset? dueAt) =>
        new(id, $"Item {id}", WorkItemPriority.Normal, status, 1, Now.AddDays(-10), dueAt);
}