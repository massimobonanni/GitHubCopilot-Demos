# audit-logger.ps1 — postToolUse hook (Windows / PowerShell)
# Install location: .github/hooks/scripts/audit-logger.ps1

$ErrorActionPreference = 'Stop'

$raw = $input -join "`n"
if (-not $raw) { $raw = [Console]::In.ReadToEnd() }
$payload = $raw | ConvertFrom-Json

$logDir = 'logs'
if (-not (Test-Path $logDir)) { New-Item -ItemType Directory -Path $logDir | Out-Null }

$toolName = if ($payload.toolName) { $payload.toolName } elseif ($payload.tool_name) { $payload.tool_name } else { 'unknown' }
$result = if ($payload.toolResult) { $payload.toolResult } elseif ($payload.tool_result) { $payload.tool_result } else { $null }
$resultType = if ($result -and $result.resultType) { $result.resultType } elseif ($result -and $result.result_type) { $result.result_type } else { 'unknown' }
$text = if ($result -and $result.textResultForLlm) { $result.textResultForLlm } elseif ($result -and $result.text_result_for_llm) { $result.text_result_for_llm } else { '' }
$preview = if ($text.Length -gt 200) { $text.Substring(0, 200) -replace "`n", ' ' } else { $text -replace "`n", ' ' }

$record = @{
    event      = 'postToolUse'
    timestamp  = (Get-Date).ToUniversalTime().ToString('yyyy-MM-ddTHH:mm:ssZ')
    tool       = $toolName
    resultType = $resultType
    preview    = $preview
} | ConvertTo-Json -Compress

Add-Content -Path "$logDir\tool-audit.jsonl" -Value $record -Encoding UTF8
exit 0
