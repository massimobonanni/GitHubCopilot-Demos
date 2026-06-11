#!/usr/bin/env bash
# audit-logger.sh — postToolUse hook
# Install location: .github/hooks/scripts/audit-logger.sh

set -euo pipefail

INPUT=$(cat)
LOG_DIR="logs"
mkdir -p "$LOG_DIR"
TIMESTAMP=$(date -u +"%Y-%m-%dT%H:%M:%SZ" 2>/dev/null || echo "unknown")

python3 -c "
import sys, json
d = json.load(sys.stdin)
tool = d.get('toolName') or d.get('tool_name', 'unknown')
result = d.get('toolResult') or d.get('tool_result') or {}
result_type = result.get('resultType') or result.get('result_type', 'unknown') if isinstance(result, dict) else 'unknown'
text = result.get('textResultForLlm') or result.get('text_result_for_llm', '') if isinstance(result, dict) else ''
record = {
    'event': 'postToolUse',
    'timestamp': '$TIMESTAMP',
    'tool': tool,
    'resultType': result_type,
    'preview': text[:200].replace('\n', ' ') if text else ''
}
print(json.dumps(record))
" <<< "\$INPUT" >> "\$LOG_DIR/tool-audit.jsonl" 2>/dev/null || \
echo "{\"event\":\"postToolUse\",\"timestamp\":\"$TIMESTAMP\",\"tool\":\"unknown\"}" >> "$LOG_DIR/tool-audit.jsonl"

exit 0
