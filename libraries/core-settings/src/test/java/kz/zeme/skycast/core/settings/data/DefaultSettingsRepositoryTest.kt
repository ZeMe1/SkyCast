package kz.zeme.skycast.core.settings.data

import android.content.SharedPreferences
import kotlinx.coroutines.runBlocking
import kz.zeme.skycast.core.domain.Response
import kz.zeme.skycast.core.settings.domain.TemperatureMetric
import kz.zeme.skycast.core.settings.domain.TemperatureMetric.TemperatureUnit
import kz.zeme.skycast.core.settings.domain.WindSpeedMetric
import kz.zeme.skycast.core.settings.domain.WindSpeedMetric.WindSpeedUnit
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultSettingsRepositoryTest {

    @Test
    fun `getWeatherUnits returns Celsius and MetersPerSecond by default`() {
        val repository = createRepository()

        val units = repository.getWeatherUnits()

        assertEquals(TemperatureUnit.Celsius, units.temperatureUnit)
        assertEquals(WindSpeedUnit.MetersPerSecond, units.windSpeedUnit)
    }

    @Test
    fun `getTemperatureMetrics marks Celsius selected by default`() = runBlocking {
        val repository = createRepository()

        val response = repository.getTemperatureMetrics()

        assertTrue(response is Response.Success)
        val metrics = (response as Response.Success).data
        assertEquals(2, metrics.size)
        assertEquals(TemperatureUnit.Celsius, metrics[0].code)
        assertTrue(metrics[0].isSelected)
        assertEquals(TemperatureUnit.Fahrenheit, metrics[1].code)
        assertFalse(metrics[1].isSelected)
    }

    @Test
    fun `saveSelectedTemperatureMetric persists Fahrenheit and updates selection`() = runBlocking {
        val preferences = InMemorySharedPreferences()
        val repository = createRepository(preferences)

        val saveResponse = repository.saveSelectedTemperatureMetric(
            TemperatureMetric(isSelected = false, code = TemperatureUnit.Fahrenheit),
        )
        val metricsResponse = repository.getTemperatureMetrics()
        val units = repository.getWeatherUnits()

        assertTrue(saveResponse is Response.Success)
        assertTrue(metricsResponse is Response.Success)
        val metrics = (metricsResponse as Response.Success).data
        assertFalse(metrics[0].isSelected)
        assertTrue(metrics[1].isSelected)
        assertEquals(TemperatureUnit.Fahrenheit, units.temperatureUnit)
        assertTrue(preferences.wasCommitCalled)
    }

    @Test
    fun `getWindSpeedMetrics marks MetersPerSecond selected by default`() = runBlocking {
        val repository = createRepository()

        val response = repository.getWindSpeedMetrics()

        assertTrue(response is Response.Success)
        val metrics = (response as Response.Success).data
        assertEquals(2, metrics.size)
        assertEquals(WindSpeedUnit.MetersPerSecond, metrics[0].code)
        assertTrue(metrics[0].isSelected)
        assertEquals(WindSpeedUnit.KilometersPerHour, metrics[1].code)
        assertFalse(metrics[1].isSelected)
    }

    @Test
    fun `saveSelectedWindSpeedMetric persists KilometersPerHour and updates selection`() = runBlocking {
        val preferences = InMemorySharedPreferences()
        val repository = createRepository(preferences)

        val saveResponse = repository.saveSelectedWindSpeedMetric(
            WindSpeedMetric(isSelected = false, code = WindSpeedUnit.KilometersPerHour),
        )
        val metricsResponse = repository.getWindSpeedMetrics()
        val units = repository.getWeatherUnits()

        assertTrue(saveResponse is Response.Success)
        assertTrue(metricsResponse is Response.Success)
        val metrics = (metricsResponse as Response.Success).data
        assertFalse(metrics[0].isSelected)
        assertTrue(metrics[1].isSelected)
        assertEquals(WindSpeedUnit.KilometersPerHour, units.windSpeedUnit)
        assertTrue(preferences.wasCommitCalled)
    }

    @Test
    fun `unit find falls back to default for unknown or null code`() {
        assertEquals(TemperatureUnit.Celsius, TemperatureUnit.find(null))
        assertEquals(TemperatureUnit.Celsius, TemperatureUnit.find("unknown"))
        assertEquals(TemperatureUnit.Fahrenheit, TemperatureUnit.find("fahrenheit"))
        assertEquals(WindSpeedUnit.MetersPerSecond, WindSpeedUnit.find(null))
        assertEquals(WindSpeedUnit.MetersPerSecond, WindSpeedUnit.find("unknown"))
        assertEquals(WindSpeedUnit.KilometersPerHour, WindSpeedUnit.find("kmh"))
    }

    private fun createRepository(
        sharedPreferences: SharedPreferences = InMemorySharedPreferences(),
    ): DefaultSettingsRepository = DefaultSettingsRepository(
        settingsLocalDataSource = SettingsLocalDataSource(sharedPreferences),
    )

    private class InMemorySharedPreferences : SharedPreferences {
        private val values = mutableMapOf<String, Any?>()
        var wasCommitCalled: Boolean = false
            private set

        override fun getAll(): MutableMap<String, *> = values.toMutableMap()

        override fun getString(key: String?, defValue: String?): String? =
            (values[key] as? String) ?: defValue

        @Suppress("UNCHECKED_CAST")
        override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? =
            (values[key] as? MutableSet<String>) ?: defValues

        override fun getInt(key: String?, defValue: Int): Int =
            (values[key] as? Int) ?: defValue

        override fun getLong(key: String?, defValue: Long): Long =
            (values[key] as? Long) ?: defValue

        override fun getFloat(key: String?, defValue: Float): Float =
            (values[key] as? Float) ?: defValue

        override fun getBoolean(key: String?, defValue: Boolean): Boolean =
            (values[key] as? Boolean) ?: defValue

        override fun contains(key: String?): Boolean = values.containsKey(key)

        override fun edit(): SharedPreferences.Editor = InMemoryEditor()

        override fun registerOnSharedPreferenceChangeListener(
            listener: SharedPreferences.OnSharedPreferenceChangeListener?,
        ) = Unit

        override fun unregisterOnSharedPreferenceChangeListener(
            listener: SharedPreferences.OnSharedPreferenceChangeListener?,
        ) = Unit

        private inner class InMemoryEditor : SharedPreferences.Editor {
            private val pending = mutableMapOf<String, Any?>()
            private val removals = mutableSetOf<String>()
            private var isCleared = false

            override fun putString(key: String?, value: String?): SharedPreferences.Editor {
                if (key != null) pending[key] = value
                return this
            }

            override fun putStringSet(
                key: String?,
                values: MutableSet<String>?,
            ): SharedPreferences.Editor {
                if (key != null) pending[key] = values
                return this
            }

            override fun putInt(key: String?, value: Int): SharedPreferences.Editor {
                if (key != null) pending[key] = value
                return this
            }

            override fun putLong(key: String?, value: Long): SharedPreferences.Editor {
                if (key != null) pending[key] = value
                return this
            }

            override fun putFloat(key: String?, value: Float): SharedPreferences.Editor {
                if (key != null) pending[key] = value
                return this
            }

            override fun putBoolean(key: String?, value: Boolean): SharedPreferences.Editor {
                if (key != null) pending[key] = value
                return this
            }

            override fun remove(key: String?): SharedPreferences.Editor {
                if (key != null) removals.add(key)
                return this
            }

            override fun clear(): SharedPreferences.Editor {
                isCleared = true
                return this
            }

            override fun commit(): Boolean {
                wasCommitCalled = true
                apply()
                return true
            }

            override fun apply() {
                if (isCleared) values.clear()
                removals.forEach { values.remove(it) }
                pending.forEach { (key, value) -> values[key] = value }
            }
        }
    }
}
