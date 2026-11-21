package kz.zeme.skycast.core.weather.data

import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.math.roundToInt
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kz.zeme.skycast.core.settings.domain.WeatherUnits
import kz.zeme.skycast.core.weather.data.WeatherForecastApi.CurrentWeatherApi
import kz.zeme.skycast.core.weather.data.WeatherForecastApi.DailyWeatherApi
import kz.zeme.skycast.core.weather.data.WeatherForecastApi.HourlyWeatherApi
import kz.zeme.skycast.core.weather.domain.CurrentWeather
import kz.zeme.skycast.core.weather.domain.DailyForecast
import kz.zeme.skycast.core.weather.domain.HourlyTimelineItem
import kz.zeme.skycast.core.weather.domain.HourlyTimelineItem.Hour
import kz.zeme.skycast.core.weather.domain.HourlyTimelineItem.SunEvent
import kz.zeme.skycast.core.weather.domain.SunEventType
import kz.zeme.skycast.core.weather.domain.UvLevel
import kz.zeme.skycast.core.weather.domain.WeatherCondition
import kz.zeme.skycast.core.weather.domain.WeatherForecast
import kz.zeme.skycast.core.weather.domain.WindDirection

class WeatherApiMapper {

    companion object {
        private const val HOURS_IN_TIMELINE = 24
        private const val HOUR_PREFIX_LENGTH = 13
        private const val TIME_LABEL_LENGTH = 5
        private const val FULL_CIRCLE_DEGREES = 360.0
        private const val WIND_SECTOR_OFFSET_DEGREES = 22.5
        private const val WIND_SECTOR_DEGREES = 45
    }

    fun map(
        from: WeatherForecastApi,
        units: WeatherUnits,
    ): WeatherForecast = WeatherForecast(
        current = mapCurrent(from.current, from.daily),
        hourlyTimeline = mapHourlyTimeline(
            currentTimeIso = from.current?.time.orEmpty(),
            hourly = from.hourly,
            daily = from.daily,
        ),
        daily = mapDaily(from.daily),
        temperatureUnit = units.temperatureUnit,
        windSpeedUnit = units.windSpeedUnit,
    )

    private fun mapCurrent(
        current: CurrentWeatherApi?,
        daily: DailyWeatherApi?,
    ): CurrentWeather {
        val currentTemp = current?.temperature2m?.roundToInt() ?: 0
        val minTemp = daily?.temperature2mMin?.firstOrNull()?.roundToInt() ?: currentTemp
        val maxTemp = daily?.temperature2mMax?.firstOrNull()?.roundToInt() ?: currentTemp
        val uvIndex = current?.uvIndex?.roundToInt() ?: 0

        return CurrentWeather(
            temperature = currentTemp,
            apparentTemperature = current?.apparentTemperature?.roundToInt() ?: currentTemp,
            minTemperature = minTemp,
            maxTemperature = maxTemp,
            condition = mapCondition(current?.weatherCode),
            isDay = current?.isDay == 1,
            windSpeed = current?.windSpeed10m ?: 0.0,
            windDirection = mapWindDirection(current?.windDirection10m),
            windGusts = current?.windGusts10m?.roundToInt() ?: 0,
            humidity = current?.relativeHumidity2m ?: 0,
            dewPoint = current?.dewPoint2m?.roundToInt() ?: currentTemp,
            pressureHpa = current?.surfacePressure?.roundToInt() ?: 0,
            uvIndex = uvIndex,
            uvLevel = mapUvLevel(uvIndex),
        )
    }

    private fun mapHourlyTimeline(
        currentTimeIso: String,
        hourly: HourlyWeatherApi?,
        daily: DailyWeatherApi?,
    ): ImmutableList<HourlyTimelineItem> {
        val times = hourly?.time.orEmpty()
        if (times.isEmpty()) return persistentListOf()

        val startIndex = findStartHourIndex(times, currentTimeIso)
        val endIndex = (startIndex + HOURS_IN_TIMELINE).coerceAtMost(times.size)

        val hourItems = (startIndex until endIndex).map { index ->
            val timeIso = times[index]
            Hour(
                timeIso = timeIso,
                hourLabel = formatTimeLabel(timeIso),
                temperature = hourly?.temperature2m?.getOrNull(index)?.roundToInt() ?: 0,
                precipitationProbability = hourly?.precipitationProbability?.getOrNull(index) ?: 0,
                condition = mapCondition(hourly?.weatherCode?.getOrNull(index)),
                isDay = hourly?.isDay?.getOrNull(index) == 1,
                isCurrent = index == startIndex,
            )
        }

        val firstHourIso = hourItems.first().timeIso
        val lastHourIso = hourItems.last().timeIso
        val sunEvents = extractSunEvents(
            timestamps = daily?.sunrise,
            type = SunEventType.Sunrise,
            firstHourIso = firstHourIso,
            lastHourIso = lastHourIso,
        ) + extractSunEvents(
            timestamps = daily?.sunset,
            type = SunEventType.Sunset,
            firstHourIso = firstHourIso,
            lastHourIso = lastHourIso,
        )

        return (hourItems + sunEvents)
            .sortedBy { it.timeIso }
            .toImmutableList()
    }

