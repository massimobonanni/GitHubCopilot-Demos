import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * Demo 5 — Prompt Engineering: The 4 S's (Refactored to Java)
 * ===========================================================
 * This file demonstrates how prompt quality affects Copilot output.
 * Each section shows a BAD prompt (vague) and a GOOD prompt (4 S's).
 *
 * The 4 S's:
 *   • Single  — One task per prompt
 *   • Specific — Explicit, detailed instructions
 *   • Short   — Concise without overloading
 *   • Surround — Descriptive names, related files open
 *
 * Exercises:
 *   1. Delete the implementation under each section
 *   2. Try the BAD prompt first, see what Copilot suggests
 *   3. Then try the GOOD prompt — compare the quality of suggestions
 *   4. Experiment: tweak the prompts and see how output changes
 */

// =====================================================================
// Example A: String manipulation
// =====================================================================

// ❌ BAD PROMPT (vague, multiple tasks):
// TODO: do string stuff

// ✅ GOOD PROMPT (Single, Specific, Short):
// Write a function called slugify that takes a string title and returns
// a URL-safe slug: lowercase, spaces replaced with hyphens, only
// alphanumeric characters and hyphens allowed, no leading/trailing hyphens.

/**
 * Converts a title string into a URL-safe slug.
 *
 * @param title the input title string
 * @return a URL-safe slug with lowercase, hyphens replacing spaces,
 *         only alphanumeric characters and hyphens allowed
 */
public static String slugify(String title) {
    String slug = title.toLowerCase().trim();
    slug = slug.replaceAll("[^\\w\\s-]", "");
    slug = slug.replaceAll("[\\s_]+", "-");
    slug = slug.replaceAll("-+", "-");
    slug = slug.replaceAll("^-+|-+$", "");
    return slug;
}

// =====================================================================
// Example B: Data validation
// =====================================================================

// ❌ BAD PROMPT:
// TODO: validate email

// ✅ GOOD PROMPT:
// Write a function called validateEmail that takes a string and returns
// true if it matches a basic email pattern (user@domain.tld), or false
// otherwise. Do not use third-party libraries.

private static final Pattern EMAIL_PATTERN = Pattern.compile(
    "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
);

/**
 * Validates if a given string is a valid email address.
 *
 * @param email the email string to validate
 * @return true if the email matches a basic email pattern, false otherwise
 */
public static boolean validateEmail(String email) {
    if (email == null) {
        return false;
    }
    Matcher matcher = EMAIL_PATTERN.matcher(email);
    return matcher.matches();
}

// =====================================================================
// Example C: Data transformation
// =====================================================================

// ❌ BAD PROMPT:
// TODO: process the data

// ✅ GOOD PROMPT:
// Write a function called summarizeSales that takes a list of maps
// with keys "product" (String), "amount" (double), and "region" (String).
// Return a map mapping each region to its total sales amount, rounded
// to 2 decimal places.

/**
 * Summarizes sales transactions by region.
 *
 * @param transactions a list of transaction maps containing "product", "amount", and "region"
 * @return a map mapping region name to total sales amount (rounded to 2 decimals)
 */
public static Map<String, Double> summarizeSales(List<Map<String, Object>> transactions) {
    Map<String, Double> totals = new HashMap<>();
    for (Map<String, Object> tx : transactions) {
        String region = (String) tx.get("region");
        double amount = ((Number) tx.get("amount")).doubleValue();
        totals.put(region, Math.round((totals.getOrDefault(region, 0.0) + amount) * 100) / 100.0);
    }
    return totals;
}

// =====================================================================
// Example D: Context matters ("Surround")
// =====================================================================

// Below, the descriptive class name + fields + method name + types
// give Copilot all the context it needs.

/**
 * Represents an employee in the organization.
 */
public static class Employee {
    private String name;
    private String department;
    private double salary;
    private int yearsOfService;

    public Employee(String name, String department, double salary, int yearsOfService) {
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.yearsOfService = yearsOfService;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public int getYearsOfService() {
        return yearsOfService;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                ", yearsOfService=" + yearsOfService +
                '}';
    }
}

/**
 * Groups employees with >= minYears of service by department.
 *
 * @param employees the list of employees to filter
 * @param minYears the minimum years of service threshold (default 5)
 * @return a map mapping department name to list of employee names
 */
public static Map<String, List<String>> getSeniorEmployeesByDepartment(
    List<Employee> employees,
    int minYears) {
    // TODO: Let Copilot complete this — the class name + types + method name
    // + parameter names give it excellent context (the "Surround" principle).
    Map<String, List<String>> result = new HashMap<>();
    for (Employee emp : employees) {
        if (emp.getYearsOfService() >= minYears) {
            result.computeIfAbsent(emp.getDepartment(), k -> new ArrayList<>())
                  .add(emp.getName());
        }
    }
    return result;
}

// =====================================================================
// Quick self-test
// =====================================================================
public static void main(String[] args) {
    System.out.println(slugify("  Hello, World! This is a Test.  "));
    // → "hello-world-this-is-a-test"

    System.out.println(validateEmail("user@example.com"));   // true
    System.out.println(validateEmail("not-an-email"));        // false

    List<Map<String, Object>> sales = Arrays.asList(
        createTransaction("Widget", 120.50, "EMEA"),
        createTransaction("Gadget", 89.99, "APAC"),
        createTransaction("Widget", 200.00, "EMEA")
    );
    System.out.println(summarizeSales(sales));
    // → {EMEA=320.5, APAC=89.99}

    List<Employee> employees = Arrays.asList(
        new Employee("Alice", "Engineering", 120000, 6),
        new Employee("Bob", "Sales", 90000, 3),
        new Employee("Carol", "Engineering", 115000, 7),
        new Employee("David", "Sales", 95000, 5)
    );
    System.out.println(getSeniorEmployeesByDepartment(employees, 5));
    // → {Engineering=[Alice, Carol], Sales=[David]}
}

private static Map<String, Object> createTransaction(String product, double amount, String region) {
    Map<String, Object> tx = new HashMap<>();
    tx.put("product", product);
    tx.put("amount", amount);
    tx.put("region", region);
    return tx;
}
