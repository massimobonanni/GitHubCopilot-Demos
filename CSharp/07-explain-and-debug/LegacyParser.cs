// Demo 7 — Explain Code & Debug
// ===============================
// This is "legacy" code that works but is hard to understand.
// Use Copilot to explain it and then improve it.
//
// Exercises:
//   1. Select ParseLogEntry() → Chat → "Explain this method step by step"
//   2. Select the regex → Inline Chat → "What does this regex match?"
//   3. Ask Chat: "Rewrite this using named groups and make it readable"
//   4. Select AnalyzeLogFile() → Chat → "This is slow for large files. Find the issue."
//   5. Run the file and inspect the output.

using System.Text.RegularExpressions;

namespace ExplainAndDebug;

public class LogAnalyzer
{
    private static readonly Regex LogPattern = new(
        @"(\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2})"   // timestamp
      + @"\s+\[(\w+)\]"                                // log level
      + @"\s+(\S+)"                                     // source module
      + @"\s+-\s+(.*)",                                 // message
        RegexOptions.Compiled
    );

    public LogEntry? ParseLogEntry(string line)
    {
        // Ask Copilot: "Explain what this method does and how the regex works."
        var match = LogPattern.Match(line.Trim());
        if (!match.Success) return null;

        return new LogEntry
        {
            Timestamp = DateTime.Parse(match.Groups[1].Value),
            Level = match.Groups[2].Value,
            Source = match.Groups[3].Value,
            Message = match.Groups[4].Value
        };
    }

    public LogReport AnalyzeLogFile(IEnumerable<string> lines)
    {
        // Ask Copilot: "This is O(n²) in a subtle way — can you find and fix it?"
        var entries = new List<LogEntry>();
        foreach (var line in lines)
        {
            var parsed = ParseLogEntry(line);
            if (parsed is not null)
                entries.Add(parsed);
        }

        // Count by level
        var levelCounts = entries
            .GroupBy(e => e.Level)
            .ToDictionary(g => g.Key, g => g.Count());

        // Count by source
        var sourceCounts = entries
            .GroupBy(e => e.Source)
            .ToDictionary(g => g.Key, g => g.Count());

        // Find error messages
        var errors = entries
            .Where(e => e.Level == "ERROR")
            .Select(e => e.Message)
            .ToList();

        // Time range
        var timestamps = entries.Select(e => e.Timestamp).ToList();
        var timeRange = timestamps.Count > 0
            ? (Start: timestamps.Min(), End: timestamps.Max())
            : ((DateTime Start, DateTime End)?)null;

        // ⚠️ Subtle issue: duplicate detection is O(n²)
        // Ask Copilot to spot and fix it.
        var duplicates = new List<string>();
        for (int i = 0; i < entries.Count; i++)
        {
            for (int j = i + 1; j < entries.Count; j++)
            {
                if (entries[j].Message == entries[i].Message)
                {
                    if (!duplicates.Contains(entries[i].Message))
                        duplicates.Add(entries[i].Message);
                }
            }
        }

        return new LogReport
        {
            TotalEntries = entries.Count,
            LevelCounts = levelCounts,
            SourceCounts = sourceCounts,
            Errors = errors,
            TimeRange = timeRange,
            DuplicateMessages = duplicates
        };
    }
}

public class LogEntry
{
    public DateTime Timestamp { get; init; }
    public string Level { get; init; } = "";
    public string Source { get; init; } = "";
    public string Message { get; init; } = "";
}

public class LogReport
{
    public int TotalEntries { get; init; }
    public Dictionary<string, int> LevelCounts { get; init; } = new();
    public Dictionary<string, int> SourceCounts { get; init; } = new();
    public List<string> Errors { get; init; } = new();
    public (DateTime Start, DateTime End)? TimeRange { get; init; }
    public List<string> DuplicateMessages { get; init; } = new();
}

// ─────────────────────────────────────────────────────────────────────
// Sample log data for testing
// ─────────────────────────────────────────────────────────────────────
public class Program
{
    private static readonly string[] SampleLogs =
    [
        "2026-06-10T09:00:01 [INFO] auth.service - User alice@example.com logged in",
        "2026-06-10T09:00:03 [DEBUG] db.pool - Connection acquired from pool (active: 5)",
        "2026-06-10T09:00:05 [INFO] api.orders - GET /api/orders — 200 OK (23ms)",
        "2026-06-10T09:00:07 [WARN] api.orders - Slow query detected: 1200ms on orders table",
        "2026-06-10T09:00:09 [ERROR] payment.gateway - Timeout connecting to Stripe API",
        "2026-06-10T09:00:12 [INFO] auth.service - User bob@example.com logged in",
        "2026-06-10T09:00:15 [ERROR] payment.gateway - Timeout connecting to Stripe API",
        "2026-06-10T09:00:18 [INFO] api.orders - POST /api/orders — 201 Created (45ms)",
        "2026-06-10T09:00:20 [DEBUG] db.pool - Connection released back to pool (active: 4)",
        "2026-06-10T09:00:22 [ERROR] api.orders - Unhandled exception in order validation",
    ];

    public static void Main()
    {
        var analyzer = new LogAnalyzer();
        var report = analyzer.AnalyzeLogFile(SampleLogs);

        Console.WriteLine($"Total entries: {report.TotalEntries}");
        Console.WriteLine($"By level: {string.Join(", ", report.LevelCounts.Select(kv => $"{kv.Key}={kv.Value}"))}");
        Console.WriteLine($"By source: {string.Join(", ", report.SourceCounts.Select(kv => $"{kv.Key}={kv.Value}"))}");
        Console.WriteLine($"Errors: {string.Join("; ", report.Errors)}");
        if (report.TimeRange.HasValue)
            Console.WriteLine($"Time range: {report.TimeRange.Value.Start:s} → {report.TimeRange.Value.End:s}");
        Console.WriteLine($"Duplicates: {string.Join("; ", report.DuplicateMessages)}");
    }
}
