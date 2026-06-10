"""
Demo 3 — Test Generation
=========================
This module has fully-implemented business logic but ZERO tests.
Use Copilot to generate a comprehensive test suite.

Exercises:
  1. Open this file → Copilot Chat → "/tests" or "Generate unit tests for this file"
  2. Select a single method → Inline Chat → "Write pytest tests including edge cases"
  3. Ask Chat: "What edge cases am I missing?"
  4. Run the generated tests: pytest test_calculator.py -v
"""

import math


class Calculator:
    """A calculator with memory, history, and scientific operations."""

    def __init__(self):
        self.memory: float = 0.0
        self.history: list[str] = []

    def add(self, a: float, b: float) -> float:
        result = a + b
        self._record(f"{a} + {b} = {result}")
        return result

    def subtract(self, a: float, b: float) -> float:
        result = a - b
        self._record(f"{a} - {b} = {result}")
        return result

    def multiply(self, a: float, b: float) -> float:
        result = a * b
        self._record(f"{a} * {b} = {result}")
        return result

    def divide(self, a: float, b: float) -> float:
        if b == 0:
            raise ZeroDivisionError("Cannot divide by zero")
        result = a / b
        self._record(f"{a} / {b} = {result}")
        return result

    def power(self, base: float, exponent: float) -> float:
        result = math.pow(base, exponent)
        self._record(f"{base} ^ {exponent} = {result}")
        return result

    def sqrt(self, value: float) -> float:
        if value < 0:
            raise ValueError("Cannot compute square root of a negative number")
        result = math.sqrt(value)
        self._record(f"√{value} = {result}")
        return result

    def percentage(self, value: float, percent: float) -> float:
        """Return `percent`% of `value`. E.g. percentage(200, 15) → 30.0"""
        result = value * (percent / 100)
        self._record(f"{percent}% of {value} = {result}")
        return result

    def store(self, value: float) -> None:
        """Save a value to memory."""
        self.memory = value

    def recall(self) -> float:
        """Retrieve the stored memory value."""
        return self.memory

    def clear_memory(self) -> None:
        self.memory = 0.0

    def get_history(self) -> list[str]:
        return list(self.history)

    def clear_history(self) -> None:
        self.history.clear()

    def _record(self, entry: str) -> None:
        self.history.append(entry)


class UnitConverter:
    """Converts between common units."""

    @staticmethod
    def celsius_to_fahrenheit(c: float) -> float:
        return (c * 9 / 5) + 32

    @staticmethod
    def fahrenheit_to_celsius(f: float) -> float:
        return (f - 32) * 5 / 9

    @staticmethod
    def kg_to_lbs(kg: float) -> float:
        return kg * 2.20462

    @staticmethod
    def lbs_to_kg(lbs: float) -> float:
        return lbs / 2.20462

    @staticmethod
    def km_to_miles(km: float) -> float:
        return km * 0.621371

    @staticmethod
    def miles_to_km(miles: float) -> float:
        return miles / 0.621371
