package kz.zeme.skycast.core.locale.domain

import java.util.Locale

interface LocaleRepository {

    fun getSelectedLocale(): Locale
}
