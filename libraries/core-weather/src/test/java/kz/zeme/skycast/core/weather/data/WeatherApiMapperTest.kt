package kz.zeme.skycast.core.weather.data

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kz.zeme.skycast.core.settings.domain.TemperatureMetric.TemperatureUnit
import kz.zeme.skycast.core.settings.domain.WeatherUnits
import kz.zeme.skycast.core.settings.domain.WindSpeedMetric.WindSpeedUnit
import kz.zeme.skycast.core.weather.data.WeatherForecastApi.CurrentWeatherApi
import kz.zeme.skycast.core.weather.data.WeatherForecastApi.DailyWeatherApi
import kz.zeme.skycast.core.weather.data.WeatherForecastApi.HourlyWeatherApi
import kz.zeme.skycast.core.weather.domain.HourlyTimelineItem.Hour
import kz.zeme.skycast.core.weather.domain.HourlyTimelineItem.SunEvent
import kz.zeme.skycast.core.weather.domain.SunEventType
import kz.zeme.skycast.core.weather.domain.UvLevel
import kz.zeme.skycast.core.weather.domain.WeatherCondition
import kz.zeme.skycast.core.weather.domain.WindDirection
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherApiMapperTest {

    private val mapper = WeatherApiMapper()
    private val defaultUnits = WeatherUnits(
        temperatureUnit = TemperatureUnit.Celsius,
        windSpeedUnit = WindSpeedUnit.MetersPerSecond,
    )

    @Test
    fun `map translates WMO codes into domain WeatherCondition`() {
        val codeToExpected = listOf(
            0 to WeatherCondition.Clear,
            1 to WeatherCondition.MainlyClear,
            2 to WeatherCondition.PartlyCloudy,
            3 to WeatherCondition.Overcast,
            45 to WeatherCondition.Fog,
            53 to WeatherCondition.Drizzle,
            61 to WeatherCondition.Rain,
            80 to WeatherCondition.Rain,
            71 to WeatherCondition.Snow,
            95 to WeatherCondition.Thunderstorm,
            999 to WeatherCondition.Overcast,
        )

        codeToExpected.forEach { (wmoCode, expectedCondition) ->
            val api = createForecastApi(weatherCode = wmoCode)
            val result = mapper.map(api, defaultUnits)
            assertEquals("WMO code $wmoCode", expectedCondition, result.current.condition)
        }
    }

    @Test
    fun `map translates wind degrees into eight compass WindDirections`() {
        val degreesToDirection = listOf(
            0.0 to WindDirection.North,
            45.0 to WindDirection.NorthEast,
            90.0 to WindDirection.East,
            135.0 to WindDirection.SouthEast,
            180.0 to WindDirection.South,
            225.0 to WindDirection.SouthWest,
            270.0 to WindDirection.West,
            315.0 to WindDirection.NorthWest,
            359.0 to WindDirection.North,
            -45.0 to WindDirection.NorthWest,
        )

        degreesToDirection.forEach { (degrees, expectedDirection) ->
            val api = createForecastApi(windDegrees = degrees)
            val result = mapper.map(api, defaultUnits)
            assertEquals("Degrees $degrees", expectedDirection, result.current.windDirection)
        }
    }

    @Test
    fun `map translates UV index into UvLevel thresholds`() {
        val uvToLevel = listOf(
            0.0 to UvLevel.Low,
            2.0 to UvLevel.Low,
            3.0 to UvLevel.Moderate,
            5.0 to UvLevel.Moderate,
            6.0 to UvLevel.High,
            8.0 to UvLevel.VeryHigh,
            11.0 to UvLevel.Extreme,
        )

        uvToLevel.forEach { (uv, expectedLevel) ->
            val api = createForecastApi(uvIndex = uv)
            val result = mapper.map(api, defaultUnits)
            assertEquals("UV $uv", expectedLevel, result.current.uvLevel)
        }
    }

    @Test
    fun `map builds 24 hour timeline with current hour and chronological sun events`() {
        val api = createForecastApi(currentTime = "2026-11-21T16:15")

        val result = mapper.map(api, defaultUnits)
        val hours = result.hourlyTimeline.filterIsInstance<Hour>()
        val sunEvents = result.hourlyTimeline.filterIsInstance<SunEvent>()

        assertEquals(24, hours.size)
        assertTrue(hours.first().isCurrent)
        assertEquals("2026-11-21T16:00", hours.first().timeIso)
        assertEquals("16:00", hours.first().hourLabel)
        assertEquals("2026-11-22T15:00", hours.last().timeIso)
        assertEquals(1, hours.count { it.isCurrent })

        assertEquals(2, sunEvents.size)
        assertEquals(SunEventType.Sunset, sunEvents[0].type)
        assertEquals("2026-11-21T17:18", sunEvents[0].timeIso)
        assertEquals("17:18", sunEvents[0].timeLabel)
        assertEquals(SunEventType.Sunrise, sunEvents[1].type)
        assertEquals("2026-11-22T07:42", sunEvents[1].timeIso)
        assertEquals("07:42", sunEvents[1].timeLabel)

        val sunsetIndex = result.hourlyTimeline.indexOf(sunEvents[0])
        assertEquals("2026-11-21T17:00", result.hourlyTimeline[sunsetIndex - 1].timeIso)
        assertEquals("2026-11-21T18:00", result.hourlyTimeline[sunsetIndex + 1].timeIso)

        val sunriseIndex = result.hourlyTimeline.indexOf(sunEvents[1])
        assertEquals("2026-11-22T07:00", result.hourlyTimeline[sunriseIndex - 1].timeIso)
        assertEquals("2026-11-22T08:00", result.hourlyTimeline[sunriseIndex + 1].timeIso)
    }

    @Test
    fun `map calculates 7 day forecast day of week and temperature bar fractions`() {
        val api = createForecastApi()

        val result = mapper.map(api, defaultUnits)

        assertEquals(7, result.daily.size)
        assertTrue(result.daily[0].isToday)
        assertFalse(result.daily[1].isToday)
        assertEquals(DayOfWeek.SATURDAY, result.daily[0].dayOfWeek)
        assertEquals(DayOfWeek.SUNDAY, result.daily[1].dayOfWeek)
        assertEquals(0.2f, result.daily[0].rangeStartFraction, 0.001f)
        assertEquals(0.6f, result.daily[0].rangeWidthFraction, 0.001f)
        assertEquals(0.0f, result.daily[1].rangeStartFraction, 0.001f)
        assertEquals(0.5f, result.daily[1].rangeWidthFraction, 0.001f)
        assertEquals(0.5f, result.daily[2].rangeStartFraction, 0.001f)
        assertEquals(0.5f, result.daily[2].rangeWidthFraction, 0.001f)
        assertEquals(TemperatureUnit.Celsius, result.temperatureUnit)
        assertEquals(WindSpeedUnit.MetersPerSecond, result.windSpeedUnit)
    }

    private fun createForecastApi(
        currentTime: String = "2026-11-21T16:15",
        weatherCode: Int = 2,
        windDegrees: Double = 315.0,
        uvIndex: Double = 2.0,
    ): WeatherForecastApi {
        val baseHour = LocalDateTime.of(2026, 11, 21, 0, 0)
        val hourlyTimes = (0 until 48).map { offset ->
            baseHour.plusHours(offset.toLong()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
        }

        return WeatherForecastApi().apply {
            current = CurrentWeatherApi().apply {
                time = currentTime
                temperature2m = 14.2
                relativeHumidity2m = 54
                apparentTemperature = 12.1
                isDay = 1
                this.weatherCode = weatherCode
                surfacePressure = 1015.4
                windSpeed10m = 4.2
                windDirection10m = windDegrees
                windGusts10m = 7.4
                dewPoint2m = 5.1
                this.uvIndex = uvIndex
            }
            hourly = HourlyWeatherApi().apply {
                time = hourlyTimes
                temperature2m = List(48) { 10.0 + (it % 8) }
                precipitationProbability = List(48) { 10 }
                this.weatherCode = List(48) { weatherCode }
                isDay = List(48) { 1 }
            }
            daily = DailyWeatherApi().apply {
                time = listOf(
                    "2026-11-21",
                    "2026-11-22",
                    "2026-11-23",
                    "2026-11-24",
                    "2026-11-25",
                    "2026-11-26",
                    "2026-11-27",
                )
                this.weatherCode = listOf(2, 61, 3, 0, 0, 2, 3)
                temperature2mMin = listOf(2.0, 0.0, 5.0, 3.0, 4.0, 2.0, 1.0)
                temperature2mMax = listOf(8.0, 5.0, 10.0, 7.0, 8.0, 6.0, 5.0)
                sunrise = listOf(
                    "2026-11-21T07:41",
                    "2026-11-22T07:42",
                    "2026-11-23T07:43",
                    "2026-11-24T07:44",
                    "2026-11-25T07:45",
                    "2026-11-26T07:46",
                    "2026-11-27T07:47",
                )
                sunset = listOf(
                    "2026-11-21T17:18",
                    "2026-11-22T17:17",
                    "2026-11-23T17:16",
                    "2026-11-24T17:15",
                    "2026-11-25T17:14",
                    "2026-11-26T17:13",
                    "2026-11-27T17:12",
                )
                uvIndexMax = listOf(2.0, 2.0, 3.0, 4.0, 4.0, 3.0, 2.0)
            }
        }
    }
}
