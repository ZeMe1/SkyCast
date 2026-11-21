package kz.zeme.skycast.core.weather.domain

sealed class HourlyTimelineItem {
    abstract val timeIso: String

    data class Hour(
        override val timeIso: String,
        val hourLabel: String,
        val temperature: Int,
        val precipitationProbability: Int,
        val condition: WeatherCondition,
        val isDay: Boolean,
        val isCurrent: Boolean,
    ) : HourlyTimelineItem()

    data class SunEvent(
        override val timeIso: String,
        val timeLabel: String,
        val type: SunEventType,
    ) : HourlyTimelineItem()
}

enum class SunEventType {
    Sunrise,
    Sunset,
}
