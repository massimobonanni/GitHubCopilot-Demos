'use strict';

const assert = require('node:assert/strict');
const { describe, test } = require('node:test');
const { TicketPriority, TicketQueue } = require('./issueDesk');

const START = new Date('2026-08-01T09:00:00Z');

function ticket(id, priority, ageInHours, owner) {
  return {
    id,
    title: `Ticket ${id}`,
    priority,
    createdAt: new Date(START.getTime() + ageInHours * 60 * 60 * 1000),
    owner,
  };
}

describe('TicketQueue', () => {
  test('orders by priority, age, then id', () => {
    const tickets = [
      ticket('LOW-1', TicketPriority.LOW, 0, 'Lee'),
      ticket('HIGH-2', TicketPriority.HIGH, 2, 'Sam'),
      ticket('CRIT-1', TicketPriority.CRITICAL, 3, null),
      ticket('HIGH-1', TicketPriority.HIGH, 1, 'Sam'),
      ticket('HIGH-0', TicketPriority.HIGH, 1, 'Lee'),
    ];

    const result = new TicketQueue(tickets).orderForTriage();

    assert.deepEqual(
      result.map((item) => item.id),
      ['CRIT-1', 'HIGH-0', 'HIGH-1', 'HIGH-2', 'LOW-1'],
    );
  });

  test('ordering does not mutate the input', () => {
    const tickets = [
      ticket('LOW-1', TicketPriority.LOW, 0, 'Lee'),
      ticket('CRIT-1', TicketPriority.CRITICAL, 1, 'Sam'),
    ];

    new TicketQueue(tickets).orderForTriage();

    assert.deepEqual(tickets.map((item) => item.id), ['LOW-1', 'CRIT-1']);
  });

  test('groups owners and sorts by count', () => {
    const tickets = [
      ticket('T-1', TicketPriority.NORMAL, 0, 'Sam'),
      ticket('T-2', TicketPriority.NORMAL, 1, 'sam'),
      ticket('T-3', TicketPriority.NORMAL, 2, 'Lee'),
      ticket('T-4', TicketPriority.NORMAL, 3, 'Alex'),
      ticket('T-5', TicketPriority.NORMAL, 4, 'Alex'),
    ];

    assert.deepEqual(new TicketQueue(tickets).getOwnerWorkload(), [
      { owner: 'Alex', ticketCount: 2 },
      { owner: 'Sam', ticketCount: 2 },
      { owner: 'Lee', ticketCount: 1 },
    ]);
  });

  for (const owner of [null, '', '   ']) {
    test(`maps ${JSON.stringify(owner)} owner to Unassigned`, () => {
      const queue = new TicketQueue([
        ticket('T-1', TicketPriority.NORMAL, 0, owner),
      ]);

      assert.deepEqual(queue.getOwnerWorkload(), [
        { owner: 'Unassigned', ticketCount: 1 },
      ]);
    });
  }

  test('returns empty results for an empty queue', () => {
    const queue = new TicketQueue([]);

    assert.deepEqual(queue.orderForTriage(), []);
    assert.deepEqual(queue.getOwnerWorkload(), []);
  });
});
