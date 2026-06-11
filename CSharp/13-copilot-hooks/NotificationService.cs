using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

namespace Demo.Services;

public enum NotificationChannel { Email, Sms, Push }

public enum NotificationStatus { Pending, Sent, Failed, Suppressed }

public record Notification(
    Guid Id,
    string RecipientId,
    NotificationChannel Channel,
    string Subject,
    string Body,
    NotificationStatus Status,
    DateTime CreatedAt,
    DateTime? SentAt);

public record SendNotificationRequest(
    string RecipientId,
    NotificationChannel Channel,
    string Subject,
    string Body);

public class NotificationService
{
    private readonly Dictionary<Guid, Notification> _store = new();
    private readonly HashSet<string> _suppressedRecipients = new();

    public async Task<Notification> SendAsync(
        SendNotificationRequest request,
        CancellationToken cancellationToken = default)
    {
        if (string.IsNullOrWhiteSpace(request.RecipientId))
            throw new ArgumentException("RecipientId cannot be empty.", nameof(request));
        if (string.IsNullOrWhiteSpace(request.Subject))
            throw new ArgumentException("Subject cannot be empty.", nameof(request));
        if (string.IsNullOrWhiteSpace(request.Body))
            throw new ArgumentException("Body cannot be empty.", nameof(request));

        await Task.Delay(0, cancellationToken);

        var status = _suppressedRecipients.Contains(request.RecipientId)
            ? NotificationStatus.Suppressed
            : NotificationStatus.Sent;

        var notification = new Notification(
            Guid.NewGuid(),
            request.RecipientId,
            request.Channel,
            request.Subject.Trim(),
            request.Body.Trim(),
            status,
            DateTime.UtcNow,
            status == NotificationStatus.Sent ? DateTime.UtcNow : null);

        _store[notification.Id] = notification;
        return notification;
    }

    public async Task<Notification?> GetByIdAsync(
        Guid notificationId,
        CancellationToken cancellationToken = default)
    {
        await Task.Delay(0, cancellationToken);
        _store.TryGetValue(notificationId, out var notification);
        return notification;
    }

    public async Task<IReadOnlyList<Notification>> GetForRecipientAsync(
        string recipientId,
        NotificationChannel? channel = null,
        int page = 1,
        int pageSize = 20,
        CancellationToken cancellationToken = default)
    {
        if (string.IsNullOrWhiteSpace(recipientId))
            throw new ArgumentException("RecipientId cannot be empty.", nameof(recipientId));
        if (page < 1)
            throw new ArgumentOutOfRangeException(nameof(page));
        if (pageSize is < 1 or > 100)
            throw new ArgumentOutOfRangeException(nameof(pageSize));

        await Task.Delay(0, cancellationToken);

        return _store.Values
            .Where(n => n.RecipientId == recipientId
                     && (channel == null || n.Channel == channel))
            .OrderByDescending(n => n.CreatedAt)
            .Skip((page - 1) * pageSize)
            .Take(pageSize)
            .ToList()
            .AsReadOnly();
    }

    public void SuppressRecipient(string recipientId)
    {
        if (string.IsNullOrWhiteSpace(recipientId))
            throw new ArgumentException("RecipientId cannot be empty.", nameof(recipientId));
        _suppressedRecipients.Add(recipientId);
    }

    public void UnsuppressRecipient(string recipientId) =>
        _suppressedRecipients.Remove(recipientId);

    public bool IsRecipientSuppressed(string recipientId) =>
        _suppressedRecipients.Contains(recipientId);

    public int TotalCount => _store.Count;

    public int SentCount => _store.Values.Count(n => n.Status == NotificationStatus.Sent);
}
