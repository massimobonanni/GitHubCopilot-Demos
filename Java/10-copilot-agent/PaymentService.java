import java.sql.*;
import java.security.MessageDigest;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Demo 10 — Custom Copilot Agent
 * ================================
 * A custom Copilot Agent is a reusable chat participant you define with a
 * `.agent.md` file. It has its own name, description, system prompt, and
 * toolset — and appears in the Copilot Chat mode picker alongside Ask, Edit,
 * Plan, and Agent.
 *
 * How it works:
 *   • Create a <name>.agent.md file in .vscode/
 *   • The YAML frontmatter sets the agent's name, description, and tools
 *   • The markdown body is the system prompt Copilot uses for every message
 *   • The agent appears in the Chat mode picker — select it by name
 *
 * Setup for this demo:
 *   1. Copy code-reviewer.agent.md from this folder to:
 *      .vscode/code-reviewer.agent.md
 *   2. Reload VS Code (Ctrl+Shift+P → "Developer: Reload Window")
 *   3. Open Copilot Chat → click the mode picker → select "Code Reviewer"
 *
 * Exercises:
 *   1. Open this file → switch to the "Code Reviewer" agent
 *   2. Type: "Review this module"
 *      → The agent inspects the whole file and produces a structured report
 *   3. Ask: "What are the security risks in this file?"
 *   4. Ask: "Which functions have the worst error handling?"
 *   5. Ask: "Rewrite fetchUserOrders to fix the issues you found"
 *   6. Bonus — edit code-reviewer.agent.md to add a new review criterion
 *      (e.g., "flag any method over 30 lines"), then repeat the review
 *      and compare the output.
 */

public class PaymentService {

    private static final Logger LOGGER = Logger.getLogger(PaymentService.class.getName());

    private static final String DB_PATH = "app.db";
    private static final String SECRET_KEY = "hardcoded-secret-1234";      // ⚠️ hard-coded secret
    private static final String ADMIN_PASSWORD = "admin123";               // ⚠️ hard-coded credential

    /**
     * Gets a connection to the SQLite database.
     *
     * @return a database connection
     * @throws SQLException if connection fails
     */
    private static Connection getDb() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
    }

    /**
     * Fetches all orders for a given user.
     * ⚠️ SQL injection — string formatting instead of parameterised query
     *
     * @param userId the user ID
     * @return list of order records
     */
    public static List<Map<String, Object>> fetchUserOrders(int userId) {
        List<Map<String, Object>> rows = new ArrayList<>();
        try (Connection conn = getDb()) {
            Statement stmt = conn.createStatement();
            // ⚠️ SQL INJECTION VULNERABILITY
            String query = "SELECT * FROM orders WHERE user_id = " + userId;
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("user_id", rs.getInt("user_id"));
                row.put("amount", rs.getDouble("amount"));
                rows.add(row);
            }
        } catch (SQLException e) {
            // ⚠️ Exception swallowed silently
            LOGGER.log(Level.WARNING, "Failed to fetch orders", e);
        }
        return rows;
    }

    /**
     * Authenticates a user by username and password.
     * ⚠️ Multiple security issues: MD5 hashing, SQL injection, hard-coded credentials
     *
     * @param username the username
     * @param password the password
     * @return true if authentication succeeds, false otherwise
     */
    public static boolean authenticate(String username, String password) {
        try {
            // ⚠️ MD5 IS CRYPTOGRAPHICALLY BROKEN FOR PASSWORD HASHING
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            StringBuilder hashed = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hashed.append('0');
                hashed.append(hex);
            }

            try (Connection conn = getDb()) {
                Statement stmt = conn.createStatement();
                // ⚠️ SQL INJECTION AGAIN — String formatting in query
                String query = String.format(
                    "SELECT id FROM users WHERE username='%s' AND password='%s'",
                    username, hashed.toString()
                );
                ResultSet result = stmt.executeQuery(query);
                return result.next();
            }
        } catch (Exception e) {
            // ⚠️ Exception swallowed
            LOGGER.log(Level.SEVERE, "Authentication error", e);
            return false;
        }
    }

    /**
     * Calculates the total amount of an order list.
     * ⚠️ O(n²) algorithm — inefficient nested loops with no early exit
     *
     * @param orders list of orders (each containing items)
     * @return total amount
     */
    public static double calculateOrderTotal(List<Map<String, Object>> orders) {
        double total = 0;
        // ⚠️ INEFFICIENT O(n²) WITH REDUNDANT OPERATIONS
        for (Map<String, Object> order : orders) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items = (List<Map<String, Object>>) order.get("items");
            if (items == null) continue;

            for (Map<String, Object> item : items) {
                // ⚠️ USELESS INNER LOOP — No purpose, just wastes cycles
                for (Map<String, Object> unused : orders) {
                    // Does nothing
                }
                double price = ((Number) item.get("price")).doubleValue();
                int quantity = ((Number) item.get("quantity")).intValue();
                total += price * quantity;
            }
        }
        return total;
    }

    /**
     * Deletes a user and all their orders.
     * ⚠️ No validation, no logging of cascade deletes, no error handling
     *
     * @param userId the user ID to delete
     */
    public static void deleteUser(int userId) {
        try (Connection conn = getDb()) {
            // ⚠️ SQL INJECTION — String formatting in delete query
            String query1 = "DELETE FROM users WHERE id = " + userId;
            String query2 = "DELETE FROM orders WHERE user_id = " + userId;

            Statement stmt = conn.createStatement();
            stmt.execute(query1);
            stmt.execute(query2);
            conn.commit();
            // ⚠️ NO LOGGING of what was deleted, no validation
        } catch (SQLException e) {
            // ⚠️ Exception swallowed silently — data inconsistency possible
            LOGGER.log(Level.WARNING, "Delete user error", e);
        }
    }

    /**
     * Gets a report of orders within a date range.
     * ⚠️ Bare catch that swallows all exceptions silently
     *
     * @param startDate the start date (YYYY-MM-DD format)
     * @param endDate the end date (YYYY-MM-DD format)
     * @return list of matching orders, or empty list on any error
     */
    public static List<Map<String, Object>> getReport(String startDate, String endDate) {
        List<Map<String, Object>> results = new ArrayList<>();
        try (Connection conn = getDb()) {
            Statement stmt = conn.createStatement();
            // ⚠️ SQL INJECTION — String formatting with dates
            String query = String.format(
                "SELECT * FROM orders WHERE created_at BETWEEN '%s' AND '%s'",
                startDate, endDate
            );
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("created_at", rs.getString("created_at"));
                row.put("amount", rs.getDouble("amount"));
                results.add(row);
            }
        } catch (Exception e) {
            // ⚠️ BARE CATCH — Swallows all exceptions silently, including programming errors
            // No logging, no context — debugging is impossible
        }
        return results;
    }
}
