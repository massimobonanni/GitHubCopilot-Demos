'use strict';

const assert = require('node:assert/strict');
const { describe, test } = require('node:test');
const { RiskAnalyzer } = require('./riskAnalyzer');
const { RiskLevel, WorkItemPriority, WorkItemStatus } = require('./workItems');

const NOW = new Date('2026-08-20T12:00:00Z');
const DAY_IN_MS = 24 * 60 * 60 * 1000;

function item(id, status, dueAt) {
  return {
    id,
    title: `Item ${id}`,
    priority: WorkItemPriority.NORMAL,
    status,
    estimate: 1,
    createdAt: new Date(NOW.getTime() - 10 * DAY_IN_MS),
    dueAt,
  };
}

describe('RiskAnalyzer', () => {
  test('classifies, filters, and orders items', () => {
    const items = [
      item('SOON-2', WorkItemStatus.IN_PROGRESS, new Date(NOW.getTime() + 2 * DAY_IN_MS)),
      item('OVER-2', WorkItemStatus.READY, new Date(NOW.getTime() - DAY_IN_MS)),
      item('SOON-1', WorkItemStatus.READY, new Date(NOW)),
      item('OVER-1', WorkItemStatus.READY, new Date(NOW.getTime() - 2 * DAY_IN_MS)),
      item('LATER-1', WorkItemStatus.READY, new Date(NOW.getTime() + 4 * DAY_IN_MS)),
      item('DONE-1', WorkItemStatus.DONE, new Date(NOW.getTime() - 3 * DAY_IN_MS)),
      item('NO-DUE', WorkItemStatus.READY, null),
    ];

    const result = new RiskAnalyzer(items).findAtRisk(NOW, 3);

    assert.deepEqual(result.map((entry) => entry.item.id), [
      'OVER-1',
      'OVER-2',
      'SOON-1',
      'SOON-2',
    ]);
    assert.deepEqual(result.map((entry) => entry.risk), [
      RiskLevel.OVERDUE,
      RiskLevel.OVERDUE,
      RiskLevel.DUE_SOON,
      RiskLevel.DUE_SOON,
    ]);
  });

  test('includes the warning boundary and breaks ties by id', () => {
    const boundary = new Date(NOW.getTime() + 3 * DAY_IN_MS);
    const items = [
      item('B-2', WorkItemStatus.READY, boundary),
      item('B-1', WorkItemStatus.READY, boundary),
    ];

    const result = new RiskAnalyzer(items).findAtRisk(NOW, 3);

    assert.deepEqual(result.map((entry) => entry.item.id), ['B-1', 'B-2']);
  });

  test('rejects a negative warning window', () => {
    assert.throws(() => new RiskAnalyzer([]).findAtRisk(NOW, -1), RangeError);
  });
});