import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class NotificationService {

    public enum Channel { EMAIL, SMS, PUSH }

    public enum Status { PENDING, SENT, FAILED, SUPPRESSED }

    public record Notification(
            UUID id,
            String recipientId,
            Channel channel,
            String subject,
            String body,
            Status status,
            Instant createdAt,
            Instant sentAt) {}

    public record SendNotificationRequest(
            String recipientId,
            Channel channel,
            String subject,
            String body) {}

    private final Map<UUID, Notification> store = new ConcurrentHashMap<>();
    private final Set<String> suppressedRecipients = ConcurrentHashMap.newKeySet();

    public Notification send(SendNotificationRequest request) {
        if (request.recipientId() == null || request.recipientId().isBlank())
            throw new IllegalArgumentException("recipientId cannot be empty.");
        if (request.subject() == null || request.subject().isBlank())
            throw new IllegalArgumentException("subject cannot be empty.");
        if (request.body() == null || request.body().isBlank())
            throw new IllegalArgumentException("body cannot be empty.");
        if (request.channel() == null)
            throw new IllegalArgumentException("channel cannot be null.");

        Status status = suppressedRecipients.contains(request.recipientId())
                ? Status.SUPPRESSED
                : Status.SENT;

        Instant now = Instant.now();

        Notification notification = new Notification(
                UUID.randomUUID(),
                request.recipientId().trim(),
                request.channel(),
                request.subject().trim(),
                request.body().trim(),
                status,
                now,
                status == Status.SENT ? now : null);

        store.put(notification.id(), notification);
        return notification;
    }

    public Optional<Notification> getById(UUID notificationId) {
        return Optional.ofNullable(store.get(notificationId));
    }

    public List<Notification> getForRecipient(String recipientId, Channel channel, int page, int pageSize) {
        if (recipientId == null || recipientId.isBlank())
            throw new IllegalArgumentException("recipientId cannot be empty.");
        if (page < 1)
            throw new IllegalArgumentException("page must be >= 1.");
        if (pageSize < 1 || pageSize > 100)
            throw new IllegalArgumentException("pageSize must be between 1 and 100.");

        return store.values().stream()
                .filter(n -> n.recipientId().equals(recipientId))
                .filter(n -> channel == null || n.channel() == channel)
                .sorted(Comparator.comparing(Notification::createdAt).reversed())
                .skip((long) (page - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toUnmodifiableList());
    }

    public void suppressRecipient(String recipientId) {
        if (recipientId == null || recipientId.isBlank())
            throw new IllegalArgumentException("recipientId cannot be empty.");
        suppressedRecipients.add(recipientId);
    }

    public void unsuppressRecipient(String recipientId) {
        suppressedRecipients.remove(recipientId);
    }

    public boolean isRecipientSuppressed(String recipientId) {
        return suppressedRecipients.contains(recipientId);
    }

    public int getTotalCount() {
        return store.size();
    }

    public long getSentCount() {
        return store.values().stream()
                .filter(n -> n.status() == Status.SENT)
                .count();
    }
}
