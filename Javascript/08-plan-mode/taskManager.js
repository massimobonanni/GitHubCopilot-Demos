/**
 * Demo 8 — Plan Mode in Copilot Chat
 * ====================================
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
 *      "Add SQLite persistence using better-sqlite3 so tasks survive
 *      a restart. Keep the CLI interface."
 *      → Copilot outlines the steps WITHOUT editing files yet.
 *
 *   2. Review the plan, then ask:
 *      "Also add input validation — task titles must be 3-100 chars,
 *      priority must be low/medium/high."
 *      → The plan updates incrementally.
 *
 *   3. Once satisfied, switch to Agent mode and say:
 *      "Implement the plan."
 *      → Copilot executes the steps it outlined.
 *
 *   4. Bonus — start a fresh Plan conversation:
 *      "Refactor this into a proper layered architecture with separate
 *      modules for models, storage, and CLI. Use ES modules."
 *
 * Why Plan mode?
 *   • Gives you a roadmap before any code changes
 *   • Lets you steer the architecture before committing
 *   • Perfect for onboarding: "Plan how to add feature X to this codebase"
 */

const readline = require("readline");

const PRIORITIES = ["low", "medium", "high"];

class Task {
  static _nextId = 1;

  constructor(title, priority = "medium") {
    this.id = Task._nextId++;
    this.title = title;
    this.priority = priority;
    this.done = false;
    this.createdAt = new Date();
    this.completedAt = null;
  }

  complete() {
    this.done = true;
    this.completedAt = new Date();
  }

  toString() {
    const status = this.done ? "✓" : "○";
    return `[${status}] #${this.id} (${this.priority}) ${this.title}`;
  }
}

class TaskManager {
  constructor() {
    /** @type {Task[]} */
    this.tasks = [];
  }

  add(title, priority = "medium") {
    const task = new Task(title, priority);
    this.tasks.push(task);
    return task;
  }

  complete(taskId) {
    const task = this.tasks.find((t) => t.id === taskId);
    if (!task) return false;
    task.complete();
    return true;
  }

  delete(taskId) {
    const index = this.tasks.findIndex((t) => t.id === taskId);
    if (index === -1) return false;
    this.tasks.splice(index, 1);
    return true;
  }

  listAll() {
    return [...this.tasks].sort((a, b) => {
      if (a.done !== b.done) return a.done ? 1 : -1;
      return a.createdAt - b.createdAt;
    });
  }

  listPending() {
    return this.listAll().filter((t) => !t.done);
  }

  search(keyword) {
    const kw = keyword.toLowerCase();
    return this.tasks.filter((t) => t.title.toLowerCase().includes(kw));
  }

  stats() {
    const total = this.tasks.length;
    const done = this.tasks.filter((t) => t.done).length;
    return {
      total,
      done,
      pending: total - done,
      completionRate: total > 0 ? `${Math.round((done / total) * 100)}%` : "N/A",
    };
  }
}

// ─────────────────────────────────────────────────────────────────────
// CLI Interface
// ─────────────────────────────────────────────────────────────────────
const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout,
});

const manager = new TaskManager();

function prompt(question) {
  return new Promise((resolve) => rl.question(question, resolve));
}

async function main() {
  console.log("=== Task Manager CLI ===\n");
  console.log("Commands: add, complete, delete, list, pending, search, stats, quit\n");

  while (true) {
    const command = (await prompt("> ")).trim().toLowerCase();

    switch (command) {
      case "quit":
        console.log("Goodbye!");
        rl.close();
        return;

      case "add": {
        const title = (await prompt("  Title: ")).trim();
        const pri = (await prompt("  Priority (low/medium/high) [medium]: ")).trim() || "medium";
        const task = manager.add(title, pri);
        console.log(`  Added: ${task}`);
        break;
      }

      case "complete": {
        const id = parseInt(await prompt("  Task ID: "), 10);
        console.log(manager.complete(id) ? "  Done!" : "  Task not found.");
        break;
      }

      case "delete": {
        const id = parseInt(await prompt("  Task ID: "), 10);
        console.log(manager.delete(id) ? "  Deleted." : "  Task not found.");
        break;
      }

      case "list":
        for (const t of manager.listAll()) console.log(`  ${t}`);
        break;

      case "pending":
        for (const t of manager.listPending()) console.log(`  ${t}`);
        break;

      case "search": {
        const keyword = (await prompt("  Keyword: ")).trim();
        const results = manager.search(keyword);
        if (results.length === 0) console.log("  No matches.");
        else for (const t of results) console.log(`  ${t}`);
        break;
      }

      case "stats":
        for (const [key, val] of Object.entries(manager.stats()))
          console.log(`  ${key}: ${val}`);
        break;

      default:
        console.log("  Unknown command.");
    }
  }
}

main();
