import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Demo 8 — Plan Mode in Copilot Chat (Refactored to Java)
 * =========================================================
 * Plan mode lets Copilot research your codebase and outline a multi-step
 * implementation plan BEFORE making any changes. This is ideal for complex
 * features that touch multiple files or require architectural decisions.
 *
 * This file contains a working but minimal task-management app. It runs
 * entirely in memory with no persistence, no validation, no API layer.
 *
 * Exercises (use Plan mode — the "Plan" button or select Plan in the mode picker):
 *
 *   1. Open this file → switch to Plan mode → type:
 *      "Add SQLite persistence so tasks survive a restart. Keep the CLI interface."
 *      → Copilot outlines the steps WITHOUT editing files yet.
 *
 *   2. Review the plan, then ask:
 *      "Also add input validation — task titles must be 3-100 chars,
 *       priority must be low/medium/high."
 *      → The plan updates incrementally.
 *
 *   3. Once satisfied, switch to Agent mode and say:
 *      "Implement the plan."
 *      → Copilot executes the steps it outlined.
 *
 *   4. Bonus — start a fresh Plan conversation:
 *      "Refactor this into a proper layered architecture with separate
 *       modules for models, storage, and CLI. Add type hints everywhere."
 *
 * Why Plan mode?
 *   • Gives you a roadmap before any code changes
 *   • Lets you steer the architecture before committing
 *   • Perfect for onboarding: "Plan how to add feature X to this codebase"
 */

/**
 * Priority levels for tasks.
 */
enum Priority {
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high");

    private final String value;

    Priority(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Priority fromString(String value) {
        for (Priority p : Priority.values()) {
            if (p.value.equalsIgnoreCase(value)) {
                return p;
            }
        }
        return MEDIUM;
    }
}

/**
 * Represents a task in the task manager.
 */
class Task {
    private static int nextId = 1;

    private int id;
    private String title;
    private Priority priority;
    private boolean done;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public Task(String title, String priority) {
        this.id = nextId++;
        this.title = title;
        this.priority = Priority.fromString(priority);
        this.done = false;
        this.createdAt = LocalDateTime.now();
        this.completedAt = null;
    }

    public void complete() {
        this.done = true;
        this.completedAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Priority getPriority() {
        return priority;
    }

    public boolean isDone() {
        return done;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    @Override
    public String toString() {
        String status = done ? "✓" : "○";
        return String.format("[%s] #%d (%s) %s", status, id, priority.getValue(), title);
    }
}

/**
 * Manages a collection of tasks.
 */
class TaskManager {
    private List<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a new task with the given title and priority.
     *
     * @param title the task title
     * @param priority the task priority (low/medium/high)
     * @return the created task
     */
    public Task add(String title, String priority) {
        Task task = new Task(title, priority);
        tasks.add(task);
        return task;
    }

    /**
     * Marks a task as complete.
     *
     * @param taskId the ID of the task to complete
     * @return true if the task was found and completed, false otherwise
     */
    public boolean complete(int taskId) {
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                task.complete();
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes a task by ID.
     *
     * @param taskId the ID of the task to delete
     * @return true if the task was found and deleted, false otherwise
     */
    public boolean delete(int taskId) {
        return tasks.removeIf(task -> task.getId() == taskId);
    }

    /**
     * Returns all tasks sorted by completion status and creation date.
     *
     * @return a list of all tasks
     */
    public List<Task> listAll() {
        return tasks.stream()
            .sorted(Comparator.comparingBoolean(Task::isDone)
                .thenComparing(Task::getCreatedAt))
            .collect(Collectors.toList());
    }

    /**
     * Returns all pending (not completed) tasks.
     *
     * @return a list of pending tasks
     */
    public List<Task> listPending() {
        return listAll().stream()
            .filter(t -> !t.isDone())
            .collect(Collectors.toList());
    }

    /**
     * Searches for tasks by keyword in the title.
     *
     * @param keyword the keyword to search for
     * @return a list of matching tasks
     */
    public List<Task> search(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return tasks.stream()
            .filter(t -> t.getTitle().toLowerCase().contains(lowerKeyword))
            .collect(Collectors.toList());
    }

    /**
     * Returns statistics about the tasks.
     *
     * @return a map with task statistics
     */
    public Map<String, String> stats() {
        int total = tasks.size();
        int done = (int) tasks.stream().filter(Task::isDone).count();
        int pending = total - done;

        Map<String, String> stats = new LinkedHashMap<>();
        stats.put("total", String.valueOf(total));
        stats.put("done", String.valueOf(done));
        stats.put("pending", String.valueOf(pending));

        String completionRate = total > 0
            ? String.format("%.0f%%", (done / (double) total * 100))
            : "N/A";
        stats.put("completion_rate", completionRate);

        return stats;
    }
}

/**
 * Task Manager CLI application.
 */
public class TaskManager {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        System.out.println("=== Task Manager CLI ===\n");
        System.out.println("Commands: add, complete, delete, list, pending, search, stats, quit\n");

        while (true) {
            System.out.print("> ");
            String command = SCANNER.nextLine().strip().toLowerCase();

            try {
                switch (command) {
                    case "quit":
                        System.out.println("Goodbye!");
                        return;

                    case "add":
                        handleAdd(manager);
                        break;

                    case "complete":
                        handleComplete(manager);
                        break;

                    case "delete":
                        handleDelete(manager);
                        break;

                    case "list":
                        handleList(manager);
                        break;

                    case "pending":
                        handlePending(manager);
                        break;

                    case "search":
                        handleSearch(manager);
                        break;

                    case "stats":
                        handleStats(manager);
                        break;

                    default:
                        System.out.println("  Unknown command.");
                }
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Please try again.");
            }
        }
    }

    private static void handleAdd(TaskManager manager) {
        System.out.print("  Title: ");
        String title = SCANNER.nextLine().strip();

        System.out.print("  Priority (low/medium/high) [medium]: ");
        String priority = SCANNER.nextLine().strip();
        if (priority.isEmpty()) {
            priority = "medium";
        }

        Task task = manager.add(title, priority);
        System.out.println("  Added: " + task);
    }

    private static void handleComplete(TaskManager manager) {
        System.out.print("  Task ID: ");
        int taskId = Integer.parseInt(SCANNER.nextLine().strip());

        if (manager.complete(taskId)) {
            System.out.println("  Done!");
        } else {
            System.out.println("  Task not found.");
        }
    }

    private static void handleDelete(TaskManager manager) {
        System.out.print("  Task ID: ");
        int taskId = Integer.parseInt(SCANNER.nextLine().strip());

        if (manager.delete(taskId)) {
            System.out.println("  Deleted.");
        } else {
            System.out.println("  Task not found.");
        }
    }

    private static void handleList(TaskManager manager) {
        for (Task task : manager.listAll()) {
            System.out.println("  " + task);
        }
    }

    private static void handlePending(TaskManager manager) {
        for (Task task : manager.listPending()) {
            System.out.println("  " + task);
        }
    }

    private static void handleSearch(TaskManager manager) {
        System.out.print("  Keyword: ");
        String keyword = SCANNER.nextLine().strip();

        List<Task> results = manager.search(keyword);
        for (Task task : results) {
            System.out.println("  " + task);
        }
        if (results.isEmpty()) {
            System.out.println("  No matches.");
        }
    }

    private static void handleStats(TaskManager manager) {
        for (Map.Entry<String, String> entry : manager.stats().entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
    }
}