    private fun findStartHourIndex(
        times: List<String>,
        currentTimeIso: String,
    ): Int {
        if (currentTimeIso.isBlank()) return 0

        val prefix = currentTimeIso.take(HOUR_PREFIX_LENGTH)
        val exactIndex = times.indexOfFirst { it.startsWith(prefix) }
        if (exactIndex >= 0) return exactIndex

        return times.indexOfFirst { it >= prefix }.takeIf { it >= 0 } ?: 0
    }

    private fun extractSunEvents(
        timestamps: List<String?>?,
        type: SunEventType,
        firstHourIso: String,
        lastHourIso: String,
    ): List<SunEvent> = timestamps.orEmpty()
        .filterNotNull()
        .filter { it > firstHourIso && it <= lastHourIso }
        .map { timeIso ->
            SunEvent(
                timeIso = timeIso,
                timeLabel = formatTimeLabel(timeIso),
                type = type,
            )
        }

    private fun mapDaily(daily: DailyWeatherApi?): ImmutableList<DailyForecast> {
        val dates = daily?.time.orEmpty()
        if (dates.isEmpty()) return persistentListOf()

        val minTemps = dates.indices.map { index ->
            daily?.temperature2mMin?.getOrNull(index)?.roundToInt() ?: 0
        }
        val maxTemps = dates.indices.map { index ->
            daily?.temperature2mMax?.getOrNull(index)?.roundToInt() ?: 0
        }
        val weekMin = minTemps.min()
        val weekMax = maxTemps.max()
        val totalSpan = (weekMax - weekMin).toFloat()

        return dates.indices.map { index ->
            val dayMin = minTemps[index]
            val dayMax = maxTemps[index]
            val startFraction =
                if (totalSpan <= 0f) 0f
                else ((dayMin - weekMin) / totalSpan).coerceIn(0f, 1f)
            val widthFraction =
                if (totalSpan <= 0f) 1f
                else ((dayMax - dayMin) / totalSpan).coerceIn(0f, 1f - startFraction)

            DailyForecast(
                dateIso = dates[index],
                dayOfWeek = parseDayOfWeek(dates[index]),
                isToday = index == 0,
                condition = mapCondition(daily?.weatherCode?.getOrNull(index)),
                minTemperature = dayMin,
                maxTemperature = dayMax,
                rangeStartFraction = startFraction,
                rangeWidthFraction = widthFraction,
            )
        }.toImmutableList()
    }

    private fun formatTimeLabel(timeIso: String): String =
        timeIso.substringAfter('T', timeIso).take(TIME_LABEL_LENGTH)

    private fun parseDayOfWeek(dateIso: String): DayOfWeek =
        runCatching { LocalDate.parse(dateIso).dayOfWeek }.getOrDefault(DayOfWeek.MONDAY)

    private fun mapCondition(weatherCode: Int?): WeatherCondition = when (weatherCode) {
        0 -> WeatherCondition.Clear
        1 -> WeatherCondition.MainlyClear
        2 -> WeatherCondition.PartlyCloudy
        3 -> WeatherCondition.Overcast
        45, 48 -> WeatherCondition.Fog
        in 51..57 -> WeatherCondition.Drizzle
        in 61..67, in 80..82 -> WeatherCondition.Rain
        in 71..77, 85, 86 -> WeatherCondition.Snow
        in 95..99 -> WeatherCondition.Thunderstorm
        else -> WeatherCondition.Overcast
    }

    private fun mapWindDirection(degrees: Double?): WindDirection {
        val raw = degrees ?: return WindDirection.North
        val normalized = (raw % FULL_CIRCLE_DEGREES + FULL_CIRCLE_DEGREES) % FULL_CIRCLE_DEGREES
        val sectorIndex =
            ((normalized + WIND_SECTOR_OFFSET_DEGREES).toInt() / WIND_SECTOR_DEGREES) % WindDirection.entries.size
        return WindDirection.entries[sectorIndex]
    }

    private fun mapUvLevel(uvIndex: Int): UvLevel = when {
        uvIndex <= 2 -> UvLevel.Low
        uvIndex <= 5 -> UvLevel.Moderate
        uvIndex <= 7 -> UvLevel.High
        uvIndex <= 10 -> UvLevel.VeryHigh
        else -> UvLevel.Extreme
    }
}
