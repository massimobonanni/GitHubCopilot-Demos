import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Demo 4 — Documentation Generation (Refactored to Java)
 * ========================================================
 * This module works but has NO docstrings, NO inline comments, and NO README.
 * Use your IDE and documentation tools to generate professional JavaDoc.
 *
 * Exercises:
 *   1. Generate JavaDoc for all public methods
 *   2. Add detailed parameter and return type documentation
 *   3. Create a README.md with usage examples
 *   4. Add @throws documentation for exception handling
 */

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

/**
 * Temperature unit enumeration for weather data.
 */
public enum TemperatureUnit {
    CELSIUS("celsius"),
    FAHRENHEIT("fahrenheit");

    private final String value;

    TemperatureUnit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

/**
 * Weather client for fetching current weather and forecast data from Open-Meteo API.
 */
public class WeatherClient {

    private static final String BASE_URL = "https://api.open-meteo.com/v1/forecast";
    private static final String USER_AGENT = "GH300-Demo/1.0";
    private static final int CACHE_DURATION_SECONDS = 300;
    private static final int REQUEST_TIMEOUT_MS = 10000;

    private final TemperatureUnit defaultUnit;
    private final Map<String, CacheEntry> cache;

    /**
     * Inner class to represent cached weather data with timestamp.
     */
    private static class CacheEntry {
        Map<String, Object> data;
        LocalDateTime timestamp;

        CacheEntry(Map<String, Object> data, LocalDateTime timestamp) {
            this.data = data;
            this.timestamp = timestamp;
        }
    }

    /**
     * Constructs a WeatherClient with default temperature unit as Celsius.
     */
    public WeatherClient() {
        this(TemperatureUnit.CELSIUS);
    }

    /**
     * Constructs a WeatherClient with specified temperature unit.
     *
     * @param defaultUnit the default temperature unit (Celsius or Fahrenheit)
     */
    public WeatherClient(TemperatureUnit defaultUnit) {
        this.defaultUnit = defaultUnit;
        this.cache = new HashMap<>();
    }

    /**
     * Fetches current temperature for the given coordinates.
     *
     * @param latitude  the latitude coordinate
     * @param longitude the longitude coordinate
     * @return a map containing temperature, windspeed, wind_direction, is_day, and unit
     * @throws ConnectionError if the API request fails
     */
    public Map<String, Object> getTemperature(double latitude, double longitude) {
        String cacheKey = latitude + "," + longitude;

        // Check cache
        if (cache.containsKey(cacheKey)) {
            CacheEntry entry = cache.get(cacheKey);
            long ageSeconds = java.time.temporal.ChronoUnit.SECONDS.between(entry.timestamp, LocalDateTime.now());
            if (ageSeconds < CACHE_DURATION_SECONDS) {
                return entry.data;
            }
        }

        String params = String.format(
            "?latitude=%f&longitude=%f&current_weather=true&temperature_unit=%s",
            latitude, longitude, defaultUnit.getValue()
        );
        String url = BASE_URL + params;

        try {
            JsonObject data = fetchJsonFromUrl(url);
            JsonObject currentWeather = data.getAsJsonObject("current_weather");

            Map<String, Object> result = new HashMap<>();
            result.put("temperature", currentWeather.get("temperature").getAsDouble());
            result.put("windspeed", currentWeather.get("windspeed").getAsDouble());
            result.put("wind_direction", currentWeather.get("winddirection").getAsDouble());
            result.put("is_day", currentWeather.get("is_day").getAsInt() == 1);
            result.put("unit", defaultUnit.getValue());

            cache.put(cacheKey, new CacheEntry(result, LocalDateTime.now()));
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch weather data: " + e.getMessage(), e);
        }
    }

    /**
     * Fetches weather forecast for the given coordinates and number of days.
     *
     * @param latitude  the latitude coordinate
     * @param longitude the longitude coordinate
     * @param days      the number of forecast days (1-16)
     * @return a list of daily forecast maps with date, high, low, and precipitation
     * @throws IllegalArgumentException if days is not between 1 and 16
     * @throws ConnectionError if the API request fails
     */
    public List<Map<String, Object>> getForecast(double latitude, double longitude, int days) {
        if (days < 1 || days > 16) {
            throw new IllegalArgumentException("Forecast days must be between 1 and 16");
        }

        String params = String.format(
            "?latitude=%f&longitude=%f&daily=temperature_2m_max,temperature_2m_min,precipitation_sum&temperature_unit=%s&forecast_days=%d",
            latitude, longitude, defaultUnit.getValue(), days
        );
        String url = BASE_URL + params;

        try {
            JsonObject data = fetchJsonFromUrl(url);
            JsonObject daily = data.getAsJsonObject("daily");

            JsonArray dates = daily.getAsJsonArray("time");
            JsonArray highs = daily.getAsJsonArray("temperature_2m_max");
            JsonArray lows = daily.getAsJsonArray("temperature_2m_min");
            JsonArray rain = daily.getAsJsonArray("precipitation_sum");

            List<Map<String, Object>> forecast = new ArrayList<>();

            for (int i = 0; i < dates.size(); i++) {
                Map<String, Object> dayForecast = new HashMap<>();
                dayForecast.put("date", dates.get(i).getAsString());
                dayForecast.put("high", i < highs.size() ? highs.get(i).getAsDouble() : null);
                dayForecast.put("low", i < lows.size() ? lows.get(i).getAsDouble() : null);
                dayForecast.put("precipitation_mm", i < rain.size() ? rain.get(i).getAsDouble() : null);

                forecast.add(dayForecast);
            }

            return forecast;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch forecast: " + e.getMessage(), e);
        }
    }

    /**
     * Formats a weather report string for the given coordinates.
     *
     * @param latitude  the latitude coordinate
     * @param longitude the longitude coordinate
     * @param days      the number of forecast days to include
     * @return a formatted weather report string
     */
    public String formatReport(double latitude, double longitude, int days) {
        Map<String, Object> current = getTemperature(latitude, longitude);
        List<Map<String, Object>> forecast = getForecast(latitude, longitude, days);

        StringBuilder report = new StringBuilder();
        report.append(String.format("Weather Report (%.2f, %.2f)%n", latitude, longitude));
        report.append("=".repeat(45)).append("\n");
        report.append(String.format("Now: %.1f° %s%n", current.get("temperature"), current.get("unit")));
        report.append(String.format("Wind: %.1f km/h%n", current.get("windspeed")));
        report.append("\n");
        report.append(String.format("%d-Day Forecast:%n", days));
        report.append("-".repeat(45)).append("\n");

        for (Map<String, Object> day : forecast) {
            report.append(String.format(
                "  %s  High: %.1f°  Low: %.1f°  Rain: %.1f mm%n",
                day.get("date"),
                day.get("high"),
                day.get("low"),
                day.get("precipitation_mm")
            ));
        }

        return report.toString();
    }

    /**
     * Clears the weather data cache.
     */
    public void clearCache() {
        cache.clear();
    }

    /**
     * Fetches JSON from the given URL with timeout.
     *
     * @param urlString the URL to fetch from
     * @return a JsonObject parsed from the response
     * @throws Exception if the request fails
     */
    private JsonObject fetchJsonFromUrl(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setConnectTimeout(REQUEST_TIMEOUT_MS);
        conn.setReadTimeout(REQUEST_TIMEOUT_MS);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return JsonParser.parseString(response.toString()).getAsJsonObject();
        }
    }
}
