import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Demo 1 — Code Completions
 * ==========================
 * GitHub Copilot suggests code as you type. Open this file in VS Code,
 * place your cursor after each "// TODO" comment, and press Enter to see
 * Copilot's inline suggestions. Accept with Tab, dismiss with Esc.
 *
 * Scenario: A small product-inventory system for a warehouse.
 */

class Product {
    private String sku;
    private String name;
    private float price;
    private int quantity;
    private LocalDateTime createdAt;

    public Product(String sku, String name, float price) {
        this(sku, name, price, 0);
    }

    public Product(String sku, String name, float price, int quantity) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.createdAt = LocalDateTime.now();
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Product{" +
                "sku='" + sku + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                '}';
    }
}

public class Inventory {
    /**
     * Manages a collection of products in a warehouse.
     */
    private Map<String, Product> products;

    public Inventory() {
        this.products = new HashMap<>();
    }

    public void addProduct(Product product) {
        /**
         * Add a new product or update quantity if it already exists.
         */
        if (products.containsKey(product.getSku())) {
            products.get(product.getSku()).setQuantity(
                    products.get(product.getSku()).getQuantity() + product.getQuantity()
            );
        } else {
            products.put(product.getSku(), product);
        }
    }

    // -----------------------------------------------------------------
    // ✋ STOP HERE — let Copilot complete each method from the comment.
    // Place your cursor at the end of each comment and press Enter.
    // -----------------------------------------------------------------

    // TODO: Write a method called removeProduct that removes a product by SKU
    // and raises a KeyError if the SKU doesn't exist.
    public void removeProduct(String sku) {
        if (!products.containsKey(sku)) {
            throw new IllegalArgumentException("Product with SKU " + sku + " does not exist.");
        }
        products.remove(sku);
    }

    // TODO: Write a method called getProduct that returns a Product by SKU
    // or null if not found.
    public Product getProduct(String sku) {
        return products.get(sku);
    }

    // TODO: Write a method called totalValue that returns the sum of
    // price * quantity for every product in the inventory.
    public float totalValue() {
        return products.values().stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum();
    }

    // TODO: Write a method called lowStockReport that takes a threshold (int)
    // and returns a list of Products whose quantity is below that threshold.
    public List<Product> lowStockReport(int threshold) {
        List<Product> lowStockProducts = new ArrayList<>();
        for (Product product : products.values()) {
            if (product.getQuantity() < threshold) {
                lowStockProducts.add(product);
            }
        }
        return lowStockProducts;
    }

    // TODO: Write a method called applyDiscount that takes a SKU and a
    // percentage (float between 0 and 100) and reduces that product's price.
    public void applyDiscount(String sku, float percentage) {
        if (!products.containsKey(sku)) {
            throw new IllegalArgumentException("Product with SKU " + sku + " does not exist.");
        }
        Product product = products.get(sku);
        float discountAmount = product.getPrice() * (percentage / 100);
        product.setPrice(product.getPrice() - discountAmount);
    }

}

// -----------------------------------------------------------------
// Demo class to run the inventory system
// -----------------------------------------------------------------

class InventoryDemo {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();

        // Add some products
        inventory.addProduct(new Product("SKU123", "Widget", 19.99f, 50));
        inventory.addProduct(new Product("SKU124", "Gadget", 29.99f, 20));
        inventory.addProduct(new Product("SKU125", "Thingamajig", 9.99f, 5));

        // Print total value
        System.out.println("Total inventory value: $" + inventory.totalValue());

        // Apply a discount
        inventory.applyDiscount("SKU123", 10);
        System.out.println("New price for SKU123: $" + inventory.getProduct("SKU123").getPrice());

        // Generate low stock report
        List<Product> lowStock = inventory.lowStockReport(10);
        System.out.println("Low stock products:");
        for (Product p : lowStock) {
            System.out.println(p);
        }

        // Remove a product
        inventory.removeProduct("SKU124");
        System.out.println("Removed SKU124. Total inventory value: $" + inventory.totalValue());
    }
}

