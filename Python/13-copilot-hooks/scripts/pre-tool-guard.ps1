# pre-tool-guard.ps1 — preToolUse hook (Windows / PowerShell)
# Install location: .github/hooks/scripts/pre-tool-guard.ps1

$ErrorActionPreference = 'Stop'
$raw = $input -join "`n"
if (-not $raw) { $raw = [Console]::In.ReadToEnd() }
$payload = $raw | ConvertFrom-Json

$toolName = if ($payload.toolName) { $payload.toolName } elseif ($payload.tool_name) { $payload.tool_name } else { '' }

if ($toolName -in @('bash', 'powershell')) {
    $args = if ($payload.toolArgs) { $payload.toolArgs } elseif ($payload.tool_input) { $payload.tool_input } else { $null }
    $command = if ($args -is [string]) { $args } elseif ($args.command) { $args.command } elseif ($args.cmd) { $args.cmd } else { '' }

    $blockedPatterns = @(
        'rm -rf /', 'rm -rf ~', 'Remove-Item -Recurse -Force C:\',
        'Remove-Item -Recurse -Force ~', 'git push --force', 'git push -f ',
        'Format-Volume', 'Clear-Disk'
    )

    foreach ($pattern in $blockedPatterns) {
        if ($command -ilike "*$pattern*") {
            @{
                permissionDecision       = 'deny'
                permissionDecisionReason = "Blocked by pre-tool-guard hook: matches prohibited pattern (`"$pattern`")."
            } | ConvertTo-Json -Compress | Write-Output
            exit 0
        }
    }
}

$logDir = 'logs'
if (-not (Test-Path $logDir)) { New-Item -ItemType Directory -Path $logDir | Out-Null }
@{ event='preToolUse'; timestamp=(Get-Date).ToUniversalTime().ToString('yyyy-MM-ddTHH:mm:ssZ'); tool=$toolName; decision='allow' } |
    ConvertTo-Json -Compress | Add-Content -Path "$logDir\tool-audit.jsonl" -Encoding UTF8
exit 0
