package kz.zeme.skycast.core.settings.domain

data class WindSpeedMetric(
    val isSelected: Boolean,
    val code: WindSpeedUnit,
) {

    enum class WindSpeedUnit(
        val code: String,
    ) {
        MetersPerSecond("ms"),
        KilometersPerHour("kmh");

        companion object {

            fun find(
                code: String?,
                default: WindSpeedUnit = MetersPerSecond,
            ): WindSpeedUnit = entries
                .find { it.code.contentEquals(code) }
                ?: default
        }
    }
}
