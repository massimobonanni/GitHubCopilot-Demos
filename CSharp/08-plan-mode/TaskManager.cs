// Demo 8 — Plan Mode in Copilot Chat
// ====================================
// Plan mode lets Copilot research your codebase and outline a multi-step
// implementation plan BEFORE making any changes. This is ideal for complex
// features that touch multiple files or require architectural decisions.
//
// This file contains a working but minimal task-management app. It runs
// entirely in memory with no persistence, no validation, no API layer.
//
// Exercises (use Plan mode — the "Plan" button or select Plan in the mode picker):
//
//   1. Open this file → switch to Plan mode → type:
//      "Add SQLite persistence using Microsoft.Data.Sqlite so tasks
//      survive a restart. Keep the CLI interface."
//      → Copilot outlines the steps WITHOUT editing files yet.
//
//   2. Review the plan, then ask:
//      "Also add input validation — task titles must be 3-100 chars,
//      priority must be Low/Medium/High."
//      → The plan updates incrementally.
//
//   3. Once satisfied, switch to Agent mode and say:
//      "Implement the plan."
//      → Copilot executes the steps it outlined.
//
//   4. Bonus — start a fresh Plan conversation:
//      "Refactor this into a proper layered architecture with separate
//      classes for models, repository, and CLI. Use dependency injection."
//
// Why Plan mode?
//   • Gives you a roadmap before any code changes
//   • Lets you steer the architecture before committing
//   • Perfect for onboarding: "Plan how to add feature X to this codebase"

namespace PlanMode;

public enum Priority { Low, Medium, High }

public class TaskItem
{
    private static int _nextId = 1;

    public int Id { get; }
    public string Title { get; set; }
    public Priority Priority { get; set; }
    public bool Done { get; private set; }
    public DateTime CreatedAt { get; }
    public DateTime? CompletedAt { get; private set; }

    public TaskItem(string title, Priority priority = Priority.Medium)
    {
        Id = _nextId++;
        Title = title;
        Priority = priority;
        CreatedAt = DateTime.Now;
    }

    public void Complete()
    {
        Done = true;
        CompletedAt = DateTime.Now;
    }

    public override string ToString()
    {
        var status = Done ? "✓" : "○";
        return $"[{status}] #{Id} ({Priority}) {Title}";
    }
}

public class TaskManager
{
    private readonly List<TaskItem> _tasks = new();

    public TaskItem Add(string title, Priority priority = Priority.Medium)
    {
        var task = new TaskItem(title, priority);
        _tasks.Add(task);
        return task;
    }

    public bool Complete(int taskId)
    {
        var task = _tasks.FirstOrDefault(t => t.Id == taskId);
        if (task is null) return false;
        task.Complete();
        return true;
    }

    public bool Delete(int taskId)
    {
        var task = _tasks.FirstOrDefault(t => t.Id == taskId);
        if (task is null) return false;
        _tasks.Remove(task);
        return true;
    }

    public List<TaskItem> ListAll() =>
        _tasks.OrderBy(t => t.Done).ThenBy(t => t.CreatedAt).ToList();

    public List<TaskItem> ListPending() =>
        ListAll().Where(t => !t.Done).ToList();

    public List<TaskItem> Search(string keyword) =>
        _tasks.Where(t => t.Title.Contains(keyword, StringComparison.OrdinalIgnoreCase)).ToList();

    public Dictionary<string, string> Stats()
    {
        var total = _tasks.Count;
        var done = _tasks.Count(t => t.Done);
        var rate = total > 0 ? $"{done * 100 / total}%" : "N/A";
        return new()
        {
            ["total"] = total.ToString(),
            ["done"] = done.ToString(),
            ["pending"] = (total - done).ToString(),
            ["completion_rate"] = rate,
        };
    }
}

public class Program
{
    public static void Main()
    {
        var manager = new TaskManager();

        Console.WriteLine("=== Task Manager CLI ===\n");
        Console.WriteLine("Commands: add, complete, delete, list, pending, search, stats, quit\n");

        while (true)
        {
            Console.Write("> ");
            var command = Console.ReadLine()?.Trim().ToLower() ?? "";

            switch (command)
            {
                case "quit":
                    Console.WriteLine("Goodbye!");
                    return;

                case "add":
                    Console.Write("  Title: ");
                    var title = Console.ReadLine()?.Trim() ?? "";
                    Console.Write("  Priority (Low/Medium/High) [Medium]: ");
                    var priInput = Console.ReadLine()?.Trim();
                    var priority = Enum.TryParse<Priority>(priInput, true, out var p) ? p : Priority.Medium;
                    var task = manager.Add(title, priority);
                    Console.WriteLine($"  Added: {task}");
                    break;

                case "complete":
                    Console.Write("  Task ID: ");
                    if (int.TryParse(Console.ReadLine(), out var completeId))
                        Console.WriteLine(manager.Complete(completeId) ? "  Done!" : "  Task not found.");
                    break;

                case "delete":
                    Console.Write("  Task ID: ");
                    if (int.TryParse(Console.ReadLine(), out var deleteId))
                        Console.WriteLine(manager.Delete(deleteId) ? "  Deleted." : "  Task not found.");
                    break;

                case "list":
                    foreach (var t in manager.ListAll()) Console.WriteLine($"  {t}");
                    break;

                case "pending":
                    foreach (var t in manager.ListPending()) Console.WriteLine($"  {t}");
                    break;

                case "search":
                    Console.Write("  Keyword: ");
                    var kw = Console.ReadLine()?.Trim() ?? "";
                    var results = manager.Search(kw);
                    if (results.Count == 0) Console.WriteLine("  No matches.");
                    else foreach (var t in results) Console.WriteLine($"  {t}");
                    break;

                case "stats":
                    foreach (var kvp in manager.Stats())
                        Console.WriteLine($"  {kvp.Key}: {kvp.Value}");
                    break;

                default:
                    Console.WriteLine("  Unknown command.");
                    break;
            }
        }
    }
}
