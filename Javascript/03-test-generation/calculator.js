/**
 * Demo 3 — Test Generation
 * =========================
 * This module has fully-implemented business logic but ZERO tests.
 * Use Copilot to generate a comprehensive test suite.
 *
 * Exercises:
 *   1. Open this file → Copilot Chat → "/tests" or "Generate Jest tests for this file"
 *   2. Select a single method → Inline Chat → "Write Jest tests including edge cases"
 *   3. Ask Chat: "What edge cases am I missing?"
 *   4. Run: npx jest calculator.test.js
 */

class Calculator {
  constructor() {
    this.memory = 0;
    /** @type {string[]} */
    this.history = [];
  }

  add(a, b) {
    const result = a + b;
    this._record(`${a} + ${b} = ${result}`);
    return result;
  }

  subtract(a, b) {
    const result = a - b;
    this._record(`${a} - ${b} = ${result}`);
    return result;
  }

  multiply(a, b) {
    const result = a * b;
    this._record(`${a} * ${b} = ${result}`);
    return result;
  }

  divide(a, b) {
    if (b === 0) throw new Error("Cannot divide by zero");
    const result = a / b;
    this._record(`${a} / ${b} = ${result}`);
    return result;
  }

  power(base, exponent) {
    const result = Math.pow(base, exponent);
    this._record(`${base} ^ ${exponent} = ${result}`);
    return result;
  }

  sqrt(value) {
    if (value < 0) throw new Error("Cannot compute square root of a negative number");
    const result = Math.sqrt(value);
    this._record(`√${value} = ${result}`);
    return result;
  }

  /**
   * Returns `percent`% of `value`. E.g. percentage(200, 15) → 30.0
   */
  percentage(value, percent) {
    const result = value * (percent / 100);
    this._record(`${percent}% of ${value} = ${result}`);
    return result;
  }

  store(value) {
    this.memory = value;
  }

  recall() {
    return this.memory;
  }

  clearMemory() {
    this.memory = 0;
  }

  getHistory() {
    return [...this.history];
  }

  clearHistory() {
    this.history = [];
  }

  _record(entry) {
    this.history.push(entry);
  }
}

const UnitConverter = {
  celsiusToFahrenheit: (c) => (c * 9) / 5 + 32,
  fahrenheitToCelsius: (f) => ((f - 32) * 5) / 9,
  kgToLbs: (kg) => kg * 2.20462,
  lbsToKg: (lbs) => lbs / 2.20462,
  kmToMiles: (km) => km * 0.621371,
  milesToKm: (miles) => miles / 0.621371,
};

module.exports = { Calculator, UnitConverter };
