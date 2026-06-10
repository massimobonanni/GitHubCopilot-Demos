// Demo 4 — Documentation Generation
// ===================================
// This module works but has NO XML doc comments, NO inline comments, NO README.
// Use Copilot to generate professional documentation.
//
// Exercises:
//   1. Select a method → Inline Chat → "Add a detailed XML doc comment"
//   2. Select the whole class → Chat → "Generate XML documentation for every method"
//   3. Ask Chat: "Write a README.md for this project"
//   4. Ask Chat: "Add <exception> tags where methods can throw"

using System.Net.Http.Json;
using System.Text.Json;

namespace Documentation;

public enum TemperatureUnit
{
    Celsius,
    Fahrenheit
}

public class WeatherClient : IDisposable
{
    private const string BaseUrl = "https://api.open-meteo.com/v1/forecast";
    private readonly HttpClient _http;
    private readonly TemperatureUnit _defaultUnit;
    private readonly Dictionary<string, (WeatherData Data, DateTime Timestamp)> _cache = new();

    public WeatherClient(TemperatureUnit defaultUnit = TemperatureUnit.Celsius)
    {
        _defaultUnit = defaultUnit;
        _http = new HttpClient();
        _http.DefaultRequestHeaders.Add("User-Agent", "GH300-Demo/1.0");
        _http.Timeout = TimeSpan.FromSeconds(10);
    }

    public async Task<WeatherData> GetTemperatureAsync(double latitude, double longitude)
    {
        var cacheKey = $"{latitude},{longitude}";
        if (_cache.TryGetValue(cacheKey, out var cached))
        {
            if ((DateTime.Now - cached.Timestamp).TotalSeconds < 300)
                return cached.Data;
        }

        var unitParam = _defaultUnit == TemperatureUnit.Celsius ? "celsius" : "fahrenheit";
        var url = $"{BaseUrl}?latitude={latitude}&longitude={longitude}"
                + $"&current_weather=true&temperature_unit={unitParam}";

        var response = await _http.GetAsync(url);
        response.EnsureSuccessStatusCode();

        var json = await response.Content.ReadFromJsonAsync<JsonElement>();
        var current = json.GetProperty("current_weather");

        var data = new WeatherData
        {
            Temperature = current.GetProperty("temperature").GetDouble(),
            WindSpeed = current.GetProperty("windspeed").GetDouble(),
            WindDirection = current.GetProperty("winddirection").GetDouble(),
            IsDay = current.GetProperty("is_day").GetInt32() == 1,
            Unit = _defaultUnit
        };

        _cache[cacheKey] = (data, DateTime.Now);
        return data;
    }

    public async Task<List<ForecastDay>> GetForecastAsync(
        double latitude, double longitude, int days = 3)
    {
        if (days < 1 || days > 16)
            throw new ArgumentOutOfRangeException(nameof(days), "Must be between 1 and 16");

        var unitParam = _defaultUnit == TemperatureUnit.Celsius ? "celsius" : "fahrenheit";
        var url = $"{BaseUrl}?latitude={latitude}&longitude={longitude}"
                + $"&daily=temperature_2m_max,temperature_2m_min,precipitation_sum"
                + $"&temperature_unit={unitParam}&forecast_days={days}";

        var response = await _http.GetAsync(url);
        response.EnsureSuccessStatusCode();

        var json = await response.Content.ReadFromJsonAsync<JsonElement>();
        var daily = json.GetProperty("daily");

        var dates = daily.GetProperty("time").EnumerateArray().Select(e => e.GetString()!).ToList();
        var highs = daily.GetProperty("temperature_2m_max").EnumerateArray().Select(e => e.GetDouble()).ToList();
        var lows = daily.GetProperty("temperature_2m_min").EnumerateArray().Select(e => e.GetDouble()).ToList();
        var rain = daily.GetProperty("precipitation_sum").EnumerateArray().Select(e => e.GetDouble()).ToList();

        return dates.Select((date, i) => new ForecastDay
        {
            Date = date,
            High = i < highs.Count ? highs[i] : null,
            Low = i < lows.Count ? lows[i] : null,
            PrecipitationMm = i < rain.Count ? rain[i] : null
        }).ToList();
    }

    public async Task<string> FormatReportAsync(double latitude, double longitude, int days = 3)
    {
        var current = await GetTemperatureAsync(latitude, longitude);
        var forecast = await GetForecastAsync(latitude, longitude, days);

        var lines = new List<string>
        {
            $"Weather Report ({latitude}, {longitude})",
            new string('=', 45),
            $"Now: {current.Temperature}° {current.Unit}",
            $"Wind: {current.WindSpeed} km/h",
            "",
            $"{days}-Day Forecast:",
            new string('-', 45)
        };

        foreach (var day in forecast)
        {
            lines.Add($"  {day.Date}  High: {day.High}°  Low: {day.Low}°  Rain: {day.PrecipitationMm} mm");
        }

        return string.Join(Environment.NewLine, lines);
    }

    public void ClearCache() => _cache.Clear();

    public void Dispose() => _http.Dispose();
}

public class WeatherData
{
    public double Temperature { get; init; }
    public double WindSpeed { get; init; }
    public double WindDirection { get; init; }
    public bool IsDay { get; init; }
    public TemperatureUnit Unit { get; init; }
}

public class ForecastDay
{
    public string Date { get; init; } = "";
    public double? High { get; init; }
    public double? Low { get; init; }
    public double? PrecipitationMm { get; init; }
}
