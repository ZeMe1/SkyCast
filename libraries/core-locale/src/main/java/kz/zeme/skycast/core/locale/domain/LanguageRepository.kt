package kz.zeme.skycast.core.locale.domain

import kz.zeme.skycast.core.domain.Response

interface LanguageRepository {

    suspend fun getLanguages(): Response<List<Language>>

    suspend fun saveSelectedLanguage(language: Language): Response<Unit>
}
