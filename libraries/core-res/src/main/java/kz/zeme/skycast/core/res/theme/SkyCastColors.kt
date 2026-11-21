package kz.zeme.skycast.core.res.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Immutable
data class SkyCastColors(
    val basicDark: Color,
    val basicGray: Color,
    val basicError: Color,
    val basicSuccess: Color,
    val inputBg: Color,
    val inputErrorBg: Color,
    val mainBg: Color,
    val mainShimmer: Color,
    val buttonPrimary: Color,
    val buttonSecondary: Color,
    val buttonDisabled: Color,
    val buttonDarkGrayed: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val gray8: Color,
    val skyTop: Color,
    val skyMid: Color,
    val skyBottom: Color,
    val sheetBg: Color,
    val cardBorder: Color,
    val divider: Color,
    val handle: Color,
    val shimmerHighlight: Color,
    val errorText: Color,
    val sunYellow: Color,
    val rainCyan: Color,
    val tempCool: Color,
    val tempMild: Color,
    val tempWarm: Color,
    val tempHot: Color,
)

val LocalStaticColors = staticCompositionLocalOf {
    SkyCastColors(
        basicDark = Basic_Dark,
        basicGray = Basic_Gray,
        basicError = Basic_Error,
        basicSuccess = Basic_Success,
        inputBg = Input_Bg,
        inputErrorBg = Input_Error_Bg,
        mainBg = Main_Bg,
        mainShimmer = Main_Shimmer,
        buttonPrimary = Button_Primary,
        buttonSecondary = Button_Secondary,
        buttonDarkGrayed = Button_Dark_Grayed,
        buttonDisabled = Button_Disabled,
        textPrimary = Text_Primary,
        textSecondary = Text_Secondary,
        gray8 = Gray_8,
        skyTop = Sky_Top,
        skyMid = Sky_Mid,
        skyBottom = Sky_Bottom,
        sheetBg = Sheet_Bg,
        cardBorder = Card_Border,
        divider = Divider,
        handle = Handle,
        shimmerHighlight = Shimmer_Highlight,
        errorText = Error_Text,
        sunYellow = Sun_Yellow,
        rainCyan = Rain_Cyan,
        tempCool = Temp_Cool,
        tempMild = Temp_Mild,
        tempWarm = Temp_Warm,
        tempHot = Temp_Hot,
    )
}

fun SkyCastColors.skyGradientBrush(): Brush = Brush.verticalGradient(
    colors = listOf(skyTop, skyMid, skyBottom),
)

fun SkyCastColors.tempBarBrush(): Brush = Brush.horizontalGradient(
    colors = listOf(tempCool, tempMild, tempWarm, tempHot),
)

@Composable
fun Color.enabled(enabled: Boolean): Color = copy(
    alpha = ContentAlpha.getAlpha(enabled),
)

@Suppress("ConstPropertyName")
object ContentAlpha {
    const val disabled: Float = 0.38f

    fun getAlpha(
        enabled: Boolean,
        disabled: Float = ContentAlpha.disabled,
    ): Float = if (enabled) 1f else disabled
}

internal val Basic_Gray = Color(color = 0xB8FFFFFF)
internal val Basic_Dark = Color(color = 0x470C366B)
internal val Input_Bg = Color(color = 0x570C366B)
internal val Input_Error_Bg = Color(color = 0x3DFF453A)
internal val Main_Bg = Color(color = 0xFF2B83E2)
internal val Basic_Error = Color(color = 0xFFFF453A)
internal val Basic_Success = Color(color = 0xFF30D158)
internal val Button_Primary = Color(color = 0x3DFFFFFF)
internal val Button_Secondary = Color(color = 0x29FFFFFF)
internal val Button_Disabled = Color(color = 0x470A2A56)
internal val Button_Dark_Grayed = Color(color = 0x6B0A2A56)
internal val Main_Shimmer = Color(color = 0x420C366B)
internal val Text_Primary = Color.White
internal val Text_Secondary = Basic_Gray
internal val Gray_8 = Color(color = 0x52FFFFFF)
internal val Sky_Top = Color(color = 0xFF1B6FD4)
internal val Sky_Mid = Color(color = 0xFF3189E5)
internal val Sky_Bottom = Color(color = 0xFF52A0EC)
internal val Sheet_Bg = Color(color = 0xFF1F5CA3)
internal val Card_Border = Color(color = 0x29FFFFFF)
internal val Divider = Color(color = 0x2EFFFFFF)
internal val Handle = Color(color = 0x61FFFFFF)
internal val Shimmer_Highlight = Color(color = 0x3DFFFFFF)
internal val Error_Text = Color(color = 0xFFFFD1CE)
internal val Sun_Yellow = Color(color = 0xFFFFD60A)
internal val Rain_Cyan = Color(color = 0xFF64D2FF)
internal val Temp_Cool = Color(color = 0xFF5AC8FA)
internal val Temp_Mild = Color(color = 0xFF5CE08C)
internal val Temp_Warm = Color(color = 0xFFFFD60A)
internal val Temp_Hot = Color(color = 0xFFFF9F0A)
