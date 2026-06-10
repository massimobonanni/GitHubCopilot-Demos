// Demo 9 — Copilot Instructions
// ================================
// Copilot Instructions let you define project-wide coding standards that
// Copilot always respects — without repeating them in every prompt.
//
// How it works:
//   • Create a `*.instructions.md` file with optional `applyTo` frontmatter
//   • Place it in `.vscode/instructions/` (workspace) or `.github/` (repo-wide)
//   • Copilot reads it automatically and applies the rules to every suggestion
//   • No need to repeat conventions in individual prompts
//
// Setup for this demo:
//   1. Copy `coding-standards.instructions.md` from this folder to:
//            .vscode/instructions/coding-standards.instructions.md
//      (or rename it to `copilot-instructions.md` and place it in `.github/`)
//   2. Reload VS Code (Ctrl+Shift+P → "Developer: Reload Window")
//   3. Come back to this file and complete the TODO methods below
//
// Exercises:
//   1. First, DELETE the instructions file (or move it away) and complete
//      one TODO — note the style Copilot uses.
//   2. Restore the instructions file, reload, and complete the same TODO
//      again — compare the difference in naming, typing, error handling,
//      and logging.
//   3. Open Chat and ask: "Does this class follow our coding standards?"
//   4. Ask Chat: "Review OrderProcessor and flag any standards violations."
//   5. Bonus — edit coding-standards.instructions.md to add a new rule,
//      then ask Copilot to update the existing code to comply.

using Microsoft.Extensions.Logging;

namespace CopilotInstructions;

public enum OrderStatus { Pending, Confirmed, Shipped, Delivered, Cancelled }

public record Order(
    string OrderId,
    string CustomerEmail,
    List<string> Items,
    decimal TotalAmount,
    OrderStatus Status,
    DateTime CreatedAt
);

public record ProcessingResult(
    bool Success,
    string OrderId,
    string Message,
    DateTime ProcessedAt
);

public interface IDbConnection
{
    Task UpdateOrderStatusAsync(string orderId, OrderStatus status, CancellationToken ct = default);
}

public interface IEmailClient
{
    Task SendAsync(string to, string subject, string body, CancellationToken ct = default);
}

public class OrderProcessor
{
    private readonly ILogger<OrderProcessor> _logger;
    private readonly IDbConnection _db;
    private readonly IEmailClient _email;

    private const string OrderIdPrefix = "ORD";
    private const int MaxRetryAttempts = 3;

    public OrderProcessor(ILogger<OrderProcessor> logger, IDbConnection db, IEmailClient email)
    {
        _logger = logger;
        _db = db;
        _email = email;
    }

    // TODO: Implement ValidateOrderAsync(Order order, CancellationToken ct = default)
    // Rules: OrderId must start with OrderIdPrefix, TotalAmount must be > 0,
    // CustomerEmail must contain '@', Items must not be empty.
    // Return a ProcessingResult with Success=true or an error message.

    // TODO: Implement ConfirmOrderAsync(Order order, CancellationToken ct = default)
    // Update the order status to Confirmed via _db,
    // send a confirmation email via _email, and log the event.
    // Return a ProcessingResult.

    // TODO: Implement CancelOrderAsync(Order order, string reason, CancellationToken ct = default)
    // Validate the order is not already Delivered or Cancelled.
    // Update status to Cancelled, notify the customer, and log the event.
    // Return a ProcessingResult.
}
