'use strict';

const TicketPriority = Object.freeze({
  LOW: 'low',
  NORMAL: 'normal',
  HIGH: 'high',
  CRITICAL: 'critical',
});

class TicketQueue {
  /**
   * @param {{ id: string, title: string, priority: string, createdAt: Date, owner: string | null }[]} tickets
   */
  constructor(tickets) {
    if (!Array.isArray(tickets)) {
      throw new TypeError('tickets must be an array');
    }

    this.tickets = [...tickets];
  }

  /**
   * Returns tickets in the order in which they should be handled.
   * @returns {{ id: string, title: string, priority: string, createdAt: Date, owner: string | null }[]}
   */
  orderForTriage() {
    // TODO (Copilot CLI): implement the behavior specified in TASK.md.
    throw new Error('Not implemented');
  }

  /**
   * Returns ticket counts grouped and sorted by owner.
   * @returns {{ owner: string, ticketCount: number }[]}
   */
  getOwnerWorkload() {
    // TODO (Copilot CLI): implement the behavior specified in TASK.md.
    throw new Error('Not implemented');
  }
}

module.exports = { TicketPriority, TicketQueue };
