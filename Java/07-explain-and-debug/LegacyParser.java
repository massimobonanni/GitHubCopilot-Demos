import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Demo 7 — Explain Code & Debug (Refactored to Java)
 * ====================================================
 * This is "legacy" code that works but is hard to understand.
 * Use Copilot to explain it and then improve it.
 *
 * Exercises:
 *   1. Select parseLogEntry() → Chat → "Explain this function step by step"
 *   2. Select the regex pattern → Inline Chat → "What does this regex match?"
 *   3. Ask Chat: "Rewrite this using named groups and make it readable"
 *   4. Select analyzeLogFile() → Chat → "This is slow for large files. Optimize it."
 *   5. Run the file and inspect the output.
 */

/**
 * Represents a single log entry with structured data.
 */
class LogEntry {
    private LocalDateTime timestamp;
    private String level;
    private String source;
    private String message;

    public LogEntry(LocalDateTime timestamp, String level, String source, String message) {
        this.timestamp = timestamp;
        this.level = level;
        this.source = source;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getLevel() {
        return level;
    }

    public String getSource() {
        return source;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "timestamp=" + timestamp +
                ", level='" + level + '\'' +
                ", source='" + source + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

/**
 * Log analysis result containing statistics about the log entries.
 */
class LogAnalysisReport {
    private int totalEntries;
    private Map<String, Integer> levelCounts;
    private Map<String, Integer> sourceCounts;
    private List<String> errors;
    private TimeRange timeRange;
    private List<String> duplicateMessages;

    public LogAnalysisReport(
        int totalEntries,
        Map<String, Integer> levelCounts,
        Map<String, Integer> sourceCounts,
        List<String> errors,
        TimeRange timeRange,
        List<String> duplicateMessages) {
        this.totalEntries = totalEntries;
        this.levelCounts = levelCounts;
        this.sourceCounts = sourceCounts;
        this.errors = errors;
        this.timeRange = timeRange;
        this.duplicateMessages = duplicateMessages;
    }

    public int getTotalEntries() {
        return totalEntries;
    }

    public Map<String, Integer> getLevelCounts() {
        return levelCounts;
    }

    public Map<String, Integer> getSourceCounts() {
        return sourceCounts;
    }

    public List<String> getErrors() {
        return errors;
    }

    public TimeRange getTimeRange() {
        return timeRange;
    }

    public List<String> getDuplicateMessages() {
        return duplicateMessages;
    }

    @Override
    public String toString() {
        return "LogAnalysisReport{" +
                "totalEntries=" + totalEntries +
                ", levelCounts=" + levelCounts +
                ", sourceCounts=" + sourceCounts +
                ", errors=" + errors +
                ", timeRange=" + timeRange +
                ", duplicateMessages=" + duplicateMessages +
                '}';
    }
}

/**
 * Represents a time range with start and end timestamps.
 */
class TimeRange {
    private String start;
    private String end;

    public TimeRange(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "TimeRange{" +
                "start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }
}

public class LegacyParser {

    private static final Pattern LOG_PATTERN = Pattern.compile(
        "(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})" +  // timestamp
        "\\s+\\[(\\w+)\\]" +                             // log level
        "\\s+(\\S+)" +                                    // source module
        "\\s+-\\s+(.*)"                                   // message
    );

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Parse a single log line into structured data.
     * Ask Copilot: 'Explain what this function does and how the regex works.'
     *
     * @param line the log line to parse
     * @return a LogEntry if parsing succeeds, null otherwise
     */
    public static LogEntry parseLogEntry(String line) {
        Matcher m = LOG_PATTERN.matcher(line.strip());
        if (!m.matches()) {
            return null;
        }

        String timestamp = m.group(1);
        String level = m.group(2);
        String source = m.group(3);
        String message = m.group(4);

        LocalDateTime ts = LocalDateTime.parse(timestamp, TIMESTAMP_FORMATTER);
        return new LogEntry(ts, level, source, message);
    }

    /**
     * Analyze a list of log lines and produce statistics.
     * Ask Copilot: 'This has an O(n²) duplicate detection — can you find and fix it?'
     *
     * @param lines the list of log lines to analyze
     * @return a LogAnalysisReport with statistics
     */
    public static LogAnalysisReport analyzeLogFile(List<String> lines) {
        // Parse all log entries
        List<LogEntry> entries = lines.stream()
            .map(LegacyParser::parseLogEntry)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        // Count by level
        Map<String, Integer> levelCounts = new HashMap<>();
        for (LogEntry e : entries) {
            levelCounts.merge(e.getLevel(), 1, Integer::sum);
        }

        // Count by source
        Map<String, Integer> sourceCounts = new HashMap<>();
        for (LogEntry e : entries) {
            sourceCounts.merge(e.getSource(), 1, Integer::sum);
        }

        // Find error messages
        List<String> errors = entries.stream()
            .filter(e -> "ERROR".equals(e.getLevel()))
            .map(LogEntry::getMessage)
            .collect(Collectors.toList());

        // Time range
        TimeRange timeRange = null;
        if (!entries.isEmpty()) {
            LocalDateTime minTime = entries.stream()
                .map(LogEntry::getTimestamp)
                .min(LocalDateTime::compareTo)
                .orElse(null);
            LocalDateTime maxTime = entries.stream()
                .map(LogEntry::getTimestamp)
                .max(LocalDateTime::compareTo)
                .orElse(null);
            if (minTime != null && maxTime != null) {
                timeRange = new TimeRange(minTime.toString(), maxTime.toString());
            }
        }

        // ⚠️ Subtle issue: duplicate detection is O(n²)
        // Ask Copilot to spot and fix it.
        Set<String> duplicateSet = new HashSet<>();
        Set<String> seenMessages = new HashSet<>();
        for (LogEntry entry : entries) {
            String msg = entry.getMessage();
            if (seenMessages.contains(msg)) {
                duplicateSet.add(msg);
            }
            seenMessages.add(msg);
        }
        List<String> duplicates = new ArrayList<>(duplicateSet);

        return new LogAnalysisReport(
            entries.size(),
            levelCounts,
            sourceCounts,
            errors,
            timeRange,
            duplicates
        );
    }

    // ─────────────────────────────────────────────────────────────────────
    // Sample log data for testing
    // ─────────────────────────────────────────────────────────────────────

    private static final List<String> SAMPLE_LOGS = Arrays.asList(
        "2026-06-08T09:00:01 [INFO] auth.service - User alice@example.com logged in",
        "2026-06-08T09:00:03 [DEBUG] db.pool - Connection acquired from pool (active: 5)",
        "2026-06-08T09:00:05 [INFO] api.orders - GET /api/orders — 200 OK (23ms)",
        "2026-06-08T09:00:07 [WARN] api.orders - Slow query detected: 1200ms on orders table",
        "2026-06-08T09:00:09 [ERROR] payment.gateway - Timeout connecting to stripe API",
        "2026-06-08T09:00:12 [INFO] auth.service - User bob@example.com logged in",
        "2026-06-08T09:00:15 [ERROR] payment.gateway - Timeout connecting to stripe API",
        "2026-06-08T09:00:18 [INFO] api.orders - POST /api/orders — 201 Created (45ms)",
        "2026-06-08T09:00:20 [DEBUG] db.pool - Connection released back to pool (active: 4)",
        "2026-06-08T09:00:22 [ERROR] api.orders - Unhandled exception in order validation"
    );

    public static void main(String[] args) {
        LogAnalysisReport report = analyzeLogFile(SAMPLE_LOGS);

        System.out.println("Total entries: " + report.getTotalEntries());
        System.out.println("By level: " + report.getLevelCounts());
        System.out.println("By source: " + report.getSourceCounts());
        System.out.println("Errors: " + report.getErrors());
        System.out.println("Time range: " + report.getTimeRange());
        System.out.println("Duplicates: " + report.getDuplicateMessages());
    }
}
