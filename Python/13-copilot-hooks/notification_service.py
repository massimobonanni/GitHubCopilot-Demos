from __future__ import annotations

from dataclasses import dataclass, field
from datetime import datetime, timezone
from enum import Enum
from typing import Optional


class NotificationChannel(str, Enum):
    EMAIL = "email"
    SMS   = "sms"
    PUSH  = "push"


class NotificationStatus(str, Enum):
    PENDING    = "pending"
    SENT       = "sent"
    FAILED     = "failed"
    SUPPRESSED = "suppressed"


@dataclass(frozen=True)
class Notification:
    id:           int
    recipient_id: str
    channel:      NotificationChannel
    subject:      str
    body:         str
    status:       NotificationStatus
    created_at:   datetime
    sent_at:      Optional[datetime]


@dataclass
class SendNotificationRequest:
    recipient_id: str
    channel:      NotificationChannel
    subject:      str
    body:         str


class NotificationService:

    def __init__(self) -> None:
        self._store:       dict[int, Notification] = {}
        self._suppressed:  set[str]                = set()
        self._next_id:     int                     = 1

    def send(self, request: SendNotificationRequest) -> Notification:
        if not request.recipient_id or not request.recipient_id.strip():
            raise ValueError("recipient_id cannot be empty.")
        if not request.subject or not request.subject.strip():
            raise ValueError("subject cannot be empty.")
        if not request.body or not request.body.strip():
            raise ValueError("body cannot be empty.")

        suppressed = request.recipient_id in self._suppressed
        status     = NotificationStatus.SUPPRESSED if suppressed else NotificationStatus.SENT
        now        = datetime.now(timezone.utc)

        notification = Notification(
            id=self._next_id,
            recipient_id=request.recipient_id.strip(),
            channel=request.channel,
            subject=request.subject.strip(),
            body=request.body.strip(),
            status=status,
            created_at=now,
            sent_at=now if status == NotificationStatus.SENT else None,
        )
        self._next_id += 1
        self._store[notification.id] = notification
        return notification

    def get_by_id(self, notification_id: int) -> Optional[Notification]:
        if not isinstance(notification_id, int) or notification_id < 1:
            raise ValueError("notification_id must be a positive integer.")
        return self._store.get(notification_id)

    def get_for_recipient(
        self,
        recipient_id: str,
        channel: Optional[NotificationChannel] = None,
        page: int = 1,
        page_size: int = 20,
    ) -> list[Notification]:
        if not recipient_id or not recipient_id.strip():
            raise ValueError("recipient_id cannot be empty.")
        if page < 1:
            raise ValueError("page must be at least 1.")
        if not (1 <= page_size <= 100):
            raise ValueError("page_size must be between 1 and 100.")

        matches = sorted(
            [
                n for n in self._store.values()
                if n.recipient_id == recipient_id
                and (channel is None or n.channel == channel)
            ],
            key=lambda n: n.created_at,
            reverse=True,
        )
        start = (page - 1) * page_size
        return matches[start : start + page_size]

    def suppress(self, recipient_id: str) -> None:
        if not recipient_id or not recipient_id.strip():
            raise ValueError("recipient_id cannot be empty.")
        self._suppressed.add(recipient_id)

    def unsuppress(self, recipient_id: str) -> None:
        self._suppressed.discard(recipient_id)

    def is_suppressed(self, recipient_id: str) -> bool:
        return recipient_id in self._suppressed

    @property
    def total_count(self) -> int:
        return len(self._store)

    @property
    def sent_count(self) -> int:
        return sum(1 for n in self._store.values() if n.status == NotificationStatus.SENT)
