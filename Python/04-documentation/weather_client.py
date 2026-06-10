"""
Demo 4 — Documentation Generation
===================================
This module works but has NO docstrings, NO inline comments, and NO README.
Use Copilot to generate professional documentation.

Exercises:
  1. Select a function → Inline Chat → "Add a detailed docstring"
  2. Select the whole class → Chat → "Generate Google-style docstrings for every method"
  3. Ask Chat: "Write a README.md for this project"
  4. Ask Chat: "Add type hints and document the return types"
"""

import json
from datetime import datetime
from enum import Enum
from urllib.request import urlopen, Request
from urllib.error import URLError


class TemperatureUnit(Enum):
    CELSIUS = "celsius"
    FAHRENHEIT = "fahrenheit"


class WeatherClient:

    BASE_URL = "https://api.open-meteo.com/v1/forecast"

    def __init__(self, default_unit: TemperatureUnit = TemperatureUnit.CELSIUS):
        self.default_unit = default_unit
        self._cache = {}

    def get_temperature(self, latitude, longitude):
        cache_key = f"{latitude},{longitude}"
        if cache_key in self._cache:
            cached = self._cache[cache_key]
            age = (datetime.now() - cached["timestamp"]).seconds
            if age < 300:
                return cached["data"]

        params = (
            f"?latitude={latitude}&longitude={longitude}"
            f"&current_weather=true"
            f"&temperature_unit={self.default_unit.value}"
        )
        url = self.BASE_URL + params

        try:
            req = Request(url, headers={"User-Agent": "GH300-Demo/1.0"})
            with urlopen(req, timeout=10) as response:
                data = json.loads(response.read().decode())
        except URLError as e:
            raise ConnectionError(f"Failed to fetch weather data: {e}")

        weather = data.get("current_weather", {})
        result = {
            "temperature": weather.get("temperature"),
            "windspeed": weather.get("windspeed"),
            "wind_direction": weather.get("winddirection"),
            "is_day": weather.get("is_day") == 1,
            "unit": self.default_unit.value,
        }

        self._cache[cache_key] = {"data": result, "timestamp": datetime.now()}
        return result

    def get_forecast(self, latitude, longitude, days=3):
        if days < 1 or days > 16:
            raise ValueError("Forecast days must be between 1 and 16")

        params = (
            f"?latitude={latitude}&longitude={longitude}"
            f"&daily=temperature_2m_max,temperature_2m_min,precipitation_sum"
            f"&temperature_unit={self.default_unit.value}"
            f"&forecast_days={days}"
        )
        url = self.BASE_URL + params

        try:
            req = Request(url, headers={"User-Agent": "GH300-Demo/1.0"})
            with urlopen(req, timeout=10) as response:
                data = json.loads(response.read().decode())
        except URLError as e:
            raise ConnectionError(f"Failed to fetch forecast: {e}")

        daily = data.get("daily", {})
        forecast = []
        dates = daily.get("time", [])
        highs = daily.get("temperature_2m_max", [])
        lows = daily.get("temperature_2m_min", [])
        rain = daily.get("precipitation_sum", [])

        for i in range(len(dates)):
            forecast.append({
                "date": dates[i],
                "high": highs[i] if i < len(highs) else None,
                "low": lows[i] if i < len(lows) else None,
                "precipitation_mm": rain[i] if i < len(rain) else None,
            })

        return forecast

    def format_report(self, latitude, longitude, days=3):
        current = self.get_temperature(latitude, longitude)
        forecast = self.get_forecast(latitude, longitude, days)

        lines = []
        lines.append(f"Weather Report ({latitude}, {longitude})")
        lines.append("=" * 45)
        lines.append(f"Now: {current['temperature']}° {current['unit']}")
        lines.append(f"Wind: {current['windspeed']} km/h")
        lines.append("")
        lines.append(f"{days}-Day Forecast:")
        lines.append("-" * 45)

        for day in forecast:
            lines.append(
                f"  {day['date']}  "
                f"High: {day['high']}°  Low: {day['low']}°  "
                f"Rain: {day['precipitation_mm']} mm"
            )

        return "\n".join(lines)

    def clear_cache(self):
        self._cache.clear()
