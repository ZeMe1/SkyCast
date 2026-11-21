package kz.zeme.skycast.settings.presentation.main

import androidx.annotation.StringRes
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import kz.zeme.skycast.core.error.BaseError
import kz.zeme.skycast.core.error.UnexpectedError
import kz.zeme.skycast.core.locale.domain.Language.LanguageCode
import kz.zeme.skycast.core.observable.ViewState
import kz.zeme.skycast.core.res.R
import kz.zeme.skycast.core.res.theme.SkyCastTheme
import kz.zeme.skycast.core.res.theme.primary
import kz.zeme.skycast.core.res.theme.secondary
import kz.zeme.skycast.core.res.theme.skyGradientBrush
import kz.zeme.skycast.core.settings.domain.TemperatureMetric.TemperatureUnit
import kz.zeme.skycast.core.settings.domain.WindSpeedMetric.WindSpeedUnit
import kz.zeme.skycast.core.ui.components.DefaultErrorScreen
import kz.zeme.skycast.core.ui.extensions.compose.clickableHaptic
import kz.zeme.skycast.core.ui.extensions.shimmer

@Composable
internal fun SettingsScreen(
    state: State<ViewState<SettingsState>?>,
    controller: SettingsController,
) {
    SettingsScreen(
        state = state.value,
        controller = controller,
    )
}

@Composable
private fun SettingsScreen(
    state: ViewState<SettingsState>?,
    controller: SettingsController,
    scrollState: ScrollState = rememberScrollState(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SkyCastTheme.colors.skyGradientBrush())
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        SettingsTopBar(onBackClick = controller::onBackPressed)

        when (state) {
            null,
            is ViewState.Loading.Skeleton -> SettingsShimmerView(
                modifier = Modifier.fillMaxSize(),
            )

            is ViewState.Error -> DefaultErrorView(
                modifier = Modifier.fillMaxSize(),
                error = state.error,
                onRefresh = controller::onStart,
            )

            is ViewState.Loading.ProgressBar,
            is ViewState.Data -> state.data?.let { data ->
                SettingsContent(
                    data = data,
                    controller = controller,
                    scrollState = scrollState,
                )
            } ?: SettingsShimmerView(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun SettingsTopBar(
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            modifier = Modifier
                .size(24.dp)
                .clickableHaptic(onClick = onBackClick),
            painter = painterResource(R.drawable.ic_arrow_left),
            contentDescription = null,
            tint = SkyCastTheme.colors.textPrimary,
        )

        Text(
            text = stringResource(R.string.settings_title),
            style = SkyCastTheme.typography.titleMedium.primary(),
        )
    }
}

@Composable
private fun SettingsContent(
    data: SettingsState,
    controller: SettingsController,
    scrollState: ScrollState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 24.dp),
    ) {
        SettingsGroupLabel(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp),
            titleRes = R.string.settings_group_units,
        )

        SettingsCard {
            SettingsRow(
                titleRes = R.string.settings_temperature,
                valueRes = data.temperatureUnit.toShortStringRes(),
                onClick = controller::onTemperatureUnitClick,
            )
            HorizontalDivider(
                color = SkyCastTheme.colors.divider,
                thickness = 1.dp,
            )
            SettingsRow(
                titleRes = R.string.settings_wind_speed,
                valueRes = data.windSpeedUnit.toShortStringRes(),
                onClick = controller::onWindSpeedUnitClick,
            )
        }

        SettingsGroupLabel(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp),
            titleRes = R.string.settings_group_app,
        )

        SettingsCard {
            SettingsRow(
                titleRes = R.string.settings_app_language,
                valueRes = data.languageCode.toDisplayStringRes(),
                onClick = controller::onLanguageClick,
            )
        }
    }
}

@Composable
private fun SettingsGroupLabel(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
) {
    Text(
        modifier = modifier,
        text = stringResource(titleRes),
        style = SkyCastTheme.typography.labelLarge.secondary(),
    )
}

@Composable
private fun SettingsCard(
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SkyCastTheme.colors.basicDark)
            .border(
                width = 1.dp,
                color = SkyCastTheme.colors.cardBorder,
                shape = RoundedCornerShape(14.dp),
            ),
        content = content,
    )
}

@Composable
private fun SettingsRow(
    @StringRes titleRes: Int,
    @StringRes valueRes: Int,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableHaptic(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(titleRes),
            style = SkyCastTheme.typography.bodyLarge.primary(),
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(valueRes),
                style = SkyCastTheme.typography.bodyMedium.secondary(),
            )
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = SkyCastTheme.colors.textSecondary,
            )
        }
    }
}

@Composable
private fun SettingsShimmerView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
    ) {
        Spacer(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 8.dp)
                .width(140.dp)
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer(),
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(106.dp)
                .clip(RoundedCornerShape(14.dp))
                .shimmer(),
        )
        Spacer(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 8.dp)
                .width(100.dp)
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer(),
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(14.dp))
                .shimmer(),
        )
    }
}

@Composable
private fun DefaultErrorView(
    modifier: Modifier = Modifier,
    error: BaseError? = null,
    onRefresh: () -> Unit,
) {
    DefaultErrorScreen(
        modifier = modifier,
        errorMessage = error?.message ?: stringResource(R.string.shared_something_went_wrong),
        onRefresh = onRefresh,
        backgroundColor = Color.Transparent,
    )
}

private val settingsControllerStub = object : SettingsController {
    override fun onStart() = Unit
    override fun onBackPressed() = Unit
    override fun onTemperatureUnitClick() = Unit
    override fun onWindSpeedUnitClick() = Unit
    override fun onLanguageClick() = Unit
}

private val settingsStateDefaultStub = SettingsState(
    temperatureUnit = TemperatureUnit.Celsius,
    windSpeedUnit = WindSpeedUnit.MetersPerSecond,
    languageCode = LanguageCode.Ru,
)

private val settingsStateAlternateStub = SettingsState(
    temperatureUnit = TemperatureUnit.Fahrenheit,
    windSpeedUnit = WindSpeedUnit.KilometersPerHour,
    languageCode = LanguageCode.En,
)

private data class SettingsScreenPreviewParameter(
    val state: ViewState<SettingsState>,
)

private class SettingsScreenPreviewParameterProvider :
    PreviewParameterProvider<SettingsScreenPreviewParameter> {

    override val values: Sequence<SettingsScreenPreviewParameter> = sequenceOf(
        SettingsScreenPreviewParameter(
            state = ViewState.Data(settingsStateDefaultStub),
        ),
        SettingsScreenPreviewParameter(
            state = ViewState.Data(settingsStateAlternateStub),
        ),
        SettingsScreenPreviewParameter(
            state = ViewState.Loading.Skeleton(),
        ),
        SettingsScreenPreviewParameter(
            state = ViewState.Error(UnexpectedError()),
        ),
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF2B83E2,
)
@Composable
private fun SettingsScreenPreview(
    @PreviewParameter(SettingsScreenPreviewParameterProvider::class)
    params: SettingsScreenPreviewParameter,
) {
    SettingsScreen(
        state = params.state,
        controller = settingsControllerStub,
    )
}
