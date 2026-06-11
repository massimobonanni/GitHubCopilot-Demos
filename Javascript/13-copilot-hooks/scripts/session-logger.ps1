# session-logger.ps1 — sessionStart / sessionEnd hook (Windows / PowerShell)
# Install location: .github/hooks/scripts/session-logger.ps1

$ErrorActionPreference = 'Stop'

$raw = $input -join "`n"
if (-not $raw) { $raw = [Console]::In.ReadToEnd() }
$payload = $raw | ConvertFrom-Json

$logDir = 'logs'
if (-not (Test-Path $logDir)) { New-Item -ItemType Directory -Path $logDir | Out-Null }

$sessionId = if ($payload.sessionId) { $payload.sessionId }
             elseif ($payload.session_id) { $payload.session_id }
             else { 'unknown' }

$event = if ($payload.PSObject.Properties['reason']) { 'sessionEnd' } else { 'sessionStart' }

$record = @{
    event     = $event
    timestamp = (Get-Date).ToUniversalTime().ToString('yyyy-MM-ddTHH:mm:ssZ')
    sessionId = $sessionId
} | ConvertTo-Json -Compress

Add-Content -Path "$logDir\sessions.log" -Value $record -Encoding UTF8
exit 0
