#!/usr/bin/env bash
# pre-tool-guard.sh — preToolUse hook
# Install location: .github/hooks/scripts/pre-tool-guard.sh

set -euo pipefail

INPUT=$(cat)

TOOL_NAME=$(echo "$INPUT" | python3 -c "
import sys, json
d = json.load(sys.stdin)
print(d.get('toolName') or d.get('tool_name', ''))
" 2>/dev/null || echo "")

if [[ "$TOOL_NAME" == "bash" || "$TOOL_NAME" == "powershell" ]]; then

  COMMAND=$(echo "$INPUT" | python3 -c "
import sys, json
d = json.load(sys.stdin)
args = d.get('toolArgs') or d.get('tool_input') or {}
if isinstance(args, dict):
    print(args.get('command', '') or args.get('cmd', ''))
elif isinstance(args, str):
    print(args)
" 2>/dev/null || echo "")

  BLOCKED_PATTERNS=(
    "rm -rf /"
    "rm -rf ~"
    "rm -rf \$HOME"
    "git push --force"
    "git push -f "
    "git push -f$"
    "> /dev/sda"
    "dd if="
    "mkfs"
    ":(){ :|:& };:"
  )

  for pattern in "${BLOCKED_PATTERNS[@]}"; do
    if echo "$COMMAND" | grep -qiF "$pattern"; then
      python3 -c "
import json
print(json.dumps({
    'permissionDecision': 'deny',
    'permissionDecisionReason': (
        'Blocked by pre-tool-guard hook: the command matches a prohibited '
        'pattern (\"${pattern}\"). Review the command and run it manually '
        'if it is intentional.'
    )
}))
"
      exit 0
    fi
  done
fi

LOG_DIR="logs"
mkdir -p "$LOG_DIR"
TIMESTAMP=$(date -u +"%Y-%m-%dT%H:%M:%SZ" 2>/dev/null || echo "unknown")

python3 -c "
import json
print(json.dumps({'event':'preToolUse','timestamp':'$TIMESTAMP','tool':'$TOOL_NAME','decision':'allow'}))
" >> "$LOG_DIR/tool-audit.jsonl" 2>/dev/null || true

exit 0
