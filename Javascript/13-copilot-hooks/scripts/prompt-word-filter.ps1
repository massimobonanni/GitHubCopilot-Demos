# =============================================================================
# prompt-word-filter.ps1 — preSendMessage hook (Windows / PowerShell)
# =============================================================================
# Runs BEFORE the user's prompt is sent to Copilot.
# Reads the message payload from stdin (JSON), checks if any blocked word
# appears in the prompt text, and either:
#   - Outputs a deny JSON object to block the message, or
#   - Exits 0 with no output to allow the message to proceed.
#
# Install location: .github/hooks/scripts/prompt-word-filter.ps1
# =============================================================================

$ErrorActionPreference = 'Stop'

# ── Blocked words list ────────────────────────────────────────────────────────
# Add or remove words to match your organization's policy.
$blockedWords = @(
    'password',
    'secret',
    'api_key',
    'private_key',
    'credit_card',
    'ssn',
    'social_security'
)

# ── Read JSON payload from stdin ──────────────────────────────────────────────
$raw = $input -join "`n"
if (-not $raw) { $raw = [Console]::In.ReadToEnd() }

$payload = $raw | ConvertFrom-Json

# ── Extract the user's message text ──────────────────────────────────────────
$message = if ($payload.message) { $payload.message }
           elseif ($payload.prompt) { $payload.prompt }
           elseif ($payload.userMessage) { $payload.userMessage }
           else { '' }

# ── Check for blocked words (case-insensitive) ───────────────────────────────
foreach ($word in $blockedWords) {
    if ($message -imatch "\b$([regex]::Escape($word))\b") {
        $deny = @{
            permissionDecision       = 'deny'
            permissionDecisionReason = (
                "Blocked by prompt-word-filter hook: your prompt contains a " +
                "prohibited word (`"$word`"). Please remove sensitive terms " +
                "before sending."
            )
        } | ConvertTo-Json -Compress
        Write-Output $deny
        exit 0
    }
}

# ── Allow: no blocked words found ────────────────────────────────────────────
exit 0
