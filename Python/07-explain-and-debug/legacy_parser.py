"""
Demo 7 — Explain Code & Debug
===============================
This is "legacy" code that works but is hard to understand.
Use Copilot to explain it and then improve it.

Exercises:
  1. Select parse_log_entry() → Chat → "Explain this function step by step"
  2. Select the regex → Inline Chat → "What does this regex match?"
  3. Ask Chat: "Rewrite this using named groups and make it readable"
  4. Select analyze_log_file() → Chat → "This is slow for large files. Optimize it."
  5. Run the file and inspect the output.
"""

import re
from collections import Counter
from datetime import datetime


LOG_PATTERN = re.compile(
    r"(\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2})"  # timestamp
    r"\s+\[(\w+)\]"                               # log level
    r"\s+(\S+)"                                    # source module
    r"\s+-\s+(.*)"                                 # message
)


def parse_log_entry(line: str) -> dict | None:
    """Parse a single log line into structured data.
    Ask Copilot: 'Explain what this function does and how the regex works.'
    """
    m = LOG_PATTERN.match(line.strip())
    if not m:
        return None
    ts, level, source, message = m.groups()
    return {
        "timestamp": datetime.fromisoformat(ts),
        "level": level,
        "source": source,
        "message": message,
    }


def analyze_log_file(lines: list[str]) -> dict:
    """Analyze a list of log lines and produce statistics.
    Ask Copilot: 'This is O(n²) in a subtle way — can you find and fix it?'
    """
    entries = []
    for line in lines:
        parsed = parse_log_entry(line)
        if parsed:
            entries.append(parsed)

    # Count by level
    level_counts = Counter(e["level"] for e in entries)

    # Count by source
    source_counts = Counter(e["source"] for e in entries)

    # Find error messages
    errors = [e for e in entries if e["level"] == "ERROR"]

    # Time range
    timestamps = [e["timestamp"] for e in entries]
    time_range = None
    if timestamps:
        time_range = {
            "start": min(timestamps).isoformat(),
            "end": max(timestamps).isoformat(),
        }

    # ⚠️ Subtle issue: duplicate detection is O(n²)
    # Ask Copilot to spot and fix it.
    duplicates = []
    for i, entry in enumerate(entries):
        for j in range(i + 1, len(entries)):
            if entries[j]["message"] == entry["message"]:
                if entry["message"] not in duplicates:
                    duplicates.append(entry["message"])

    return {
        "total_entries": len(entries),
        "level_counts": dict(level_counts),
        "source_counts": dict(source_counts),
        "errors": [e["message"] for e in errors],
        "time_range": time_range,
        "duplicate_messages": duplicates,
    }


# ─────────────────────────────────────────────────────────────────────
# Sample log data for testing
# ─────────────────────────────────────────────────────────────────────
SAMPLE_LOGS = """
2026-06-08T09:00:01 [INFO] auth.service - User alice@example.com logged in
2026-06-08T09:00:03 [DEBUG] db.pool - Connection acquired from pool (active: 5)
2026-06-08T09:00:05 [INFO] api.orders - GET /api/orders — 200 OK (23ms)
2026-06-08T09:00:07 [WARN] api.orders - Slow query detected: 1200ms on orders table
2026-06-08T09:00:09 [ERROR] payment.gateway - Timeout connecting to stripe API
2026-06-08T09:00:12 [INFO] auth.service - User bob@example.com logged in
2026-06-08T09:00:15 [ERROR] payment.gateway - Timeout connecting to stripe API
2026-06-08T09:00:18 [INFO] api.orders - POST /api/orders — 201 Created (45ms)
2026-06-08T09:00:20 [DEBUG] db.pool - Connection released back to pool (active: 4)
2026-06-08T09:00:22 [ERROR] api.orders - Unhandled exception in order validation
""".strip().split("\n")


if __name__ == "__main__":
    report = analyze_log_file(SAMPLE_LOGS)
    print(f"Total entries: {report['total_entries']}")
    print(f"By level: {report['level_counts']}")
    print(f"By source: {report['source_counts']}")
    print(f"Errors: {report['errors']}")
    print(f"Time range: {report['time_range']}")
    print(f"Duplicates: {report['duplicate_messages']}")
