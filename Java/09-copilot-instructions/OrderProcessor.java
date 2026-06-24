import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Demo 9 — Copilot Instructions
 * ===============================
 * Copilot Instructions let you define project-wide coding standards that
 * Copilot always respects — without repeating them in every prompt.
 *
 * How it works:
 *   • Create a *.instructions.md file with optional YAML frontmatter
 *   • Place it in .vscode/instructions/ (workspace) or .github/ (repo-wide)
 *   • Copilot reads it automatically and applies the rules to every suggestion
 *   • No need to repeat conventions in individual prompts
 *
 * Setup for this demo:
 *   1. Create a coding-standards.instructions.md file with rules for Java
 *   2. Place it at: .vscode/instructions/java-coding-standards.instructions.md
 *   3. Reload VS Code (Ctrl+Shift+P → "Developer: Reload Window")
 *   4. Come back to this file and complete the TODO methods below
 *
 * Exercises:
 *   1. First, DELETE the instructions file and complete one TODO — note the style Copilot uses.
 *   2. Restore the instructions file, reload, and complete the same TODO again — compare differences.
 *   3. Open Chat and ask: "Does this class follow our coding standards?"
 *   4. Ask Chat: "Review OrderProcessor and flag any standards violations."
 *   5. Bonus — edit coding-standards.instructions.md to add a new rule,
 *      then ask Copilot to update the existing code to comply.
 */

/**
 * Represents the lifecycle status of an order.
 */
enum OrderStatus {
    PENDING("pending"),
    CONFIRMED("confirmed"),
    SHIPPED("shipped"),
    DELIVERED("delivered"),
    CANCELLED("cancelled");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

/**
 * Represents a customer order.
 */
class Order {
    private String orderId;
    private String customerEmail;
    private List<String> items;
    private double totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;

