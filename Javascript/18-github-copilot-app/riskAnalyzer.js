'use strict';

class RiskAnalyzer {
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
   * Return incomplete work due on or before the warning boundary.
   * @param {Date} asOf
   * @param {number} warningWindowDays
   * @returns {{ item: object, risk: string }[]}
   */
  findAtRisk(asOf, warningWindowDays) {
    // TODO (Copilot app, workstream B): implement the behavior in TASK.md.
    throw new Error('Not implemented');
  }
}

module.exports = { RiskAnalyzer };