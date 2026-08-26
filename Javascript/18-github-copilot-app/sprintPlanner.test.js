'use strict';

const assert = require('node:assert/strict');
const { describe, test } = require('node:test');
const { SprintPlanner } = require('./sprintPlanner');
const { WorkItemPriority, WorkItemStatus } = require('./workItems');

const START = new Date('2026-08-01T09:00:00Z');

function item(id, priority, status, estimate, ageInHours) {
  return {
    id,
    title: `Item ${id}`,
    priority,
    status,
    estimate,
    createdAt: new Date(START.getTime() + ageInHours * 60 * 60 * 1000),
    dueAt: null,
  };
}

describe('SprintPlanner', () => {
  test('filters, orders, and uses available capacity', () => {
    const items = [
      item('LOW-1', WorkItemPriority.LOW, WorkItemStatus.READY, 2, 0),
      item('CRIT-1', WorkItemPriority.CRITICAL, WorkItemStatus.READY, 5, 3),
      item('HIGH-2', WorkItemPriority.HIGH, WorkItemStatus.READY, 4, 2),
      item('HIGH-1', WorkItemPriority.HIGH, WorkItemStatus.READY, 3, 1),
      item('DONE-1', WorkItemPriority.CRITICAL, WorkItemStatus.DONE, 1, 0),
      item('ZERO-1', WorkItemPriority.CRITICAL, WorkItemStatus.READY, 0, 0),
    ];

    const result = new SprintPlanner(items).buildPlan(10);

    assert.deepEqual(result.items.map((entry) => entry.id), [
      'CRIT-1',
      'HIGH-1',
      'LOW-1',
    ]);
    assert.equal(result.totalEstimate, 10);
  });

  test('breaks equal ties by case-sensitive id', () => {
    const items = [
      item('item-2', WorkItemPriority.NORMAL, WorkItemStatus.READY, 1, 0),
      item('Item-1', WorkItemPriority.NORMAL, WorkItemStatus.READY, 1, 0),
    ];

    const result = new SprintPlanner(items).buildPlan(2);

    assert.deepEqual(result.items.map((entry) => entry.id), ['Item-1', 'item-2']);
  });

  test('rejects negative capacity', () => {
    assert.throws(() => new SprintPlanner([]).buildPlan(-1), RangeError);
  });
});