package kz.zeme.skycast.core.locale.view.presentation

import kz.zeme.skycast.core.locale.domain.Language

interface LanguagePickerController {
    fun onLanguageClick(language: Language)
    fun onClose()
    fun onStart()
}
