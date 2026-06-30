import java.util.ArrayList;
import java.util.List;

/**
 * Demo 14 — Issue to Pull Request (the full Copilot workflow)
 * ===========================================================
 * This file contains an INTENTIONAL bug. It is the starting point for an
 * end-to-end demo: file a GitHub Issue → let Copilot fix it → open a Pull
 * Request → review → merge.
 *
 * The bug:
 *   applyDiscount() treats a PERCENTAGE discount as a flat amount. A "10%"
 *   coupon subtracts 10 currency units instead of 10% of the subtotal.
 *   See ISSUE.md in this folder for the bug report to copy into GitHub.
 *
 * Demo flow (see the language README for the full script):
 *   1. Run the program → observe the wrong total
 *   2. Create a GitHub Issue from ISSUE.md
 *   3. Assign it to the Copilot coding agent (or use Chat → "Fix issue #N")
 *   4. Review the generated branch + Pull Request
 *   5. Merge the PR and close the issue
 */
public class ShoppingCart {

    /** A single line item in the cart. */
    public record CartItem(String name, double unitPrice, int quantity) {
        public double lineTotal() {
            return unitPrice * quantity;
        }
    }

    /** A discount coupon. {@code isPercentage} decides how it is applied. */
    public record Coupon(String code, double value, boolean isPercentage) {
    }

    private final List<CartItem> items = new ArrayList<>();
    private Coupon coupon;

    public void addItem(String name, double unitPrice, int quantity) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        items.add(new CartItem(name, unitPrice, quantity));
    }

    public void applyCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public double getSubtotal() {
        return items.stream().mapToDouble(CartItem::lineTotal).sum();
    }

    /**
     * Applies the active coupon to the subtotal.
     */
    public double applyDiscount(double subtotal) {
        if (coupon == null) {
            return subtotal;
        }

        // BUG: a percentage coupon subtracts the raw value (e.g. 10) instead of
        // computing value percent OF the subtotal (subtotal * value / 100).
        double discounted = subtotal - coupon.value();

        return discounted < 0 ? 0 : discounted;
    }

    public double getTotal() {
        return applyDiscount(getSubtotal());
    }

    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem("Mechanical Keyboard", 120.00, 1);
        cart.addItem("USB-C Cable", 15.00, 2);

        cart.applyCoupon(new Coupon("SAVE10", 10, true));

        double subtotal = cart.getSubtotal();   // 150.00
        double total = cart.getTotal();

        System.out.printf("Subtotal: %.2f%n", subtotal);
        System.out.println("Coupon:   SAVE10 (10%)");
        System.out.printf("Total:    %.2f%n%n", total);
        System.out.printf("Expected total with 10%% off: %.2f%n", subtotal * 0.90);
        System.out.printf("Actual total (buggy):        %.2f%n", total);
    }
}
