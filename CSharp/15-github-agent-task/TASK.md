# 🤖 Agent Task: Extend the TextAnalyzer with new metrics

> Copy the **Prompt** block below into the GitHub Copilot **Agents** page
> (<https://github.com/copilot/agents>) — or assign it to Copilot from this
> repository — to run the task autonomously on GitHub.com.

## Prompt

In `CSharp/15-github-agent-task/TextAnalyzer.cs`, implement the four methods
described in the `TODO (agent task)` comment and remove that comment:

- `int CountSentences(string text)` — count sentences by splitting on `.`, `!`, and `?` (ignore empty fragments).
- `double AverageWordLength(string text)` — average number of letters per word, rounded to 2 decimals; return `0` for empty text.
- `int EstimateReadingTimeMinutes(string text)` — reading time at ~200 words per minute, rounded **up** to whole minutes (minimum `1` for non-empty text).
- `IReadOnlyList<(string Word, int Count)> TopWords(string text, int n)` — the `n` most frequent words, case-insensitive, with ties broken alphabetically.

Also:

- Add an **xUnit** test project/class covering every method, including edge cases (empty string, single word, punctuation, ties).
- Update the `Program.Main` demo so it prints the new metrics for the sample text.
- Add a short `README.md` in the same folder documenting the public API with a usage example.

## Acceptance criteria

- [ ] All four methods are implemented and the `TODO` comment is removed.
- [ ] `CountSentences`, `AverageWordLength`, `EstimateReadingTimeMinutes`, and `TopWords` behave as specified.
- [ ] xUnit tests cover every method and all edge cases, and `dotnet test` passes.
- [ ] `Program.Main` prints the new metrics.
- [ ] A `README.md` documents the API with an example.
- [ ] The Pull Request description summarizes the changes.
