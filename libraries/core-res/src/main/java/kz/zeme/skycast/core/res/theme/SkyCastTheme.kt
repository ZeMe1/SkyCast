package kz.zeme.skycast.core.res.theme

import androidx.compose.runtime.Composable

object SkyCastTheme {

    val colors: SkyCastColors
        @Composable
        get() = LocalStaticColors.current

    val typography: SkyCastTypography
        @Composable
        get() = LocalStaticTypography.current
}
