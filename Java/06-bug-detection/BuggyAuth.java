import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

/**
 * Demo 6 — Bug Detection & Code Review (Refactored to Java)
 * ===========================================================
 * This file contains INTENTIONAL BUGS and security issues.
 * Use Copilot Chat to find and fix them.
 *
 * Exercises:
 *   1. Select the whole file → Chat → "Review this code for bugs and security issues"
 *   2. Select hashPassword() → Inline Chat → "Is this secure? How should I fix it?"
 *   3. Ask Chat: "What OWASP Top 10 issues exist in this code?"
 *   4. After fixing, ask: "Did I miss anything?"
 *
 * ⚠️  There are at least 6 issues hidden below. Can Copilot find them all?
 */

public class BuggyAuth {

    // 🐛 Bug 1: Passwords are hashed with MD5 (insecure, no salt)
    /**
     * Hashes a password using MD5 (INSECURE — for educational purposes only).
     *
     * @param password the password to hash
     * @return the MD5 hash of the password
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }

    // 🐛 Bug 2: SQL injection vulnerability
    /**
     * Finds a user by username (VULNERABLE TO SQL INJECTION).
     *
     * @param dbPath the path to the SQLite database
     * @param username the username to search for
     * @return a map with user data or null if not found
     */
    public static Map<String, Object> findUser(String dbPath, String username) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath)) {
            // 🐛 SQL INJECTION: username is directly interpolated
            String query = "SELECT id, username, password_hash FROM users WHERE username = '" + username + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("id", rs.getInt("id"));
                user.put("username", rs.getString("username"));
                user.put("password_hash", rs.getString("password_hash"));
                return user;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    // 🐛 Bug 3: Timing attack — string comparison leaks information
    /**
     * Verifies a password against a stored hash (VULNERABLE TO TIMING ATTACKS).
     *
     * @param storedHash the stored password hash
     * @param providedPassword the password provided by the user
     * @return true if the password matches, false otherwise
     */
    public static boolean verifyPassword(String storedHash, String providedPassword) {
        String providedHash = hashPassword(providedPassword);
        // 🐛 TIMING ATTACK: == comparison returns early on mismatch
        return storedHash.equals(providedHash);
    }

    // 🐛 Bug 4: No rate limiting + no account lockout
    /**
     * Attempts to log in a user (NO RATE LIMITING OR ACCOUNT LOCKOUT).
     *
     * @param dbPath the path to the SQLite database
     * @param username the username
     * @param password the password
     * @return a map with success status, token, or error message
     */
    public static Map<String, Object> login(String dbPath, String username, String password) {
        Map<String, Object> user = findUser(dbPath, username);

        if (user == null) {
            return Map.of("success", false, "error", "Invalid credentials");
        }

        if (verifyPassword((String) user.get("password_hash"), password)) {
            // 🐛 Bug 5: Token is predictable (timestamp-based)
            long timestamp = System.currentTimeMillis();
            String tokenSource = username + timestamp;
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] messageDigest = md.digest(tokenSource.getBytes());
                StringBuilder token = new StringBuilder();
                for (byte b : messageDigest) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) token.append('0');
                    token.append(hex);
                }
                return Map.of(
                    "success", true,
                    "token", token.toString(),
                    "user_id", user.get("id")
                );
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("SHA-256 algorithm not available", e);
            }
        } else {
            return Map.of("success", false, "error", "Invalid credentials");
        }
    }

    // 🐛 Bug 6: Sensitive data in log output
    /**
     * Logs a login attempt including sensitive data (INSECURE LOGGING).
     *
     * @param username the username
     * @param password the password (LOGGED IN PLAINTEXT!)
     * @param success whether the login was successful
     */
    public static void logLoginAttempt(String username, String password, boolean success) {
        String status = success ? "SUCCESS" : "FAILED";
        // 🐛 SECURITY ISSUE: Password is logged in plaintext!
        System.out.println("[AUTH] " + status + ": user=" + username + ", password=" + password);
    }

    // ─────────────────────────────────────────────────────────────────────
    // BONUS: Ask Copilot to rewrite this entire module following security
    // best practices (bcrypt, parameterised queries, constant-time comparison,
    // SecureRandom tokens, proper logging).
    // ─────────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("Hash of 'password123': " + hashPassword("password123"));
        System.out.println("Verify 'password123': " + verifyPassword(
            hashPassword("password123"),
            "password123"
        ));
        logLoginAttempt("alice", "super_secret_password", true);
    }
}
