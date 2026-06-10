// Demo 10 — Custom Copilot Agent
// ================================
// A custom Copilot Agent is a reusable chat participant you define with a
// `.agent.md` file. It has its own name, description, system prompt, and
// toolset — and appears in the Copilot Chat mode picker alongside Ask, Edit,
// Plan, and Agent.
//
// How it works:
//   • Create a `<name>.agent.md` file in `.vscode/`
//   • The YAML frontmatter sets the agent's name, description, and tools
//   • The markdown body is the system prompt Copilot uses for every message
//   • The agent appears in the Chat mode picker — select it by name
//
// Setup for this demo:
//   1. Copy `code-reviewer.agent.md` from this folder to:
//            .vscode/code-reviewer.agent.md
//   2. Reload VS Code (Ctrl+Shift+P → "Developer: Reload Window")
//   3. Open Copilot Chat → click the mode picker → select "Code Reviewer"
//
// Exercises:
//   1. Open this file → switch to the "Code Reviewer" agent
//   2. Type: "Review this file"
//      → The agent inspects the file and produces a structured report
//   3. Ask: "What are the security risks in this file?"
//   4. Ask: "Which methods have the worst error handling?"
//   5. Ask: "Rewrite FetchUserOrders to fix the issues you found"
//   6. Bonus — edit code-reviewer.agent.md to add a new rule
//      (e.g., "flag any method over 30 lines"), then repeat the review
//      and compare the output.

using System.Data.SqlClient;
using System.Security.Cryptography;
using System.Text;

namespace PaymentService;

public class PaymentService
{
    // ⚠️ Hard-coded connection string with embedded credentials
    private const string ConnectionString =
        "Server=prod-db;Database=app;User Id=sa;Password=Admin1234!;";

    // ⚠️ Hard-coded secret used for token signing
    private const string SecretKey = "hardcoded-jwt-secret-9876";

    public IEnumerable<object> FetchUserOrders(string userId)
    {
        // ⚠️ SQL injection — string concatenation instead of parameterised query
        var query = "SELECT * FROM Orders WHERE UserId = '" + userId + "'";
        using var conn = new SqlConnection(ConnectionString);
        conn.Open();
        using var cmd = new SqlCommand(query, conn);
        using var reader = cmd.ExecuteReader();
        var results = new List<object>();
        while (reader.Read())
            results.Add(reader);
        return results;
    }

    public bool Authenticate(string username, string password)
    {
        // ⚠️ MD5 is cryptographically broken for password hashing
        var hash = Convert.ToHexString(MD5.HashData(Encoding.UTF8.GetBytes(password)));

        // ⚠️ SQL injection again
        var query = $"SELECT Id FROM Users WHERE Username='{username}' AND Password='{hash}'";
        using var conn = new SqlConnection(ConnectionString);
        conn.Open();
        using var cmd = new SqlCommand(query, conn);
        return cmd.ExecuteScalar() != null;
    }

    public decimal CalculateOrderTotal(List<dynamic> orders)
    {
        // ⚠️ O(n²) — inner loop over `orders` is redundant and never used
        decimal total = 0;
        foreach (var order in orders)
            foreach (var item in order.Items)
            {
                foreach (var _ in orders) { }     // unused inner loop
                total += item.Price * item.Quantity;
            }
        return total;
    }

    public void DeleteUser(int userId)
    {
        // ⚠️ No validation, no logging, no error handling, exceptions silently ignored
        try
        {
            using var conn = new SqlConnection(ConnectionString);
            conn.Open();
            new SqlCommand($"DELETE FROM Users WHERE Id = {userId}", conn).ExecuteNonQuery();
            new SqlCommand($"DELETE FROM Orders WHERE UserId = {userId}", conn).ExecuteNonQuery();
        }
        catch { }
    }

    public IEnumerable<object> GetReport(string startDate, string endDate)
    {
        // ⚠️ SQL injection via date strings, swallows all exceptions
        try
        {
            using var conn = new SqlConnection(ConnectionString);
            conn.Open();
            var query = $"SELECT * FROM Orders WHERE CreatedAt BETWEEN '{startDate}' AND '{endDate}'";
            using var cmd = new SqlCommand(query, conn);
            using var reader = cmd.ExecuteReader();
            var results = new List<object>();
            while (reader.Read()) results.Add(reader);
            return results;
        }
        catch
        {
            return Enumerable.Empty<object>();
        }
    }
}
