'use strict';

const NotificationChannel = Object.freeze({ EMAIL: 'email', SMS: 'sms', PUSH: 'push' });
const NotificationStatus  = Object.freeze({ PENDING: 'pending', SENT: 'sent', FAILED: 'failed', SUPPRESSED: 'suppressed' });

class Notification {
  constructor(id, recipientId, channel, subject, body, status, createdAt, sentAt) {
    this.id          = id;
    this.recipientId = recipientId;
    this.channel     = channel;
    this.subject     = subject;
    this.body        = body;
    this.status      = status;
    this.createdAt   = createdAt;
    this.sentAt      = sentAt ?? null;
  }
}

class NotificationService {
  #store              = new Map();
  #suppressedSet      = new Set();
  #idCounter          = 1;

  async send(request) {
    if (!request?.recipientId?.trim()) throw new TypeError('recipientId is required.');
    if (!request?.subject?.trim())     throw new TypeError('subject is required.');
    if (!request?.body?.trim())        throw new TypeError('body is required.');
    if (!Object.values(NotificationChannel).includes(request.channel)) {
      throw new RangeError(`Invalid channel "${request.channel}".`);
    }

    const suppressed = this.#suppressedSet.has(request.recipientId);
    const status     = suppressed ? NotificationStatus.SUPPRESSED : NotificationStatus.SENT;
    const now        = new Date();

    const notification = new Notification(
      this.#idCounter++,
      request.recipientId.trim(),
      request.channel,
      request.subject.trim(),
      request.body.trim(),
      status,
      now,
      status === NotificationStatus.SENT ? now : null
    );

    this.#store.set(notification.id, notification);
    return notification;
  }

  async getById(id) {
    if (!Number.isInteger(id) || id < 1) throw new RangeError('id must be a positive integer.');
    return this.#store.get(id) ?? null;
  }

  async getForRecipient(recipientId, { channel = null, page = 1, pageSize = 20 } = {}) {
    if (!recipientId?.trim())                         throw new TypeError('recipientId is required.');
    if (!Number.isInteger(page) || page < 1)          throw new RangeError('page must be >= 1.');
    if (!Number.isInteger(pageSize) || pageSize < 1 || pageSize > 100)
      throw new RangeError('pageSize must be between 1 and 100.');

    const results = [...this.#store.values()]
      .filter(n => n.recipientId === recipientId && (channel === null || n.channel === channel))
      .sort((a, b) => b.createdAt - a.createdAt);

    return results.slice((page - 1) * pageSize, page * pageSize);
  }

  suppress(recipientId) {
    if (!recipientId?.trim()) throw new TypeError('recipientId is required.');
    this.#suppressedSet.add(recipientId);
  }

  unsuppress(recipientId) { this.#suppressedSet.delete(recipientId); }

  isSuppressed(recipientId) { return this.#suppressedSet.has(recipientId); }

  get totalCount() { return this.#store.size; }
  get sentCount()  { return [...this.#store.values()].filter(n => n.status === NotificationStatus.SENT).length; }
}

module.exports = { NotificationChannel, NotificationStatus, Notification, NotificationService };
