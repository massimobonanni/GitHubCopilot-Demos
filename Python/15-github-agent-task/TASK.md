# 🤖 Agent Task: Extend the TextAnalyzer with new metrics

> Copy the **Prompt** block below into the GitHub Copilot **Agents** page
> (<https://github.com/copilot/agents>) — or assign it to Copilot from this
> repository — to run the task autonomously on GitHub.com.

## Prompt

In `Python/15-github-agent-task/text_analyzer.py`, implement the four methods
described in the `TODO (agent task)` comment and remove that comment:

- `count_sentences(text)` — count sentences by splitting on `.`, `!`, and `?` (ignore empty fragments).
- `average_word_length(text)` — average number of letters per word, rounded to 2 decimals; return `0` for empty text.
- `estimate_reading_time_minutes(text)` — reading time at ~200 words per minute, rounded **up** to whole minutes (minimum `1` for non-empty text).
- `top_words(text, n)` — the `n` most frequent words, case-insensitive, with ties broken alphabetically, returned as `list[tuple[str, int]]`.

Also:

- Add a **pytest** test module covering every method, including edge cases (empty string, single word, punctuation, ties).
- Update the `main` demo so it prints the new metrics for the sample text.
- Add a short `README.md` in the same folder documenting the public API with a usage example.

## Acceptance criteria

- [ ] All four methods are implemented and the `TODO` comment is removed.
- [ ] `count_sentences`, `average_word_length`, `estimate_reading_time_minutes`, and `top_words` behave as specified.
- [ ] pytest tests cover every method and all edge cases, and `pytest` passes.
- [ ] `main` prints the new metrics.
- [ ] A `README.md` documents the API with an example.
- [ ] The Pull Request description summarizes the changes.
