// Demo 1 — Code Completions
// ==========================
// GitHub Copilot suggests code as you type. Open this file in VS Code,
// place your cursor after each "// TODO" comment, and press Enter to see
// Copilot's inline suggestions. Accept with Tab, dismiss with Esc.
//
// Scenario: A small product-inventory system for a warehouse.

namespace CodeCompletions;

public record Product(
    string Sku,
    string Name,
    decimal Price,
    int Quantity = 0,
    DateTime? CreatedAt = null
)
{
    public DateTime CreatedAt { get; init; } = CreatedAt ?? DateTime.Now;
}

public class Inventory
{
    private readonly Dictionary<string, Product> _products = new();

    public void AddProduct(Product product)
    {
        if (_products.TryGetValue(product.Sku, out var existing))
        {
            _products[product.Sku] = existing with
            {
                Quantity = existing.Quantity + product.Quantity
            };
        }
        else
        {
            _products[product.Sku] = product;
        }
    }

    // -----------------------------------------------------------------
    // ✋ STOP HERE — let Copilot complete each method from the comment.
    // Place your cursor at the end of each comment and press Enter.
    // -----------------------------------------------------------------

    // TODO: Write a method called RemoveProduct that removes a product by SKU
    // and throws a KeyNotFoundException if the SKU doesn't exist.

    // TODO: Write a method called GetProduct that returns a Product by SKU
    // or null if not found.

    // TODO: Write a method called TotalValue that returns the sum of
    // Price * Quantity for every product in the inventory.

    // TODO: Write a method called LowStockReport that takes a threshold (int)
    // and returns a list of Products whose Quantity is below that threshold.

    // TODO: Write a method called ApplyDiscount that takes a SKU and a
    // percentage (decimal between 0 and 100) and reduces that product's Price.
}

// -----------------------------------------------------------------
// Bonus: place your cursor on the blank line below and start typing
// "public static class Program" — watch Copilot scaffold an entire demo.
// -----------------------------------------------------------------
