/**
 * Demo 4 — Documentation Generation
 * ===================================
 * This module works but has NO JSDoc comments, NO inline comments, NO README.
 * Use Copilot to generate professional documentation.
 *
 * Exercises:
 *   1. Select a function → Inline Chat → "Add a detailed JSDoc comment"
 *   2. Select the whole class → Chat → "Generate JSDoc for every method"
 *   3. Ask Chat: "Write a README.md for this project"
 *   4. Ask Chat: "Convert this to TypeScript with full type annotations"
 */

class WeatherClient {
  static BASE_URL = "https://api.open-meteo.com/v1/forecast";

  constructor(defaultUnit = "celsius") {
    this.defaultUnit = defaultUnit;
    this._cache = new Map();
  }

  async getTemperature(latitude, longitude) {
    const cacheKey = `${latitude},${longitude}`;
    if (this._cache.has(cacheKey)) {
      const cached = this._cache.get(cacheKey);
      const ageSeconds = (Date.now() - cached.timestamp) / 1000;
      if (ageSeconds < 300) return cached.data;
    }

    const url =
      `${WeatherClient.BASE_URL}?latitude=${latitude}&longitude=${longitude}` +
      `&current_weather=true&temperature_unit=${this.defaultUnit}`;

    const response = await fetch(url, {
      headers: { "User-Agent": "GH300-Demo/1.0" },
      signal: AbortSignal.timeout(10000),
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch weather data: ${response.status}`);
    }

    const json = await response.json();
    const weather = json.current_weather || {};

    const data = {
      temperature: weather.temperature,
      windspeed: weather.windspeed,
      windDirection: weather.winddirection,
      isDay: weather.is_day === 1,
      unit: this.defaultUnit,
    };

    this._cache.set(cacheKey, { data, timestamp: Date.now() });
    return data;
  }

  async getForecast(latitude, longitude, days = 3) {
    if (days < 1 || days > 16) {
      throw new RangeError("Forecast days must be between 1 and 16");
    }

    const url =
      `${WeatherClient.BASE_URL}?latitude=${latitude}&longitude=${longitude}` +
      `&daily=temperature_2m_max,temperature_2m_min,precipitation_sum` +
      `&temperature_unit=${this.defaultUnit}&forecast_days=${days}`;

    const response = await fetch(url, {
      headers: { "User-Agent": "GH300-Demo/1.0" },
      signal: AbortSignal.timeout(10000),
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch forecast: ${response.status}`);
    }

    const json = await response.json();
    const daily = json.daily || {};

    const dates = daily.time || [];
    const highs = daily.temperature_2m_max || [];
    const lows = daily.temperature_2m_min || [];
    const rain = daily.precipitation_sum || [];

    return dates.map((date, i) => ({
      date,
      high: highs[i] ?? null,
      low: lows[i] ?? null,
      precipitationMm: rain[i] ?? null,
    }));
  }

  async formatReport(latitude, longitude, days = 3) {
    const current = await this.getTemperature(latitude, longitude);
    const forecast = await this.getForecast(latitude, longitude, days);

    const lines = [
      `Weather Report (${latitude}, ${longitude})`,
      "=".repeat(45),
      `Now: ${current.temperature}° ${current.unit}`,
      `Wind: ${current.windspeed} km/h`,
      "",
      `${days}-Day Forecast:`,
      "-".repeat(45),
    ];

    for (const day of forecast) {
      lines.push(
        `  ${day.date}  High: ${day.high}°  Low: ${day.low}°  Rain: ${day.precipitationMm} mm`
      );
    }

    return lines.join("\n");
  }

  clearCache() {
    this._cache.clear();
  }
}

module.exports = { WeatherClient };
