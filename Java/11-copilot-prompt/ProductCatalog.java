import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Demo 11 — Copilot Prompt Files
 * ================================
 * A Copilot Prompt File is a reusable prompt template stored as a `.prompt.md` file.
 * Unlike Instructions (always-on rules) or Agents (persistent chat modes), a
 * Prompt File is something you INVOKE on demand — like a named command.
 *
 * How it works:
 *   • Create a <name>.prompt.md file in .vscode/prompts/
 *   • The YAML frontmatter sets the mode, description, and tools
 *   • The markdown body is the prompt sent to Copilot when you invoke it
 *   • Invoke it by typing `/` in Copilot Chat and selecting the prompt name
 *
 * How Prompt Files differ from Agents and Instructions:
 *   ┌─────────────────┬──────────────────────────────┬──────────────────────────────┐
 *   │ Feature         │ When active                  │ File                         │
 *   ├─────────────────┼──────────────────────────────┼──────────────────────────────┤
 *   │ Instructions    │ Always — every suggestion    │ *.instructions.md            │
 *   │ Agent           │ While selected in mode picker│ .vscode/*.agent.md           │
 *   │ Prompt File     │ When explicitly invoked      │ .vscode/prompts/*.prompt.md  │
 *   └─────────────────┴──────────────────────────────┴──────────────────────────────┘
 *
 * Setup for this demo:
 *   1. Copy generate-changelog.prompt.md from this folder to:
 *      .vscode/prompts/generate-changelog.prompt.md
 *   2. Reload VS Code (Ctrl+Shift+P → "Developer: Reload Window")
 *   3. Open Copilot Chat → type `/` → verify "generate-changelog" appears
 *
 * Exercises:
 *   1. Open this file → open Copilot Chat
 *   2. Type `/generate-changelog` and invoke it
 *      → Copilot produces a structured CHANGELOG entry for this module
 *   3. Modify one of the methods below, then invoke the prompt again —
 *      note how it picks up your changes
 *   4. Ask in Chat (without the prompt): "What changed in this module?"
 *      → Compare the output quality with and without the prompt file
 *   5. Bonus — edit generate-changelog.prompt.md to change the output
 *      format (e.g., add an "Impact" field), reload, and run it again
 */

// ─────────────────────────────────────────────────────────────────────
// Changelog:
// v2.1.0 — Added low-stock threshold and supplier info
// v2.0.0 — Migrated to proper classes instead of maps
// v1.0.0 — Initial release
// ─────────────────────────────────────────────────────────────────────

/**
 * Represents a product supplier.
 */
class Supplier {
    private String supplierId;
    private String name;
    private String contactEmail;
    private int leadTimeDays;

    public Supplier(String supplierId, String name, String contactEmail, int leadTimeDays) {
        this.supplierId = supplierId;
        this.name = name;
        this.contactEmail = contactEmail;
        this.leadTimeDays = leadTimeDays;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public String getName() {
        return name;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public int getLeadTimeDays() {
        return leadTimeDays;
    }
}

/**
 * Represents a product in the catalog.
 */
class Product {
    private String productId;
    private String name;
    private String sku;
    private double price;
    private int stockQuantity;
    private String category;
    private Supplier supplier;
    private int lowStockThreshold;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product(String productId, String name, String sku, double price,
                   int stockQuantity, String category) {
        this.productId = productId;
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.supplier = null;
        this.lowStockThreshold = 10;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getSku() {
        return sku;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        this.updatedAt = LocalDateTime.now();
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
        this.updatedAt = LocalDateTime.now();
    }

    public String getCategory() {
        return category;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(int lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Checks if the product is in stock.
     *
     * @return true if stock quantity is greater than 0
     */
    public boolean isInStock() {
        return stockQuantity > 0;
    }

    /**
     * Checks if the product is in low stock.
     *
     * @return true if stock is above 0 but below or equal to low stock threshold
     */
    public boolean isLowStock() {
        return stockQuantity > 0 && stockQuantity <= lowStockThreshold;
    }

    /**
     * Calculates the price after applying a discount.
     *
     * @param percent the discount percentage (0-100)
     * @return the discounted price rounded to 2 decimal places
     * @throws IllegalArgumentException if percent is not between 0 and 100
     */
    public double applyDiscount(double percent) {
        if (percent <= 0 || percent > 100) {
            throw new IllegalArgumentException(
                String.format("Discount must be between 0 and 100, got %.2f", percent)
            );
        }
        return Math.round(price * (1 - percent / 100) * 100.0) / 100.0;
    }
}

/**
 * Manages a catalog of products.
 */
public class ProductCatalog {
    private Map<String, Product> products;

    /**
     * Constructs an empty product catalog.
     */
    public ProductCatalog() {
        this.products = new HashMap<>();
    }

    /**
     * Adds a product to the catalog.
     *
     * @param product the product to add
     * @throws IllegalArgumentException if a product with the same ID already exists
     */
    public void addProduct(Product product) {
        if (products.containsKey(product.getProductId())) {
            throw new IllegalArgumentException(
                String.format("Product '%s' already exists", product.getProductId())
            );
        }
        products.put(product.getProductId(), product);
    }

    /**
     * Removes a product from the catalog.
     *
     * @param productId the ID of the product to remove
     * @throws NoSuchElementException if the product is not found
     */
    public void removeProduct(String productId) {
        if (!products.containsKey(productId)) {
            throw new NoSuchElementException(
                String.format("Product '%s' not found", productId)
            );
        }
        products.remove(productId);
    }

    /**
     * Retrieves a product by ID.
     *
     * @param productId the ID of the product
     * @return the product
     * @throws NoSuchElementException if the product is not found
     */
    public Product getProduct(String productId) {
        if (!products.containsKey(productId)) {
            throw new NoSuchElementException(
                String.format("Product '%s' not found", productId)
            );
        }
        return products.get(productId);
    }

    /**
     * Searches for products by category.
     *
     * @param category the category to search for
     * @return a list of products in the specified category
     */
    public List<Product> searchByCategory(String category) {
        return products.values().stream()
            .filter(p -> p.getCategory().equals(category))
            .collect(Collectors.toList());
    }

    /**
     * Gets all products with low stock.
     *
     * @return a list of products with low stock
     */
    public List<Product> getLowStockProducts() {
        return products.values().stream()
            .filter(Product::isLowStock)
            .collect(Collectors.toList());
    }

    /**
     * Restocks a product by adding quantity.
     *
     * @param productId the ID of the product to restock
     * @param quantity the quantity to add
     * @throws IllegalArgumentException if quantity is not positive
     * @throws NoSuchElementException if the product is not found
     */
    public void restock(String productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Restock quantity must be positive");
        }
        Product product = getProduct(productId);
        product.setStockQuantity(product.getStockQuantity() + quantity);
    }

    /**
     * Updates the price of a product.
     *
     * @param productId the ID of the product
     * @param newPrice the new price
     * @throws IllegalArgumentException if the price is negative
     * @throws NoSuchElementException if the product is not found
     */
    public void updatePrice(String productId, double newPrice) {
        if (newPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        Product product = getProduct(productId);
        product.setPrice(newPrice);
    }

    /**
     * Calculates the total value of all inventory.
     *
     * @return the sum of (price * stock quantity) for all products
     */
    public double totalInventoryValue() {
        return products.values().stream()
            .mapToDouble(p -> p.getPrice() * p.getStockQuantity())
            .sum();
    }
}
