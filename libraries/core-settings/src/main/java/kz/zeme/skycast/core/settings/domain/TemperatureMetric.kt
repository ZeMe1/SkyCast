package kz.zeme.skycast.core.settings.domain

data class TemperatureMetric(
    val isSelected: Boolean,
    val code: TemperatureUnit,
) {

    enum class TemperatureUnit(
        val code: String,
    ) {
        Celsius("celsius"),
        Fahrenheit("fahrenheit");

        companion object {

            fun find(
                code: String?,
                default: TemperatureUnit = Celsius,
            ): TemperatureUnit = entries
                .find { it.code.contentEquals(code) }
                ?: default
        }
    }
}
