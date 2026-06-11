#!/usr/bin/env bash
# =============================================================================
# session-logger.sh — sessionStart / sessionEnd hook
# =============================================================================
# Logs when a Copilot agent session starts or ends.
# Receives the session payload on stdin and appends a line to logs/sessions.log.
#
# Install location: .github/hooks/scripts/session-logger.sh
# =============================================================================

set -euo pipefail

INPUT=$(cat)

LOG_DIR="logs"
mkdir -p "$LOG_DIR"

TIMESTAMP=$(date -u +"%Y-%m-%dT%H:%M:%SZ" 2>/dev/null || echo "unknown")

# Extract fields from the JSON payload (handles both camelCase and snake_case)
SESSION_ID=$(echo "$INPUT" | python3 -c "
import sys, json
d = json.load(sys.stdin)
print(d.get('sessionId') or d.get('session_id', 'unknown'))
" 2>/dev/null || echo "unknown")

EVENT=$(echo "$INPUT" | python3 -c "
import sys, json
d = json.load(sys.stdin)
# Detect event type: sessionEnd payload has a 'reason' field
print('sessionEnd' if 'reason' in d else 'sessionStart')
" 2>/dev/null || echo "sessionStart")

python3 -c "
import json
record = {
    'event':     '$EVENT',
    'timestamp': '$TIMESTAMP',
    'sessionId': '$SESSION_ID',
}
print(json.dumps(record))
" >> "$LOG_DIR/sessions.log" 2>/dev/null || \
echo "{\"event\":\"$EVENT\",\"timestamp\":\"$TIMESTAMP\",\"sessionId\":\"$SESSION_ID\"}" >> "$LOG_DIR/sessions.log"

exit 0
