'use strict';

class SprintPlanner {
  /**
   * @param {{ id: string, title: string, priority: string, status: string, estimate: number, createdAt: Date, dueAt: Date | null }[]} items
   */
  constructor(items) {
    if (!Array.isArray(items)) {
      throw new TypeError('items must be an array');
    }

    this.items = [...items];
  }

  /**
   * Select ready work within the available capacity.
   * @param {number} capacity
   * @returns {{ items: object[], totalEstimate: number }}
   */
  buildPlan(capacity) {
    // TODO (Copilot app, workstream A): implement the behavior in TASK.md.
    throw new Error('Not implemented');
  }
}

module.exports = { SprintPlanner };