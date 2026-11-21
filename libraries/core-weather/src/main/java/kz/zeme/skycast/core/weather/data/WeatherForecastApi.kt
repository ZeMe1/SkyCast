package kz.zeme.skycast.core.weather.data

import com.google.gson.annotations.SerializedName

class WeatherForecastApi {

    @SerializedName("current")
    var current: CurrentWeatherApi? = null

    @SerializedName("hourly")
    var hourly: HourlyWeatherApi? = null

    @SerializedName("daily")
    var daily: DailyWeatherApi? = null

    class CurrentWeatherApi {

        @SerializedName("time")
        var time: String? = null

        @SerializedName("temperature_2m")
        var temperature2m: Double? = null

        @SerializedName("relative_humidity_2m")
        var relativeHumidity2m: Int? = null

        @SerializedName("apparent_temperature")
        var apparentTemperature: Double? = null

        @SerializedName("is_day")
        var isDay: Int? = null

        @SerializedName("weather_code")
        var weatherCode: Int? = null

        @SerializedName("surface_pressure")
        var surfacePressure: Double? = null

        @SerializedName("wind_speed_10m")
        var windSpeed10m: Double? = null

        @SerializedName("wind_direction_10m")
        var windDirection10m: Double? = null

        @SerializedName("wind_gusts_10m")
        var windGusts10m: Double? = null

        @SerializedName("dew_point_2m")
        var dewPoint2m: Double? = null

        @SerializedName("uv_index")
        var uvIndex: Double? = null
    }

    class HourlyWeatherApi {

        @SerializedName("time")
        var time: List<String>? = null

        @SerializedName("temperature_2m")
        var temperature2m: List<Double?>? = null

        @SerializedName("precipitation_probability")
        var precipitationProbability: List<Int?>? = null

        @SerializedName("weather_code")
        var weatherCode: List<Int?>? = null

        @SerializedName("is_day")
        var isDay: List<Int?>? = null
    }

    class DailyWeatherApi {

        @SerializedName("time")
        var time: List<String>? = null

        @SerializedName("weather_code")
        var weatherCode: List<Int?>? = null

        @SerializedName("temperature_2m_max")
        var temperature2mMax: List<Double?>? = null

        @SerializedName("temperature_2m_min")
        var temperature2mMin: List<Double?>? = null

        @SerializedName("sunrise")
        var sunrise: List<String?>? = null

        @SerializedName("sunset")
        var sunset: List<String?>? = null

        @SerializedName("uv_index_max")
        var uvIndexMax: List<Double?>? = null
    }

    enum class TemperatureUnitApi {
        @SerializedName("celsius")
        Celsius,

        @SerializedName("fahrenheit")
        Fahrenheit,
    }

    enum class WindSpeedUnitApi {
        @SerializedName("ms")
        MetersPerSecond,

        @SerializedName("kmh")
        KilometersPerHour,
    }
}
