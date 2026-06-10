"""
Demo 8 — Plan Mode in Copilot Chat
====================================
Plan mode lets Copilot research your codebase and outline a multi-step
implementation plan BEFORE making any changes. This is ideal for complex
features that touch multiple files or require architectural decisions.

This file contains a working but minimal task-management app. It runs
entirely in memory with no persistence, no validation, no API layer.

Exercises (use Plan mode — the "Plan" button or select Plan in the mode picker):

  1. Open this file → switch to Plan mode → type:
     "Add SQLite persistence so tasks survive a restart. Keep the CLI interface."
     → Copilot outlines the steps WITHOUT editing files yet.

  2. Review the plan, then ask:
     "Also add input validation — task titles must be 3-100 chars,
     priority must be low/medium/high."
     → The plan updates incrementally.

  3. Once satisfied, switch to Agent mode and say:
     "Implement the plan."
     → Copilot executes the steps it outlined.

  4. Bonus — start a fresh Plan conversation:
     "Refactor this into a proper layered architecture with separate
     modules for models, storage, and CLI. Add type hints everywhere."

Why Plan mode?
  • Gives you a roadmap before any code changes
  • Lets you steer the architecture before committing
  • Perfect for onboarding: "Plan how to add feature X to this codebase"
"""

from datetime import datetime
from enum import Enum


class Priority(Enum):
    LOW = "low"
    MEDIUM = "medium"
    HIGH = "high"


class Task:
    _next_id = 1

    def __init__(self, title: str, priority: str = "medium"):
        self.id = Task._next_id
        Task._next_id += 1
        self.title = title
        self.priority = Priority(priority)
        self.done = False
        self.created_at = datetime.now()
        self.completed_at: datetime | None = None

    def complete(self):
        self.done = True
        self.completed_at = datetime.now()

    def __str__(self):
        status = "✓" if self.done else "○"
        return f"[{status}] #{self.id} ({self.priority.value}) {self.title}"


class TaskManager:
    def __init__(self):
        self.tasks: list[Task] = []

    def add(self, title: str, priority: str = "medium") -> Task:
        task = Task(title, priority)
        self.tasks.append(task)
        return task

    def complete(self, task_id: int) -> bool:
        for task in self.tasks:
            if task.id == task_id:
                task.complete()
                return True
        return False

    def delete(self, task_id: int) -> bool:
        for i, task in enumerate(self.tasks):
            if task.id == task_id:
                self.tasks.pop(i)
                return True
        return False

    def list_all(self) -> list[Task]:
        return sorted(self.tasks, key=lambda t: (t.done, t.created_at))

    def list_pending(self) -> list[Task]:
        return [t for t in self.list_all() if not t.done]

    def search(self, keyword: str) -> list[Task]:
        keyword = keyword.lower()
        return [t for t in self.tasks if keyword in t.title.lower()]

    def stats(self) -> dict:
        total = len(self.tasks)
        done = sum(1 for t in self.tasks if t.done)
        return {
            "total": total,
            "done": done,
            "pending": total - done,
            "completion_rate": f"{(done / total * 100):.0f}%" if total > 0 else "N/A",
        }


def main():
    manager = TaskManager()

    print("=== Task Manager CLI ===\n")
    print("Commands: add, complete, delete, list, pending, search, stats, quit\n")

    while True:
        command = input("> ").strip().lower()

        if command == "quit":
            print("Goodbye!")
            break
        elif command == "add":
            title = input("  Title: ").strip()
            priority = input("  Priority (low/medium/high) [medium]: ").strip() or "medium"
            task = manager.add(title, priority)
            print(f"  Added: {task}")
        elif command == "complete":
            task_id = int(input("  Task ID: "))
            if manager.complete(task_id):
                print("  Done!")
            else:
                print("  Task not found.")
        elif command == "delete":
            task_id = int(input("  Task ID: "))
            if manager.delete(task_id):
                print("  Deleted.")
            else:
                print("  Task not found.")
        elif command == "list":
            for task in manager.list_all():
                print(f"  {task}")
        elif command == "pending":
            for task in manager.list_pending():
                print(f"  {task}")
        elif command == "search":
            keyword = input("  Keyword: ").strip()
            results = manager.search(keyword)
            for task in results:
                print(f"  {task}")
            if not results:
                print("  No matches.")
        elif command == "stats":
            for key, val in manager.stats().items():
                print(f"  {key}: {val}")
        else:
            print("  Unknown command.")


if __name__ == "__main__":
    main()
