// Demo 2 — Copilot Chat & Refactoring
// ====================================
// This file contains messy but working code. Use Copilot Chat or Inline
// Chat (Ctrl+I / Cmd+I) to refactor, simplify, and modernise it.
//
// Suggested exercises:
//   1. Select ProcessOrder() → Inline Chat → "Refactor using early returns"
//   2. Select CalculateShipping() → Chat → "Simplify with a dictionary lookup"
//   3. Ask Chat: "Add XML documentation comments to all methods"
//   4. Ask Chat: "Convert these static methods into a proper service class with DI"

namespace ChatAndRefactoring;

public static class OrderProcessor
{
    public static Dictionary<string, object> ProcessOrder(Dictionary<string, object>? order)
    {
        var result = new Dictionary<string, object>();
        if (order != null)
        {
            if (order.ContainsKey("Items"))
            {
                var items = (List<Dictionary<string, object>>)order["Items"];
                if (items.Count > 0)
                {
                    decimal subtotal = 0;
                    foreach (var item in items)
                    {
                        if (item.ContainsKey("Price") && item.ContainsKey("Qty"))
                        {
                            subtotal += Convert.ToDecimal(item["Price"])
                                      * Convert.ToInt32(item["Qty"]);
                        }
                        else
                        {
                            result["Error"] = "Invalid item: missing Price or Qty";
                            return result;
                        }
                    }

                    // Apply discount
                    if (order.ContainsKey("DiscountCode"))
                    {
                        var code = order["DiscountCode"].ToString();
                        if (code == "SAVE10")
                            subtotal *= 0.9m;
                        else if (code == "SAVE20")
                            subtotal *= 0.8m;
                        else if (code == "HALF")
                            subtotal *= 0.5m;
                        else
                            result["Warning"] = "Unknown discount code";
                    }

                    result["Subtotal"] = Math.Round(subtotal, 2);
                    result["Tax"] = Math.Round(subtotal * 0.08m, 2);
                    result["Total"] = Math.Round(subtotal * 1.08m, 2);
                    result["Status"] = "processed";
                    result["Timestamp"] = DateTime.Now.ToString("o");
                }
                else
                {
                    result["Error"] = "Order has no items";
                }
            }
            else
            {
                result["Error"] = "Missing Items key";
            }
        }
        else
        {
            result["Error"] = "Order is null";
        }
        return result;
    }

    public static decimal CalculateShipping(double weight, string destination)
    {
        decimal cost = 0;
        if (destination == "domestic")
        {
            if (weight <= 1) cost = 5.99m;
            else if (weight <= 5) cost = 9.99m;
            else if (weight <= 20) cost = 14.99m;
            else cost = 24.99m;
        }
        else if (destination == "europe")
        {
            if (weight <= 1) cost = 12.99m;
            else if (weight <= 5) cost = 22.99m;
            else if (weight <= 20) cost = 39.99m;
            else cost = 59.99m;
        }
        else if (destination == "international")
        {
            if (weight <= 1) cost = 19.99m;
            else if (weight <= 5) cost = 34.99m;
            else if (weight <= 20) cost = 54.99m;
            else cost = 79.99m;
        }
        else
        {
            throw new ArgumentException($"Unknown destination: {destination}");
        }
        return cost;
    }

    public static string FormatReceipt(Dictionary<string, object> orderResult, string customerName)
    {
        var lines = new List<string>
        {
            new string('=', 40),
            "RECEIPT",
            new string('=', 40),
            $"Customer: {customerName}",
            $"Date: {orderResult.GetValueOrDefault("Timestamp", "N/A")}",
            new string('-', 40),
            $"Subtotal: ${orderResult.GetValueOrDefault("Subtotal", 0)}",
            $"Tax:      ${orderResult.GetValueOrDefault("Tax", 0)}",
            $"Total:    ${orderResult.GetValueOrDefault("Total", 0)}",
            new string('=', 40)
        };
        return string.Join(Environment.NewLine, lines);
    }
}

// Try it: run this file, then ask Copilot Chat to improve it.
public class Program
{
    public static void Main()
    {
        var order = new Dictionary<string, object>
        {
            ["Items"] = new List<Dictionary<string, object>>
            {
                new() { ["Name"] = "Keyboard", ["Price"] = 79.99m, ["Qty"] = 1 },
                new() { ["Name"] = "Mouse", ["Price"] = 29.99m, ["Qty"] = 2 },
                new() { ["Name"] = "USB-C Cable", ["Price"] = 12.49m, ["Qty"] = 3 },
            },
            ["DiscountCode"] = "SAVE10"
        };

        var result = OrderProcessor.ProcessOrder(order);
        Console.WriteLine(OrderProcessor.FormatReceipt(result, "Alice Johnson"));
        Console.WriteLine($"\nShipping (3 kg, domestic): ${OrderProcessor.CalculateShipping(3, "domestic")}");
    }
}
