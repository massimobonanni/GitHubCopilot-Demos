import java.util.*;

/**
 * Demo 3 — Test Generation (Refactored to Java)
 * ==============================================
 * This module has fully-implemented business logic but ZERO tests.
 * Use your testing framework (JUnit 5, TestNG) to generate a comprehensive test suite.
 *
 * Exercises:
 *   1. Create a test class → Generate unit tests for Calculator
 *   2. Test single methods with edge cases
 *   3. Add tests for UnitConverter static methods
 *   4. Run tests with: mvn test or gradle test
 */

/**
 * A calculator with memory, history, and scientific operations.
 */
public class Calculator {

    private double memory;
    private List<String> history;

    public Calculator() {
        this.memory = 0.0;
        this.history = new ArrayList<>();
    }

    public double add(double a, double b) {
        double result = a + b;
        record(a + " + " + b + " = " + result);
        return result;
    }

    public double subtract(double a, double b) {
        double result = a - b;
        record(a + " - " + b + " = " + result);
        return result;
    }

    public double multiply(double a, double b) {
        double result = a * b;
        record(a + " * " + b + " = " + result);
        return result;
    }

    public double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        double result = a / b;
        record(a + " / " + b + " = " + result);
        return result;
    }

    public double power(double base, double exponent) {
        double result = Math.pow(base, exponent);
        record(base + " ^ " + exponent + " = " + result);
        return result;
    }

    public double sqrt(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Cannot compute square root of a negative number");
        }
        double result = Math.sqrt(value);
        record("√" + value + " = " + result);
        return result;
    }

    /**
     * Return {@code percent}% of {@code value}. E.g. percentage(200, 15) → 30.0
     */
    public double percentage(double value, double percent) {
        double result = value * (percent / 100);
        record(percent + "% of " + value + " = " + result);
        return result;
    }

    /**
     * Save a value to memory.
     */
    public void store(double value) {
        this.memory = value;
    }

    /**
     * Retrieve the stored memory value.
     */
    public double recall() {
        return this.memory;
    }

    /**
     * Clear the memory.
     */
    public void clearMemory() {
        this.memory = 0.0;
    }

    /**
     * Get a copy of the calculation history.
     */
    public List<String> getHistory() {
        return new ArrayList<>(this.history);
    }

    /**
     * Clear the calculation history.
     */
    public void clearHistory() {
        this.history.clear();
    }

    /**
     * Record a calculation entry in history.
     */
    private void record(String entry) {
        this.history.add(entry);
    }
}

/**
 * Converts between common units.
 */
public class UnitConverter {

    private UnitConverter() {
        // Static utility class, prevent instantiation
    }

    public static double celsiusToFahrenheit(double c) {
        return (c * 9 / 5) + 32;
    }

    public static double fahrenheitToCelsius(double f) {
        return (f - 32) * 5 / 9;
    }

    public static double kgToLbs(double kg) {
        return kg * 2.20462;
    }

    public static double lbsToKg(double lbs) {
        return lbs / 2.20462;
    }

    public static double kmToMiles(double km) {
        return km * 0.621371;
    }

    public static double milesToKm(double miles) {
        return miles / 0.621371;
    }
}
