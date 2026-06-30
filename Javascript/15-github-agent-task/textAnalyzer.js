/**
 * Demo 15 — Run an Agent Task on GitHub.com
 * =========================================
 * This file is the starting point for delegating work to the GitHub Copilot
 * coding agent directly from the browser at https://github.com/copilot/agents
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

class TextAnalyzer {
  /** Counts the words in `text` (whitespace-separated). */
  countWords(text) {
    if (!text || !text.trim()) {
      return 0;
    }
    return text.trim().split(/\s+/).length;
  }

  /** Counts the characters in `text`. */
  countCharacters(text, includeWhitespace = true) {
    if (!text) {
      return 0;
    }
    return includeWhitespace
      ? text.length
      : text.replace(/\s/g, '').length;
  }

  // TODO (agent task — see TASK.md): the Copilot coding agent on GitHub.com
  // should implement the following, with Jest tests and a short README:
  //   - countSentences(text)            -> split on '.', '!', '?'
  //   - averageWordLength(text)         -> letters per word, 2 decimals
  //   - estimateReadingTimeMinutes(text) -> rounded up, ~200 wpm
  //   - topWords(text, n)               -> the n most frequent words,
  //       case-insensitive, ties broken alphabetically -> [{ word, count }]
}

function main() {
  const sample =
    'GitHub Copilot can run an agent task directly on GitHub.com. ' +
    'You describe the work and the agent opens a Pull Request!';

  const analyzer = new TextAnalyzer();

  console.log('=== Text Analyzer (Demo 15) ===');
  console.log(`Words:               ${analyzer.countWords(sample)}`);
  console.log(`Characters:          ${analyzer.countCharacters(sample)}`);
  console.log(`Characters (no ws):  ${analyzer.countCharacters(sample, false)}`);
  console.log('');
  console.log('Sentences, average word length, reading time and top words');
  console.log("are NOT implemented yet — that's the agent's task (see TASK.md).");
}

if (require.main === module) {
  main();
}

module.exports = { TextAnalyzer };
