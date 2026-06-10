// Demo 3 — Test Generation
// =========================
// This module has fully-implemented business logic but ZERO tests.
// Use Copilot to generate a comprehensive test suite.
//
// Exercises:
//   1. Open this file → Copilot Chat → "/tests" or "Generate xUnit tests for this file"
//   2. Select a single method → Inline Chat → "Write xUnit tests including edge cases"
//   3. Ask Chat: "What edge cases am I missing?"
//   4. Run: dotnet test

namespace TestGeneration;

public class Calculator
{
    public double Memory { get; private set; }
    private readonly List<string> _history = new();

    public double Add(double a, double b)
    {
        var result = a + b;
        Record($"{a} + {b} = {result}");
        return result;
    }

    public double Subtract(double a, double b)
    {
        var result = a - b;
        Record($"{a} - {b} = {result}");
        return result;
    }

    public double Multiply(double a, double b)
    {
        var result = a * b;
        Record($"{a} * {b} = {result}");
        return result;
    }

    public double Divide(double a, double b)
    {
        if (b == 0)
            throw new DivideByZeroException("Cannot divide by zero");
        var result = a / b;
        Record($"{a} / {b} = {result}");
        return result;
    }

    public double Power(double baseValue, double exponent)
    {
        var result = Math.Pow(baseValue, exponent);
        Record($"{baseValue} ^ {exponent} = {result}");
        return result;
    }

    public double Sqrt(double value)
    {
        if (value < 0)
            throw new ArgumentException("Cannot compute square root of a negative number");
        var result = Math.Sqrt(value);
        Record($"√{value} = {result}");
        return result;
    }

    /// <summary>
    /// Returns <paramref name="percent"/>% of <paramref name="value"/>.
    /// E.g. Percentage(200, 15) → 30.0
    /// </summary>
    public double Percentage(double value, double percent)
    {
        var result = value * (percent / 100.0);
        Record($"{percent}% of {value} = {result}");
        return result;
    }

    public void Store(double value) => Memory = value;

    public double Recall() => Memory;

    public void ClearMemory() => Memory = 0;

    public IReadOnlyList<string> GetHistory() => _history.AsReadOnly();

    public void ClearHistory() => _history.Clear();

    private void Record(string entry) => _history.Add(entry);
}

public static class UnitConverter
{
    public static double CelsiusToFahrenheit(double c) => (c * 9.0 / 5.0) + 32;

    public static double FahrenheitToCelsius(double f) => (f - 32) * 5.0 / 9.0;

    public static double KgToLbs(double kg) => kg * 2.20462;

    public static double LbsToKg(double lbs) => lbs / 2.20462;

    public static double KmToMiles(double km) => km * 0.621371;

    public static double MilesToKm(double miles) => miles / 0.621371;
}
