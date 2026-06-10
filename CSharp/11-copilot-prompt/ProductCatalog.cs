// Demo 11 — Copilot Prompt Files
// =================================
// A Copilot Prompt File is a reusable prompt template stored as a `.prompt.md` file.
// Unlike Instructions (always-on rules) or Agents (persistent chat modes), a
// Prompt File is something you INVOKE on demand — like a named command.
//
// How it works:
//   • Create a `<name>.prompt.md` file in `.vscode/prompts/`
//   • The YAML frontmatter sets the mode, description, and tools
//   • The markdown body is the prompt sent to Copilot when you invoke it
//   • Invoke it by typing `/` in Copilot Chat and selecting the prompt name
//
// How Prompt Files differ from Agents and Instructions:
//   ┌─────────────────┬──────────────────────────────┬──────────────────────────────┐
//   │ Feature         │ When active                  │ File                         │
//   ├─────────────────┼──────────────────────────────┼──────────────────────────────┤
//   │ Instructions    │ Always — every suggestion    │ *.instructions.md            │
//   │ Agent           │ While selected in mode picker│ .vscode/*.agent.md           │
//   │ Prompt File     │ When explicitly invoked      │ .vscode/prompts/*.prompt.md  │
//   └─────────────────┴──────────────────────────────┴──────────────────────────────┘
//
// Setup for this demo:
//   1. Copy `generate-changelog.prompt.md` from this folder to:
//            .vscode/prompts/generate-changelog.prompt.md
//   2. Reload VS Code (Ctrl+Shift+P → "Developer: Reload Window")
//   3. Open Copilot Chat → type `/` → verify "generate-changelog" appears
//
// Exercises:
//   1. Open this file → open Copilot Chat
//   2. Type `/generate-changelog` and invoke it
//      → Copilot produces a structured CHANGELOG entry for this module
//   3. Modify one of the methods below, then invoke the prompt again —
//      note how it picks up your changes
//   4. Ask in Chat (without the prompt): "What changed in this module?"
//      → Compare the output quality with and without the prompt file
//   5. Bonus — edit generate-changelog.prompt.md to change the output
//      format (e.g., add an "Impact" field), reload, and run it again

namespace ProductCatalog;

// ---------------------------------------------------------------------------
// v2.1.0 — Added low-stock threshold and supplier info
// v2.0.0 — Migrated from dictionary-based to record-based model
// v1.0.0 — Initial release
// ---------------------------------------------------------------------------

public record Supplier(
    string SupplierId,
    string Name,
    string ContactEmail,
    int LeadTimeDays
);

public class Product
{
    public string ProductId { get; init; }
    public string Name { get; set; }
    public string Sku { get; init; }
    public decimal Price { get; set; }
    public int StockQuantity { get; set; }
    public string Category { get; set; }
    public Supplier? Supplier { get; set; }
    public int LowStockThreshold { get; set; } = 10;
    public DateTime CreatedAt { get; init; } = DateTime.UtcNow;
    public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;

    public Product(string productId, string name, string sku, decimal price,
                   int stockQuantity, string category)
    {
        ProductId = productId;
        Name = name;
        Sku = sku;
        Price = price;
        StockQuantity = stockQuantity;
        Category = category;
    }

    public bool IsInStock() => StockQuantity > 0;

    public bool IsLowStock() => StockQuantity > 0 && StockQuantity <= LowStockThreshold;

    public decimal ApplyDiscount(double percent)
    {
        if (percent <= 0 || percent > 100)
            throw new ArgumentOutOfRangeException(nameof(percent), "Discount must be between 0 and 100.");
        return Math.Round(Price * (decimal)(1 - percent / 100), 2);
    }
}

public class ProductCatalog
{
    private readonly Dictionary<string, Product> _products = new();

    public void AddProduct(Product product)
    {
        if (_products.ContainsKey(product.ProductId))
            throw new InvalidOperationException($"Product '{product.ProductId}' already exists.");
        _products[product.ProductId] = product;
    }

    public void RemoveProduct(string productId)
    {
        if (!_products.Remove(productId))
            throw new KeyNotFoundException($"Product '{productId}' not found.");
    }

    public Product GetProduct(string productId)
    {
        if (!_products.TryGetValue(productId, out var product))
            throw new KeyNotFoundException($"Product '{productId}' not found.");
        return product;
    }

    public IEnumerable<Product> SearchByCategory(string category) =>
        _products.Values.Where(p => p.Category == category);

    public IEnumerable<Product> GetLowStockProducts() =>
        _products.Values.Where(p => p.IsLowStock());

    public void Restock(string productId, int quantity)
    {
        if (quantity <= 0)
            throw new ArgumentOutOfRangeException(nameof(quantity), "Restock quantity must be positive.");
        var product = GetProduct(productId);
        product.StockQuantity += quantity;
        product.UpdatedAt = DateTime.UtcNow;
    }

    public void UpdatePrice(string productId, decimal newPrice)
    {
        if (newPrice < 0)
            throw new ArgumentOutOfRangeException(nameof(newPrice), "Price cannot be negative.");
        var product = GetProduct(productId);
        product.Price = newPrice;
        product.UpdatedAt = DateTime.UtcNow;
    }

    public decimal TotalInventoryValue() =>
        _products.Values.Sum(p => p.Price * p.StockQuantity);
}
