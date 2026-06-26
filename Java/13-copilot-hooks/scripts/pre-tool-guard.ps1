# =============================================================================
# pre-tool-guard.ps1 — preToolUse hook (Windows / PowerShell)
# =============================================================================
# Runs BEFORE every tool call the Copilot agent makes.
# Reads the tool payload from stdin (JSON), inspects the command, and either:
#   - Outputs a deny JSON object to block the call, or
#   - Exits 0 with no output to allow the call to proceed.
#
# Install location: .github/hooks/scripts/pre-tool-guard.ps1
# =============================================================================

$ErrorActionPreference = 'Stop'

# ── Read JSON payload from stdin ──────────────────────────────────────────────
$raw = $input -join "`n"
if (-not $raw) { $raw = [Console]::In.ReadToEnd() }

$payload = $raw | ConvertFrom-Json

# ── Extract tool name ─────────────────────────────────────────────────────────
$toolName = if ($payload.toolName) { $payload.toolName }
            elseif ($payload.tool_name) { $payload.tool_name }
            else { '' }

# ── Only inspect bash / powershell tool calls ─────────────────────────────────
if ($toolName -in @('bash', 'powershell')) {

    $toolArgs = if ($payload.toolArgs) { $payload.toolArgs }
                elseif ($payload.tool_input) { $payload.tool_input }
                else { $null }

    $command = if ($toolArgs -is [string]) { $toolArgs }
               elseif ($toolArgs.command)  { $toolArgs.command }
               elseif ($toolArgs.cmd)      { $toolArgs.cmd }
               else                        { '' }

    # ── Blocked patterns ───────────────────────────────────────────────────────
    $blockedPatterns = @(
        'rm -rf /',
        'rm -rf ~',
        'Remove-Item -Recurse -Force C:\',
        'Remove-Item -Recurse -Force ~',
        'git push --force',
        'git push -f ',
        'Format-Volume',
        'Clear-Disk'
    )

    foreach ($pattern in $blockedPatterns) {
        if ($command -ilike "*$pattern*") {
            $deny = @{
                permissionDecision       = 'deny'
                permissionDecisionReason = (
                    "Blocked by pre-tool-guard hook: the command matches a prohibited " +
                    "pattern (`"$pattern`"). Review the command and run it manually " +
                    "if it is intentional."
                )
            } | ConvertTo-Json -Compress
            Write-Output $deny
            exit 0
        }
    }
}

# ── Allow: log the approved tool call ────────────────────────────────────────
$logDir = 'logs'
if (-not (Test-Path $logDir)) { New-Item -ItemType Directory -Path $logDir | Out-Null }

$record = @{
    event     = 'preToolUse'
    timestamp = (Get-Date).ToUniversalTime().ToString('yyyy-MM-ddTHH:mm:ssZ')
    tool      = $toolName
    decision  = 'allow'
} | ConvertTo-Json -Compress

Add-Content -Path "$logDir\tool-audit.jsonl" -Value $record -Encoding UTF8

exit 0