    public Order(String orderId, String customerEmail, List<String> items,
                 double totalAmount, OrderStatus status, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.customerEmail = customerEmail;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public List<String> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

/**
 * Result of an order processing operation.
 */
class ProcessingResult {
    private boolean success;
    private String orderId;
    private String message;
    private LocalDateTime processedAt;

    public ProcessingResult(boolean success, String orderId, String message, LocalDateTime processedAt) {
        this.success = success;
        this.orderId = orderId;
        this.message = message;
        this.processedAt = processedAt;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    @Override
    public String toString() {
        return String.format(
            "ProcessingResult{success=%b, orderId='%s', message='%s', processedAt=%s}",
            success, orderId, message, processedAt
        );
    }
}

/**
 * Processes customer orders through their lifecycle.
 * Coordinates with database and email services to manage order states.
 */
public class OrderProcessor {

    private static final Logger LOGGER = Logger.getLogger(OrderProcessor.class.getName());

    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final String ORDER_ID_PREFIX = "ORD";

    private Object dbConnection;      // Database connection (injected)
    private Object emailClient;       // Email service client (injected)

    /**
     * Constructs an OrderProcessor with database and email dependencies.
     *
     * @param dbConnection the database connection
     * @param emailClient the email client service
     */
    public OrderProcessor(Object dbConnection, Object emailClient) {
        this.dbConnection = dbConnection;
        this.emailClient = emailClient;
    }

    // TODO: Implement validateOrder(Order order) -> ProcessingResult
    // Rules: 
    //   • order_id must start with ORDER_ID_PREFIX
    //   • total_amount must be > 0
    //   • customer_email must contain '@'
    //   • items list must not be empty
    // Return a ProcessingResult with success=true or an error message.
    // Apply early returns to avoid deep nesting.
    // Log validation failures at WARNING level with context.
    public ProcessingResult validateOrder(Order order) {
        if (order == null) {
            LOGGER.warning("Attempted to validate null order");
            return new ProcessingResult(false, "UNKNOWN", "Order cannot be null", LocalDateTime.now());
        }

        // Early return: check order ID format
        if (!order.getOrderId().startsWith(ORDER_ID_PREFIX)) {
            String message = String.format(
                "Invalid order ID format: '%s' (must start with '%s')",
                order.getOrderId(), ORDER_ID_PREFIX
            );
            LOGGER.warning(message);
            return new ProcessingResult(false, order.getOrderId(), message, LocalDateTime.now());
        }

        // Early return: check total amount
        if (order.getTotalAmount() <= 0) {
            String message = String.format(
                "Invalid total amount: %.2f (must be > 0)",
                order.getTotalAmount()
            );
            LOGGER.warning(message);
            return new ProcessingResult(false, order.getOrderId(), message, LocalDateTime.now());
        }

        // Early return: check email format
        if (!order.getCustomerEmail().contains("@")) {
            String message = String.format(
                "Invalid email format: '%s' (must contain '@')",
                order.getCustomerEmail()
            );
            LOGGER.warning(message);
            return new ProcessingResult(false, order.getOrderId(), message, LocalDateTime.now());
        }

        // Early return: check items list
        if (order.getItems() == null || order.getItems().isEmpty()) {
            String message = "Order must contain at least one item";
            LOGGER.warning(message);
            return new ProcessingResult(false, order.getOrderId(), message, LocalDateTime.now());
        }

        LOGGER.info(String.format("Order %s validation passed", order.getOrderId()));
        return new ProcessingResult(
            true,
            order.getOrderId(),
            "Order validation successful",
            LocalDateTime.now()
        );
    }

    // TODO: Implement confirmOrder(Order order) -> ProcessingResult
    // Update the order status to CONFIRMED in dbConnection,
    // send a confirmation email via emailClient, and log the confirmation event.
    // Use try-catch with specific exception types.
    // Always log at INFO level with order ID and status.
    // Return a ProcessingResult with appropriate success/failure status.
    public ProcessingResult confirmOrder(Order order) {
        if (order == null) {
            LOGGER.warning("Attempted to confirm null order");
            return new ProcessingResult(false, "UNKNOWN", "Order cannot be null", LocalDateTime.now());
        }

        try {
            // Update order status in database
            order.setStatus(OrderStatus.CONFIRMED);
            // TODO: Implement database persistence: dbConnection.update(order)

            // Send confirmation email
            // TODO: Implement email sending: emailClient.sendConfirmation(order.getCustomerEmail(), order)

            LOGGER.info(String.format(
                "Order %s confirmed. Customer: %s",
                order.getOrderId(),
                order.getCustomerEmail()
            ));

            return new ProcessingResult(
                true,
                order.getOrderId(),
                "Order confirmed successfully",
                LocalDateTime.now()
            );
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, String.format(
                "Failed to confirm order %s: %s",
                order.getOrderId(),
                e.getMessage()
            ), e);
            return new ProcessingResult(
                false,
                order.getOrderId(),
                String.format("Confirmation failed: %s", e.getMessage()),
                LocalDateTime.now()
            );
        }
    }

    // TODO: Implement cancelOrder(Order order, String reason) -> ProcessingResult
    // Validate the order is not already DELIVERED or CANCELLED.
    // Update the status to CANCELLED in dbConnection.
    // Notify the customer via emailClient.
    // Log the cancellation event with order ID and reason.
    // Return a ProcessingResult.
    public ProcessingResult cancelOrder(Order order, String reason) {
        if (order == null) {
            LOGGER.warning("Attempted to cancel null order");
            return new ProcessingResult(false, "UNKNOWN", "Order cannot be null", LocalDateTime.now());
        }

        // Early return: check if order is already delivered
        if (order.getStatus() == OrderStatus.DELIVERED) {
            String message = "Cannot cancel a delivered order";
            LOGGER.warning(String.format(
                "Cancel attempt on delivered order %s: %s",
                order.getOrderId(),
                message
            ));
            return new ProcessingResult(false, order.getOrderId(), message, LocalDateTime.now());
        }

        // Early return: check if order is already cancelled
        if (order.getStatus() == OrderStatus.CANCELLED) {
            String message = "Order is already cancelled";
            LOGGER.info(String.format("Order %s was already cancelled", order.getOrderId()));
            return new ProcessingResult(false, order.getOrderId(), message, LocalDateTime.now());
        }

        try {
            // Update order status
            order.setStatus(OrderStatus.CANCELLED);
            // TODO: Implement database update: dbConnection.update(order)

            // Notify customer
            // TODO: Implement email notification: emailClient.sendCancellation(order.getCustomerEmail(), order, reason)

            LOGGER.info(String.format(
                "Order %s cancelled. Reason: %s. Customer notified: %s",
                order.getOrderId(),
                reason,
                order.getCustomerEmail()
            ));

            return new ProcessingResult(
                true,
                order.getOrderId(),
                String.format("Order cancelled. Reason: %s", reason),
                LocalDateTime.now()
            );
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, String.format(
                "Failed to cancel order %s: %s",
                order.getOrderId(),
                e.getMessage()
            ), e);
            return new ProcessingResult(
                false,
                order.getOrderId(),
                String.format("Cancellation failed: %s", e.getMessage()),
                LocalDateTime.now()
            );
        }
    }
}
