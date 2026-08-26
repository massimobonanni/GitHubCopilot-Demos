'use strict';

const WorkItemPriority = Object.freeze({
  LOW: 'low',
  NORMAL: 'normal',
  HIGH: 'high',
  CRITICAL: 'critical',
});

const WorkItemStatus = Object.freeze({
  DRAFT: 'draft',
  READY: 'ready',
  IN_PROGRESS: 'in_progress',
  DONE: 'done',
});

const RiskLevel = Object.freeze({
  DUE_SOON: 'due_soon',
  OVERDUE: 'overdue',
});

module.exports = { RiskLevel, WorkItemPriority, WorkItemStatus };