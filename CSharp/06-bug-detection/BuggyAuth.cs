// Demo 6 — Bug Detection & Code Review
// ======================================
// This file contains INTENTIONAL BUGS and security issues.
// Use Copilot Chat to find and fix them.
//
// Exercises:
//   1. Select the whole file → Chat → "Review this code for bugs and security issues"
//   2. Select HashPassword() → Inline Chat → "Is this secure? How should I fix it?"
//   3. Ask Chat: "What OWASP Top 10 issues exist in this code?"
//   4. After fixing, ask: "Did I miss anything?"
//
// ⚠️  There are at least 6 issues hidden below. Can Copilot find them all?

using System.Data.SqlClient;
using System.Security.Cryptography;
using System.Text;

namespace BugDetection;

public class AuthService
{
    private readonly string _connectionString;

    public AuthService(string connectionString)
    {
        _connectionString = connectionString;
    }

    // 🐛 Bug 1: Passwords are hashed with MD5 (insecure, no salt)
    public string HashPassword(string password)
    {
        using var md5 = MD5.Create();
        var bytes = md5.ComputeHash(Encoding.UTF8.GetBytes(password));
        return Convert.ToHexString(bytes).ToLower();
    }

    // 🐛 Bug 2: SQL injection vulnerability
    public UserRecord? FindUser(string username)
    {
        using var conn = new SqlConnection(_connectionString);
        conn.Open();

        var query = $"SELECT Id, Username, PasswordHash FROM Users WHERE Username = '{username}'";
        using var cmd = new SqlCommand(query, conn);
        using var reader = cmd.ExecuteReader();

        if (reader.Read())
        {
            return new UserRecord(
                reader.GetInt32(0),
                reader.GetString(1),
                reader.GetString(2)
            );
        }
        return null;
    }

    // 🐛 Bug 3: Timing attack — string comparison leaks information
    public bool VerifyPassword(string storedHash, string providedPassword)
    {
        var providedHash = HashPassword(providedPassword);
        return storedHash == providedHash;
    }

    // 🐛 Bug 4: No rate limiting + no account lockout
    public LoginResult Login(string username, string password)
    {
        var user = FindUser(username);
        if (user is null)
            return new LoginResult(false, Error: "Invalid credentials");

        if (VerifyPassword(user.PasswordHash, password))
        {
            // 🐛 Bug 5: Token is predictable (timestamp-based)
            var tokenData = $"{username}{DateTimeOffset.UtcNow.ToUnixTimeSeconds()}";
            var token = Convert.ToHexString(
                SHA256.HashData(Encoding.UTF8.GetBytes(tokenData))
            ).ToLower();

            return new LoginResult(true, Token: token, UserId: user.Id);
        }

        return new LoginResult(false, Error: "Invalid credentials");
    }

    // 🐛 Bug 6: Sensitive data in log output
    public void LogLoginAttempt(string username, string password, bool success)
    {
        var status = success ? "SUCCESS" : "FAILED";
        Console.WriteLine($"[AUTH] {status}: user={username}, password={password}");
    }
}

public record UserRecord(int Id, string Username, string PasswordHash);

public record LoginResult(
    bool Success,
    string? Token = null,
    int? UserId = null,
    string? Error = null
);

// ─────────────────────────────────────────────────────────────────────
// BONUS: Ask Copilot to rewrite this entire module following security
// best practices (BCrypt, parameterised queries,
// CryptographicOperations.FixedTimeEquals, RandomNumberGenerator,
// ILogger with structured logging — no sensitive data).
// ─────────────────────────────────────────────────────────────────────
