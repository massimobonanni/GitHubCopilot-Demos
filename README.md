# GH-300-Demos

## Goal

This repository contains demo examples for the **GH-300: GitHub Copilot Fundamentals** course. The demos are designed to showcase GitHub Copilot's key capabilities during live training sessions, covering topics such as code completions, chat-based refactoring, test generation, documentation, prompt engineering, bug detection, and code explanation.

The course is split into two parts, available on Microsoft Learn:

- [GitHub Copilot Fundamentals — Part 1 of 2](https://learn.microsoft.com/en-us/training/paths/copilot/)
- [GitHub Copilot Fundamentals — Part 2 of 2](https://learn.microsoft.com/en-us/training/paths/gh-copilot-2/)

---

## Project Structure

The repository is organized by programming language. Each language folder contains the same set of seven demos, each targeting a specific Copilot feature.

```
GH-300-Demos/
├── CSharp/       # C# demo examples
└── Python/       # Python demo examples
```

| Folder | Language | README |
|--------|----------|--------|
| `CSharp/` | C# (.NET 8+) | [CSharp/README.md](CSharp/README.md) |
| `Python/` | Python 3.12+ | [Python/README.md](Python/README.md) |

Each sub-folder contains the following demos:

| # | Folder | Copilot Feature |
|---|--------|-----------------|
| 1 | `01-code-completions` | Inline suggestions |
| 2 | `02-chat-and-refactoring` | Chat & Inline Chat refactoring |
| 3 | `03-test-generation` | Test generation |
| 4 | `04-documentation` | Doc & README generation |
| 5 | `05-prompt-engineering` | Prompt quality comparison |
| 6 | `06-bug-detection` | Security review & bug finding |
| 7 | `07-explain-and-debug` | Code explanation & optimization |