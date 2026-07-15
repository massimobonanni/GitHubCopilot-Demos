#!/usr/bin/env bash
# =============================================================================
# prompt-word-filter.sh — preSendMessage hook
# =============================================================================
# Runs BEFORE the user's prompt is sent to Copilot.
# Reads the message payload from stdin (JSON), checks if any blocked word
# appears in the prompt text, and either:
#   - Outputs {"permissionDecision":"deny", ...} to block the message, or
#   - Exits 0 with no output to allow the message to proceed.
#
# Install location: .github/hooks/scripts/prompt-word-filter.sh
# =============================================================================

set -euo pipefail

# ── Blocked words list ────────────────────────────────────────────────────────
# Add or remove words to match your organization's policy.
BLOCKED_WORDS=(
  "password"
  "secret"
  "api_key"
  "private_key"
  "credit_card"
  "ssn"
  "social_security"
)

# ── Read the full JSON payload from stdin ─────────────────────────────────────
INPUT=$(cat)

# ── Extract the user's message text ──────────────────────────────────────────
MESSAGE=$(echo "$INPUT" | python3 -c "
import sys, json
d = json.load(sys.stdin)
print(d.get('message') or d.get('prompt') or d.get('userMessage', ''))
" 2>/dev/null || echo "")

# ── Convert message to lowercase for case-insensitive matching ────────────────
MESSAGE_LOWER=$(echo "$MESSAGE" | tr '[:upper:]' '[:lower:]')

# ── Check for blocked words ───────────────────────────────────────────────────
for word in "${BLOCKED_WORDS[@]}"; do
  if echo "$MESSAGE_LOWER" | grep -qiw "$word"; then
    cat <<EOF
{"permissionDecision":"deny","permissionDecisionReason":"Blocked by prompt-word-filter hook: your prompt contains a prohibited word (\"$word\"). Please remove sensitive terms before sending."}
EOF
    exit 0
  fi
done

# ── Allow: no blocked words found ────────────────────────────────────────────
exit 0
