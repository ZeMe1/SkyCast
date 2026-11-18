package kz.zeme.skycast.core.locale.view.presentation.navigation

import kotlinx.parcelize.Parcelize
import kz.zeme.skycast.core.navigation.Navigator

@Parcelize
sealed class LanguagePickerNavigation : Navigator.Navigation {
    data object OpenLanguagePicker : LanguagePickerNavigation()
}
