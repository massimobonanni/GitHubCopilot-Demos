"""
Demo 15 — Run an Agent Task on GitHub.com
=========================================
This file is the starting point for delegating work to the GitHub Copilot
coding agent directly from the browser at https://github.com/copilot/agents
(the "Agents" page) -- no local editor required.

Unlike Demo 14 (which starts from a GitHub Issue), here you describe a task in
plain language, hand it to the agent on GitHub.com, and the agent works
asynchronously in a cloud session: it creates a branch, edits files, runs the
build/tests, and opens a Pull Request for you to review.

The class below intentionally implements only the basics. The feature gaps
listed under "TODO (agent task)" are what you ask the agent to build.
See TASK.md in this folder for the exact prompt to paste into GitHub.com.

Demo flow (see the language README for the full script):
  1. Push this repository to GitHub
  2. Open https://github.com/copilot/agents and select this repository
  3. Paste the prompt from TASK.md and start the task
  4. Watch the agent session run, then review the Pull Request it opens
  5. Iterate with review comments, then merge
"""


class TextAnalyzer:
    """Analyzes text. Only word/character counting is implemented so far."""

    def count_words(self, text: str) -> int:
        """Counts the words in ``text`` (whitespace-separated)."""
        if not text or not text.strip():
            return 0
        return len(text.split())

    def count_characters(self, text: str, include_whitespace: bool = True) -> int:
        """Counts the characters in ``text``."""
        if not text:
            return 0
        if include_whitespace:
            return len(text)
        return sum(1 for c in text if not c.isspace())

    # TODO (agent task -- see TASK.md): the Copilot coding agent on GitHub.com
    # should implement the following, with pytest tests and a short README:
    #   - count_sentences(text) -> int            : split on '.', '!', '?'
    #   - average_word_length(text) -> float      : letters per word, 2 decimals
    #   - estimate_reading_time_minutes(text) -> int : rounded up, ~200 wpm
    #   - top_words(text, n) -> list[tuple[str, int]]
    #       : the n most frequent words, case-insensitive,
    #         ties broken alphabetically


def main() -> None:
    sample = (
        "GitHub Copilot can run an agent task directly on GitHub.com. "
        "You describe the work and the agent opens a Pull Request!"
    )

    analyzer = TextAnalyzer()

    print("=== Text Analyzer (Demo 15) ===")
    print(f"Words:               {analyzer.count_words(sample)}")
    print(f"Characters:          {analyzer.count_characters(sample)}")
    print(f"Characters (no ws):  {analyzer.count_characters(sample, include_whitespace=False)}")
    print()
    print("Sentences, average word length, reading time and top words")
    print("are NOT implemented yet -- that's the agent's task (see TASK.md).")


if __name__ == "__main__":
    main()
