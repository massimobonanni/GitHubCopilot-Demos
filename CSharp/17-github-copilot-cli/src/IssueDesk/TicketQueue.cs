namespace IssueDesk;

public enum TicketPriority
{
    Low,
    Normal,
    High,
    Critical
}

public sealed record SupportTicket(
    string Id,
    string Title,
    TicketPriority Priority,
    DateTimeOffset CreatedAt,
    string? Owner);

public sealed record OwnerWorkload(string Owner, int TicketCount);

public sealed class TicketQueue
{
    private readonly IReadOnlyList<SupportTicket> _tickets;

    public TicketQueue(IEnumerable<SupportTicket> tickets)
    {
        ArgumentNullException.ThrowIfNull(tickets);
        _tickets = tickets.ToList();
    }

    public IReadOnlyList<SupportTicket> OrderForTriage()
    {
        // TODO (Copilot CLI): implement the behavior specified in TASK.md.
        throw new NotImplementedException();
    }

    public IReadOnlyList<OwnerWorkload> GetOwnerWorkload()
    {
        // TODO (Copilot CLI): implement the behavior specified in TASK.md.
        throw new NotImplementedException();
    }
}
