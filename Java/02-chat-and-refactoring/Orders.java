import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Demo 2 — Refactoring from Python to Java
 * ==========================================
 * This file contains the refactored version of the Python orders.py,
 * demonstrating functional equivalents in Java with proper OOP patterns.
 *
 * Key improvements:
 *   1. Type-safe Map structures instead of Python dicts
 *   2. Dedicated classes for domain objects (Order, OrderResult, ShippingRate)
 *   3. Enum for discount codes
 *   4. Early returns to reduce nesting
 *   5. Proper exception handling with custom exceptions
 */

// Domain Models
class Order {
    private List<OrderItem> items;
    private String discountCode;

    public Order(List<OrderItem> items, String discountCode) {
        this.items = items;
        this.discountCode = discountCode;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public String getDiscountCode() {
        return discountCode;
    }
}

class OrderItem {
    private String name;
    private double price;
    private int qty;

    public OrderItem(String name, double price, int qty) {
        this.name = name;
        this.price = price;
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }
}

class OrderResult {
    private double subtotal;
    private double tax;
    private double total;
    private String status;
    private String timestamp;
    private String error;
    private String warning;

    public OrderResult() {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        this.status = "pending";
    }

    // Getters and setters
    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }
}

// Discount Code Enum
enum DiscountCode {
    SAVE10(0.90),
    SAVE20(0.80),
    HALF(0.50);

    private final double multiplier;

    DiscountCode(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public static DiscountCode fromString(String code) {
        try {
            return DiscountCode.valueOf(code);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

// Shipping Rate Structure
class ShippingRate {
    private String destination;
    private int weight;
    private double cost;

    public ShippingRate(String destination, int weight) {
        this.destination = destination;
        this.weight = weight;
        calculateCost();
    }

    private void calculateCost() {
        Map<String, int[]> rates = new HashMap<>();
        rates.put("domestic", new int[]{599, 999, 1499, 2499});
        rates.put("europe", new int[]{1299, 2299, 3999, 5999});
        rates.put("international", new int[]{1999, 3499, 5499, 7999});

        if (!rates.containsKey(destination)) {
            throw new IllegalArgumentException("Unknown destination: " + destination);
        }

        int[] destRates = rates.get(destination);
        if (weight <= 1) {
            cost = destRates[0] / 100.0;
        } else if (weight <= 5) {
            cost = destRates[1] / 100.0;
        } else if (weight <= 20) {
            cost = destRates[2] / 100.0;
        } else {
            cost = destRates[3] / 100.0;
        }
    }

    public double getCost() {
        return cost;
    }
}

// Order Processor
public class Orders {

    /**
     * Process an incoming order — validate, apply discounts, return result.
     */
    public static OrderResult processOrder(Order order) {
        OrderResult result = new OrderResult();

        // Validate order
        if (order == null) {
            result.setError("Order is null");
            return result;
        }

        List<OrderItem> items = order.getItems();
        if (items == null || items.isEmpty()) {
            result.setError("Order has no items");
            return result;
        }

        // Calculate subtotal
        double subtotal = 0;
        for (OrderItem item : items) {
            subtotal += item.getPrice() * item.getQty();
        }

        // Apply discount
        if (order.getDiscountCode() != null) {
            DiscountCode discount = DiscountCode.fromString(order.getDiscountCode());
            if (discount != null) {
                subtotal *= discount.getMultiplier();
            } else {
                result.setWarning("Unknown discount code: " + order.getDiscountCode());
            }
        }

        // Calculate tax and total
        double tax = round(subtotal * 0.08, 2);
        double total = round(subtotal * 1.08, 2);

        // Set result
        result.setSubtotal(round(subtotal, 2));
        result.setTax(tax);
        result.setTotal(total);
        result.setStatus("processed");

        return result;
    }

    /**
     * Calculate shipping cost based on weight (kg) and destination zone.
     */
    public static double calculateShipping(int weight, String destination) {
        ShippingRate rate = new ShippingRate(destination, weight);
        return rate.getCost();
    }

    /**
     * Format order result into a receipt string.
     */
    public static String formatReceipt(OrderResult orderResult, String customerName) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("========================================\n");
        receipt.append("RECEIPT\n");
        receipt.append("========================================\n");
        receipt.append("Customer: ").append(customerName).append("\n");
        receipt.append("Date: ").append(orderResult.getTimestamp()).append("\n");
        receipt.append("----------------------------------------\n");
        receipt.append("Subtotal: $").append(orderResult.getSubtotal()).append("\n");
        receipt.append("Tax:      $").append(orderResult.getTax()).append("\n");
        receipt.append("Total:    $").append(orderResult.getTotal()).append("\n");
        receipt.append("========================================\n");
        return receipt.toString();
    }

    /**
     * Helper method to round doubles to 2 decimal places.
     */
    private static double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    // Demo
    public static void main(String[] args) {
        List<OrderItem> items = Arrays.asList(
            new OrderItem("Keyboard", 79.99, 1),
            new OrderItem("Mouse", 29.99, 2),
            new OrderItem("USB-C Cable", 12.49, 3)
        );

        Order order = new Order(items, "SAVE10");
        OrderResult result = processOrder(order);

        System.out.println(formatReceipt(result, "Alice Johnson"));
        System.out.printf("\nShipping (3 kg, domestic): $%.2f%n", calculateShipping(3, "domestic"));
    }
}
