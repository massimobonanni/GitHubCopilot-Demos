using IssueDesk;
using Xunit;

namespace IssueDesk.Tests;

public sealed class TicketQueueTests
{
    private static readonly DateTimeOffset Start = new(2026, 8, 1, 9, 0, 0, TimeSpan.Zero);

    [Fact]
    public void OrderForTriage_SortsByPriorityThenAgeThenId()
    {
        var tickets = new[]
        {
            Ticket("LOW-1", TicketPriority.Low, 0, "Lee"),
            Ticket("HIGH-2", TicketPriority.High, 2, "Sam"),
            Ticket("CRIT-1", TicketPriority.Critical, 3, null),
            Ticket("HIGH-1", TicketPriority.High, 1, "Sam"),
            Ticket("HIGH-0", TicketPriority.High, 1, "Lee")
        };

        var result = new TicketQueue(tickets).OrderForTriage();

        Assert.Equal(
            ["CRIT-1", "HIGH-0", "HIGH-1", "HIGH-2", "LOW-1"],
            result.Select(ticket => ticket.Id));
    }

    [Fact]
    public void OrderForTriage_DoesNotMutateInput()
    {
        var tickets = new List<SupportTicket>
        {
            Ticket("LOW-1", TicketPriority.Low, 0, "Lee"),
            Ticket("CRIT-1", TicketPriority.Critical, 1, "Sam")
        };

        _ = new TicketQueue(tickets).OrderForTriage();

        Assert.Equal(["LOW-1", "CRIT-1"], tickets.Select(ticket => ticket.Id));
    }

    [Fact]
    public void GetOwnerWorkload_GroupsOwnersAndSortsByCount()
    {
        var tickets = new[]
        {
            Ticket("T-1", TicketPriority.Normal, 0, "Sam"),
            Ticket("T-2", TicketPriority.Normal, 1, "sam"),
            Ticket("T-3", TicketPriority.Normal, 2, "Lee"),
            Ticket("T-4", TicketPriority.Normal, 3, "Alex"),
            Ticket("T-5", TicketPriority.Normal, 4, "Alex")
        };

        var result = new TicketQueue(tickets).GetOwnerWorkload();

        Assert.Equal(
            [new OwnerWorkload("Alex", 2), new OwnerWorkload("Sam", 2), new OwnerWorkload("Lee", 1)],
            result);
    }

    [Theory]
    [InlineData(null)]
    [InlineData("")]
    [InlineData("   ")]
    public void GetOwnerWorkload_MapsBlankOwnerToUnassigned(string? owner)
    {
        var queue = new TicketQueue([Ticket("T-1", TicketPriority.Normal, 0, owner)]);

        Assert.Equal([new OwnerWorkload("Unassigned", 1)], queue.GetOwnerWorkload());
    }

    [Fact]
    public void EmptyQueue_ReturnsEmptyResults()
    {
        var queue = new TicketQueue([]);

        Assert.Empty(queue.OrderForTriage());
        Assert.Empty(queue.GetOwnerWorkload());
    }

    private static SupportTicket Ticket(
        string id,
        TicketPriority priority,
        int ageInHours,
        string? owner) =>
        new(id, $"Ticket {id}", priority, Start.AddHours(ageInHours), owner);
}
