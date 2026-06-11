from __future__ import annotations

from dataclasses import dataclass
from datetime import datetime, timezone
from typing import Optional


@dataclass(frozen=True)
class Customer:
    id: int
    name: str
    email: str
    phone: Optional[str]
    created_at: datetime


@dataclass
class CreateCustomerRequest:
    name: str
    email: str
    phone: Optional[str] = None


@dataclass
class UpdateCustomerRequest:
    name: Optional[str] = None
    email: Optional[str] = None
    phone: Optional[str] = None


class CustomerService:

    def __init__(self) -> None:
        self._store: dict[int, Customer] = {}
        self._next_id: int = 1

    def create(self, request: CreateCustomerRequest) -> Customer:
        if not request.name or not request.name.strip():
            raise ValueError("Name cannot be empty.")
        if not request.email or "@" not in request.email:
            raise ValueError("A valid email address is required.")

        customer = Customer(
            id=self._next_id,
            name=request.name.strip(),
            email=request.email.strip().lower(),
            phone=request.phone.strip() if request.phone else None,
            created_at=datetime.now(timezone.utc),
        )
        self._next_id += 1
        self._store[customer.id] = customer
        return customer

    def get_by_id(self, customer_id: int) -> Optional[Customer]:
        if not isinstance(customer_id, int) or customer_id <= 0:
            raise ValueError("customer_id must be a positive integer.")
        return self._store.get(customer_id)

    def search(
        self,
        query: str,
        page: int = 1,
        page_size: int = 20,
    ) -> list[Customer]:
        if not query or not query.strip():
            raise ValueError("Search query cannot be empty.")
        if page < 1:
            raise ValueError("page must be at least 1.")
        if not (1 <= page_size <= 100):
            raise ValueError("page_size must be between 1 and 100.")

        lower = query.lower()
        matches = sorted(
            [
                c
                for c in self._store.values()
                if lower in c.name.lower() or lower in c.email.lower()
            ],
            key=lambda c: c.name,
        )
        start = (page - 1) * page_size
        return matches[start : start + page_size]

    def update(self, customer_id: int, request: UpdateCustomerRequest) -> Customer:
        if not isinstance(customer_id, int) or customer_id <= 0:
            raise ValueError("customer_id must be a positive integer.")
        existing = self._store.get(customer_id)
        if existing is None:
            raise KeyError(f"No customer with ID {customer_id} was found.")

        updated = Customer(
            id=existing.id,
            name=request.name.strip() if request.name else existing.name,
            email=request.email.strip().lower() if request.email else existing.email,
            phone=request.phone.strip() if request.phone is not None else existing.phone,
            created_at=existing.created_at,
        )
        self._store[customer_id] = updated
        return updated

    def delete(self, customer_id: int) -> bool:
        if not isinstance(customer_id, int) or customer_id <= 0:
            raise ValueError("customer_id must be a positive integer.")
        if customer_id in self._store:
            del self._store[customer_id]
            return True
        return False

    @property
    def count(self) -> int:
        return len(self._store)
