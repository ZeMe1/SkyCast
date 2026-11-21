package kz.zeme.skycast.core.env.props

import org.koin.core.component.KoinComponent

@Suppress("ConstPropertyName")
object BaseSettings {

    const val baseUrl: String = "https://api.open-meteo.com/"
    const val filesUrl: String = "https://files.example.com/"

    object Property : KoinComponent {
        val baseUrl: String
            get() = getProperty(BASE_URL_PROPERTY)
        val filesUrl: String
            get() = getProperty(FILES_URL_PROPERTY)
    }

    private fun KoinComponent.getProperty(key: String): String = getKoin().getProperty(key)!!
}
