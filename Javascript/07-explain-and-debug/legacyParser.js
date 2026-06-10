/**
 * Demo 7 — Explain Code & Debug
 * ===============================
 * This is "legacy" code that works but is hard to understand.
 * Use Copilot to explain it and then improve it.
 *
 * Exercises:
 *   1. Select parseLogEntry() → Chat → "Explain this function step by step"
 *   2. Select the regex → Inline Chat → "What does this regex match?"
 *   3. Ask Chat: "Rewrite this using named groups and make it readable"
 *   4. Select analyzeLogFile() → Chat → "This is slow for large files. Find the issue."
 *   5. Run: node legacyParser.js
 */

const LOG_PATTERN =
  /(\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2})\s+\[(\w+)\]\s+(\S+)\s+-\s+(.*)/;

/**
 * Parse a single log line into structured data.
 * Ask Copilot: "Explain what this function does and how the regex works."
 */
function parseLogEntry(line) {
  const match = line.trim().match(LOG_PATTERN);
  if (!match) return null;

  return {
    timestamp: new Date(match[1]),
    level: match[2],
    source: match[3],
    message: match[4],
  };
}

/**
 * Analyze an array of log lines and produce statistics.
 * Ask Copilot: "This is O(n²) in a subtle way — can you find and fix it?"
 */
function analyzeLogFile(lines) {
  const entries = [];
  for (const line of lines) {
    const parsed = parseLogEntry(line);
    if (parsed) entries.push(parsed);
  }

  // Count by level
  const levelCounts = {};
  for (const e of entries) {
    levelCounts[e.level] = (levelCounts[e.level] || 0) + 1;
  }

  // Count by source
  const sourceCounts = {};
  for (const e of entries) {
    sourceCounts[e.source] = (sourceCounts[e.source] || 0) + 1;
  }

  // Find error messages
  const errors = entries.filter((e) => e.level === "ERROR").map((e) => e.message);

  // Time range
  const timestamps = entries.map((e) => e.timestamp);
  let timeRange = null;
  if (timestamps.length > 0) {
    timeRange = {
      start: new Date(Math.min(...timestamps)).toISOString(),
      end: new Date(Math.max(...timestamps)).toISOString(),
    };
  }

  // ⚠️ Subtle issue: duplicate detection is O(n²)
  // Ask Copilot to spot and fix it.
  const duplicates = [];
  for (let i = 0; i < entries.length; i++) {
    for (let j = i + 1; j < entries.length; j++) {
      if (entries[j].message === entries[i].message) {
        if (!duplicates.includes(entries[i].message)) {
          duplicates.push(entries[i].message);
        }
      }
    }
  }

  return {
    totalEntries: entries.length,
    levelCounts,
    sourceCounts,
    errors,
    timeRange,
    duplicateMessages: duplicates,
  };
}

// ─────────────────────────────────────────────────────────────────────
// Sample log data for testing
// ─────────────────────────────────────────────────────────────────────
const SAMPLE_LOGS = [
  "2026-06-10T09:00:01 [INFO] auth.service - User alice@example.com logged in",
  "2026-06-10T09:00:03 [DEBUG] db.pool - Connection acquired from pool (active: 5)",
  "2026-06-10T09:00:05 [INFO] api.orders - GET /api/orders — 200 OK (23ms)",
  "2026-06-10T09:00:07 [WARN] api.orders - Slow query detected: 1200ms on orders table",
  "2026-06-10T09:00:09 [ERROR] payment.gateway - Timeout connecting to Stripe API",
  "2026-06-10T09:00:12 [INFO] auth.service - User bob@example.com logged in",
  "2026-06-10T09:00:15 [ERROR] payment.gateway - Timeout connecting to Stripe API",
  "2026-06-10T09:00:18 [INFO] api.orders - POST /api/orders — 201 Created (45ms)",
  "2026-06-10T09:00:20 [DEBUG] db.pool - Connection released back to pool (active: 4)",
  "2026-06-10T09:00:22 [ERROR] api.orders - Unhandled exception in order validation",
];

const report = analyzeLogFile(SAMPLE_LOGS);
console.log(`Total entries: ${report.totalEntries}`);
console.log("By level:", report.levelCounts);
console.log("By source:", report.sourceCounts);
console.log("Errors:", report.errors);
console.log("Time range:", report.timeRange);
console.log("Duplicates:", report.duplicateMessages);
