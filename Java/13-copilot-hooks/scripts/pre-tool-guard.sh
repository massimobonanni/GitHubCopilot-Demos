#!/usr/bin/env bash
# =============================================================================
# pre-tool-guard.sh — preToolUse hook
# =============================================================================
# Runs BEFORE every tool call the Copilot agent makes.
# Reads the tool payload from stdin (JSON), inspects the command, and either:
#   - Outputs {"permissionDecision":"deny", ...} to block the call, or
#   - Exits 0 with no output to allow the call to proceed.
#
# Install location: .github/hooks/scripts/pre-tool-guard.sh
# =============================================================================

set -euo pipefail

# ── Read the full JSON payload from stdin ─────────────────────────────────────
INPUT=$(cat)

# ── Extract tool name (handles both camelCase and snake_case payloads) ────────
TOOL_NAME=$(echo "$INPUT" | python3 -c "
import sys, json
d = json.load(sys.stdin)
print(d.get('toolName') or d.get('tool_name', ''))
" 2>/dev/null || echo "")

# ── Only inspect bash / powershell tool calls ─────────────────────────────────
if [[ "$TOOL_NAME" == "bash" || "$TOOL_NAME" == "powershell" ]]; then

  # Extract the command string from toolArgs / tool_input
  COMMAND=$(echo "$INPUT" | python3 -c "
import sys, json
d = json.load(sys.stdin)
args = d.get('toolArgs') or d.get('tool_input') or {}
if isinstance(args, dict):
    print(args.get('command', '') or args.get('cmd', ''))
elif isinstance(args, str):
    print(args)
" 2>/dev/null || echo "")

  # ── Blocked patterns ─────────────────────────────────────────────────────────
  # Add or remove patterns to match your team's security policy.
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
      # Output deny decision — agent sees the reason in its context
      python3 -c "
import json, sys
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

# ── Allow: log the approved tool call and exit cleanly ───────────────────────
LOG_DIR="logs"
mkdir -p "$LOG_DIR"

TIMESTAMP=$(date -u +"%Y-%m-%dT%H:%M:%SZ" 2>/dev/null || echo "unknown")

python3 -c "
import json, sys
record = {
    'event': 'preToolUse',
    'timestamp': '$TIMESTAMP',
    'tool': '$TOOL_NAME',
    'decision': 'allow'
}
print(json.dumps(record))
" >> "$LOG_DIR/tool-audit.jsonl" 2>/dev/null || true

exit 0
