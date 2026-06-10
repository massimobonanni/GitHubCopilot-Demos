// Demo 5 — Prompt Engineering: The 4 S's
// ========================================
// This file demonstrates how prompt quality affects Copilot output.
// Each section shows a BAD prompt (vague) and a GOOD prompt (4 S's).
//
// The 4 S's:
//   • Single  — One task per prompt
//   • Specific — Explicit, detailed instructions
//   • Short   — Concise without overloading
//   • Surround — Descriptive names, related files open
//
// Exercises:
//   1. Delete the implementation under each section
//   2. Try the BAD prompt first (uncomment it), see what Copilot suggests
//   3. Then try the GOOD prompt instead — compare the quality
//   4. Experiment: tweak the prompts and observe how output changes

using System.Text.RegularExpressions;

namespace PromptEngineering;

// =====================================================================
// Example A: String manipulation
// =====================================================================

// ❌ BAD PROMPT (vague, multiple tasks):
// TODO: do string stuff

// ✅ GOOD PROMPT (Single, Specific, Short):
// Write a method called Slugify that takes a string title and returns
// a URL-safe slug: lowercase, spaces replaced with hyphens, only
// alphanumeric characters and hyphens allowed, no leading/trailing hyphens.

public static class StringUtils
{
    public static string Slugify(string title)
    {
        var slug = title.Trim().ToLowerInvariant();
        slug = Regex.Replace(slug, @"[^\w\s-]", "");
        slug = Regex.Replace(slug, @"[\s_]+", "-");
        slug = slug.Trim('-');
        return slug;
    }
}

// =====================================================================
// Example B: Data validation
// =====================================================================

// ❌ BAD PROMPT:
// TODO: validate email

// ✅ GOOD PROMPT:
// Write a method called ValidateEmail that takes a string and returns
// true if it matches a basic email pattern (user@domain.tld), or false
// otherwise. Do not use third-party libraries.

public static class ValidationUtils
{
    public static bool ValidateEmail(string email)
    {
        var pattern = @"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$";
        return Regex.IsMatch(email, pattern);
    }
}

// =====================================================================
// Example C: Data transformation
// =====================================================================

// ❌ BAD PROMPT:
// TODO: process the data

// ✅ GOOD PROMPT:
// Write a method called SummarizeSales that takes a list of SaleTransaction
// objects with Product (string), Amount (decimal), and Region (string).
// Return a dictionary mapping each region to its total sales amount,
// rounded to 2 decimal places.

public record SaleTransaction(string Product, decimal Amount, string Region);

public static class SalesAnalyzer
{
    public static Dictionary<string, decimal> SummarizeSales(
        List<SaleTransaction> transactions)
    {
        return transactions
            .GroupBy(tx => tx.Region)
            .ToDictionary(
                g => g.Key,
                g => Math.Round(g.Sum(tx => tx.Amount), 2)
            );
    }
}

// =====================================================================
// Example D: Context matters ("Surround")
// =====================================================================

// Below, the descriptive method name + type hints + surrounding code
// give Copilot all the context it needs. Try placing your cursor after
// the method signature and let Copilot complete the body.

public record Employee(string Name, string Department, decimal Salary, int YearsOfService);

public static class EmployeeAnalyzer
{
    /// <summary>
    /// Groups employees with at least <paramref name="minYears"/> of service by department.
    /// Returns a dictionary mapping department name → list of employee names.
    /// </summary>
    public static Dictionary<string, List<string>> GetSeniorEmployeesByDepartment(
        List<Employee> employees, int minYears = 5)
    {
        // TODO: Let Copilot complete this — the name + types + doc comment
        // give it excellent context (the "Surround" principle).
        return employees
            .Where(e => e.YearsOfService >= minYears)
            .GroupBy(e => e.Department)
            .ToDictionary(
                g => g.Key,
                g => g.Select(e => e.Name).ToList()
            );
    }
}

// =====================================================================
// Quick self-test
// =====================================================================
public class Program
{
    public static void Main()
    {
        Console.WriteLine(StringUtils.Slugify("  Hello, World! This is a Test.  "));
        // → "hello-world-this-is-a-test"

        Console.WriteLine(ValidationUtils.ValidateEmail("user@example.com"));   // True
        Console.WriteLine(ValidationUtils.ValidateEmail("not-an-email"));       // False

        var sales = new List<SaleTransaction>
        {
            new("Widget", 120.50m, "EMEA"),
            new("Gadget", 89.99m, "APAC"),
            new("Widget", 200.00m, "EMEA"),
        };

        foreach (var kvp in SalesAnalyzer.SummarizeSales(sales))
            Console.WriteLine($"  {kvp.Key}: {kvp.Value}");
        // → EMEA: 320.50   APAC: 89.99
    }
}
