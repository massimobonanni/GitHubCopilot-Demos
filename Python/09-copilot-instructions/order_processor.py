"""
Demo 9 — Copilot Instructions
================================
Copilot Instructions let you define project-wide coding standards that
Copilot always respects — without repeating them in every prompt.

How it works:
  • Create a `*.instructions.md` file with optional `applyTo` frontmatter
  • Place it in `.vscode/instructions/` (workspace) or `.github/` (repo-wide)
  • Copilot reads it automatically and applies the rules to every suggestion
  • No need to repeat conventions in individual prompts

Setup for this demo:
  1. Copy `coding-standards.instructions.md` from this folder to:
         .vscode/instructions/coding-standards.instructions.md
     (or rename it to `copilot-instructions.md` and place it in `.github/`)
  2. Reload VS Code (Ctrl+Shift+P → "Developer: Reload Window")
  3. Come back to this file and complete the TODO methods below

Exercises:
  1. First, DELETE the instructions file (or move it away) and complete
     one TODO — note the style Copilot uses.
  2. Restore the instructions file, reload, and complete the same TODO
     again — compare the difference in naming, typing, error handling,
     and logging.
  3. Open Chat and ask: "Does this class follow our coding standards?"
  4. Ask Chat: "Review OrderProcessor and flag any standards violations."
  5. Bonus — edit coding-standards.instructions.md to add a new rule,
     then ask Copilot to update the existing code to comply.
"""

import logging
from dataclasses import dataclass
from datetime import datetime
from enum import Enum

logger = logging.getLogger(__name__)

MAX_RETRY_ATTEMPTS = 3
ORDER_ID_PREFIX = "ORD"


class OrderStatus(Enum):
    PENDING = "pending"
    CONFIRMED = "confirmed"
    SHIPPED = "shipped"
    DELIVERED = "delivered"
    CANCELLED = "cancelled"


@dataclass
class Order:
    order_id: str
    customer_email: str
    items: list[str]
    total_amount: float
    status: OrderStatus
    created_at: datetime


@dataclass
class ProcessingResult:
    success: bool
    order_id: str
    message: str
    processed_at: datetime


class OrderProcessor:
    """Processes customer orders through their lifecycle."""

    def __init__(self, db_connection, email_client):
        self.db = db_connection
        self.email = email_client

    # TODO: Implement validate_order(self, order: Order) -> ProcessingResult
    # Rules: order_id must start with ORDER_ID_PREFIX, total_amount must be > 0,
    # customer_email must contain '@', items list must not be empty.
    # Return a ProcessingResult with success=True or an error message.

    # TODO: Implement confirm_order(self, order: Order) -> ProcessingResult
    # Update the order status to CONFIRMED in self.db,
    # send a confirmation email via self.email, and log the confirmation event.
    # Return a ProcessingResult.

    # TODO: Implement cancel_order(self, order: Order, reason: str) -> ProcessingResult
    # Validate the order is not already DELIVERED or CANCELLED.
    # Update the status to CANCELLED, notify the customer, and log the event.
    # Return a ProcessingResult.
