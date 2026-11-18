package kz.zeme.skycast.core.locale.data

import android.content.res.Resources
import kz.zeme.skycast.core.activity.ActivityProvider
import kz.zeme.skycast.core.activity.extensions.setLocale
import kz.zeme.skycast.core.domain.Response
import kz.zeme.skycast.core.locale.domain.Language
import kz.zeme.skycast.core.locale.domain.Language.LanguageCode
import kz.zeme.skycast.core.locale.domain.Language.LanguageCode.En
import kz.zeme.skycast.core.locale.domain.Language.LanguageCode.Kz
import kz.zeme.skycast.core.locale.domain.Language.LanguageCode.Ru
import kz.zeme.skycast.core.locale.domain.LanguageRepository
import kz.zeme.skycast.core.locale.domain.LocaleRepository
import java.util.Locale

class DefaultLanguageRepository(
    private val activityProvider: ActivityProvider,
    private val languageLocalDataSource: LanguageLocalDataSource,
) : LanguageRepository, LocaleRepository {

    override suspend fun getLanguages(): Response<List<Language>> {
        val selectedLanguageCode = languageLocalDataSource
            .getSelectedLanguageCode(defaultCode = getDefaultCode())

        return Response.Success(
            listOf(
                Language(
                    isSelected = selectedLanguageCode == Kz,
                    code = Kz,
                ),
                Language(
                    isSelected = selectedLanguageCode == Ru,
                    code = Ru,
                ),
                Language(
                    isSelected = selectedLanguageCode == En,
                    code = En,
                ),
            ),
        )
    }

    override suspend fun saveSelectedLanguage(language: Language): Response<Unit> {
        applyLanguage(language)
        return Response.Success(Unit)
    }

    override fun getSelectedLocale(): Locale {
        val languageCode = languageLocalDataSource
            .getSelectedLanguageCode(defaultCode = getDefaultCode())

        return Locale.forLanguageTag(languageCode.code)
    }

    private fun getDefaultCode(): LanguageCode {
        val systemLanguageCode = Resources.getSystem()
            .configuration
            .locales[0]
            .language

        return LanguageCode.find(
            code = systemLanguageCode,
            default = Ru,
        )
    }

    private fun applyLanguage(language: Language) {
        activityProvider.currentActivity?.setLocale(Locale.forLanguageTag(language.code.code))
        languageLocalDataSource.saveSelectedLanguageCode(language.code)
    }
}
