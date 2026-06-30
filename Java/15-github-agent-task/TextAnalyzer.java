/**
 * Demo 15 — Run an Agent Task on GitHub.com
 * =========================================
 * This file is the starting point for delegating work to the GitHub Copilot
 * <b>coding agent</b> directly from the browser at https://github.com/copilot/agents
 * (the "Agents" page) — no local editor required.
 *
 * Unlike Demo 14 (which starts from a GitHub Issue), here you describe a task in
 * plain language, hand it to the agent on GitHub.com, and the agent works
 * asynchronously in a cloud session: it creates a branch, edits files, runs the
 * build/tests, and opens a Pull Request for you to review.
 *
 * The class below intentionally implements only the basics. The feature gaps
 * listed under "TODO (agent task)" are what you ask the agent to build.
 * See TASK.md in this folder for the exact prompt to paste into GitHub.com.
 *
 * Demo flow (see the language README for the full script):
 *   1. Push this repository to GitHub
 *   2. Open https://github.com/copilot/agents and select this repository
 *   3. Paste the prompt from TASK.md and start the task
 *   4. Watch the agent session run, then review the Pull Request it opens
 *   5. Iterate with review comments, then merge
 */
public class TextAnalyzer {

    /** Counts the words in {@code text} (whitespace-separated). */
    public int countWords(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        return text.trim().split("\\s+").length;
    }

    /** Counts the characters in {@code text}. */
    public int countCharacters(String text, boolean includeWhitespace) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        if (includeWhitespace) {
            return text.length();
        }
        return (int) text.chars().filter(c -> !Character.isWhitespace(c)).count();
    }

    // TODO (agent task — see TASK.md): the Copilot coding agent on GitHub.com
    // should implement the following, with JUnit tests and a short README:
    //   - int countSentences(String text)          -> split on '.', '!', '?'
    //   - double averageWordLength(String text)    -> letters per word, 2 decimals
    //   - int estimateReadingTimeMinutes(String text) -> rounded up, ~200 wpm
    //   - List<Map.Entry<String, Integer>> topWords(String text, int n)
    //       -> the n most frequent words, case-insensitive, ties broken alphabetically

    public static void main(String[] args) {
        final String sample =
                "GitHub Copilot can run an agent task directly on GitHub.com. "
                + "You describe the work and the agent opens a Pull Request!";

        TextAnalyzer analyzer = new TextAnalyzer();

        System.out.println("=== Text Analyzer (Demo 15) ===");
        System.out.println("Words:               " + analyzer.countWords(sample));
        System.out.println("Characters:          " + analyzer.countCharacters(sample, true));
        System.out.println("Characters (no ws):  " + analyzer.countCharacters(sample, false));
        System.out.println();
        System.out.println("Sentences, average word length, reading time and top words");
        System.out.println("are NOT implemented yet — that's the agent's task (see TASK.md).");
    }
}
