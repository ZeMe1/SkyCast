package kz.zeme.skycast.home.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import kotlin.math.roundToInt
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kz.zeme.skycast.core.error.BaseError
import kz.zeme.skycast.core.error.ConnectionError
import kz.zeme.skycast.core.observable.ViewState
import kz.zeme.skycast.core.res.R
import kz.zeme.skycast.core.res.theme.SkyCastTheme
import kz.zeme.skycast.core.res.theme.align
import kz.zeme.skycast.core.res.theme.bold
import kz.zeme.skycast.core.res.theme.medium
import kz.zeme.skycast.core.res.theme.primary
import kz.zeme.skycast.core.res.theme.secondary
import kz.zeme.skycast.core.res.theme.skyGradientBrush
import kz.zeme.skycast.core.res.theme.tempBarBrush
import kz.zeme.skycast.core.settings.domain.TemperatureMetric.TemperatureUnit
import kz.zeme.skycast.core.settings.domain.WindSpeedMetric.WindSpeedUnit
import kz.zeme.skycast.core.ui.components.DefaultErrorScreen
import kz.zeme.skycast.core.ui.extensions.compose.clickableHaptic
import kz.zeme.skycast.core.ui.extensions.shimmer
import kz.zeme.skycast.core.weather.domain.CurrentWeather
import kz.zeme.skycast.core.weather.domain.DailyForecast
import kz.zeme.skycast.core.weather.domain.HourlyTimelineItem
import kz.zeme.skycast.core.weather.domain.HourlyTimelineItem.Hour
import kz.zeme.skycast.core.weather.domain.HourlyTimelineItem.SunEvent
import kz.zeme.skycast.core.weather.domain.SunEventType
import kz.zeme.skycast.core.weather.domain.UvLevel
import kz.zeme.skycast.core.weather.domain.WeatherCondition
import kz.zeme.skycast.core.weather.domain.WeatherForecast
import kz.zeme.skycast.core.weather.domain.WindDirection

@Composable
internal fun HomeScreen(
    state: State<ViewState<WeatherForecast>?>,
    controller: HomeController,
) {
    HomeScreen(
        state = state.value,
        controller = controller,
    )
}

@Composable
private fun HomeScreen(
    state: ViewState<WeatherForecast>?,
    controller: HomeController,
    scrollState: ScrollState = rememberScrollState(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SkyCastTheme.colors.skyGradientBrush())
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        when (state) {
            null,
            is ViewState.Loading.Skeleton -> HomeShimmerView(
                modifier = Modifier.fillMaxSize(),
            )

            is ViewState.Error -> {
                HomeTopBar(onSettingsClick = controller::onSettingsClick)
                DefaultErrorView(
                    modifier = Modifier.fillMaxSize(),
                    error = state.error,
                    onRefresh = controller::onRefresh,
                )
            }

            is ViewState.Loading.ProgressBar,
            is ViewState.Data -> state.data?.let { forecast ->
                HomeTopBar(onSettingsClick = controller::onSettingsClick)
                HomeContent(
                    forecast = forecast,
                    scrollState = scrollState,
                )
            } ?: HomeShimmerView(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun HomeTopBar(
    onSettingsClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.home_title),
            style = SkyCastTheme.typography.titleMedium.primary(),
        )

        Icon(
            modifier = Modifier
                .size(24.dp)
                .clickableHaptic(onClick = onSettingsClick),
            painter = painterResource(R.drawable.ic_settings),
            contentDescription = null,
            tint = SkyCastTheme.colors.textPrimary,
        )
    }
}

@Composable
private fun HomeContent(
    forecast: WeatherForecast,
    scrollState: ScrollState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 24.dp),
    ) {
        HomeHeroSection(current = forecast.current)
        HourlyForecastSection(timeline = forecast.hourlyTimeline)
        DailyForecastSection(daily = forecast.daily)
        WeatherDetailsSection(
            current = forecast.current,
            windSpeedUnit = forecast.windSpeedUnit,
        )
    }
}

@Composable
private fun HomeHeroSection(
    current: CurrentWeather,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 24.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = current.temperature.formatSignedTemperature(),
                style = SkyCastTheme.typography.titleLarge.primary().copy(
                    fontSize = 64.sp,
                    lineHeight = 68.sp,
                    letterSpacing = (-2).sp,
                    fontFeatureSettings = "tnum",
                ),
            )

            Column(
                modifier = Modifier.padding(bottom = 6.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = stringResource(
                        R.string.home_max_temp,
                        current.maxTemperature.formatSignedTemperature(),
                    ),
                    style = SkyCastTheme.typography.labelLarge.primary().copy(
                        fontFeatureSettings = "tnum",
                    ),
                )
                Text(
                    text = stringResource(
                        R.string.home_min_temp,
                        current.minTemperature.formatSignedTemperature(),
                    ),
                    style = SkyCastTheme.typography.labelLarge.secondary().copy(
                        fontWeight = FontWeight.Normal,
                        fontFeatureSettings = "tnum",
                    ),
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(current.condition.toIconRes(current.isDay)),
                    contentDescription = null,
                )
                Text(
                    text = stringResource(current.condition.toTitleRes()),
                    style = SkyCastTheme.typography.bodyLarge.primary(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(
                    R.string.home_feels_like,
                    current.apparentTemperature.formatSignedTemperature(),
                ),
                style = SkyCastTheme.typography.bodyMedium.secondary().copy(
                    fontFeatureSettings = "tnum",
                ),
            )
        }
    }
}

@Composable
private fun HourlyForecastSection(
    timeline: ImmutableList<HourlyTimelineItem>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
            text = stringResource(R.string.home_hourly_title),
            style = SkyCastTheme.typography.titleSmall.primary(),
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items = timeline,
                key = { "${it::class.simpleName}_${it.timeIso}" },
            ) { item ->
                when (item) {
                    is Hour -> HourlyForecastCard(item = item)
                    is SunEvent -> SunEventForecastCard(item = item)
                }
            }
        }
    }
}

@Composable
private fun HourlyForecastCard(
    item: Hour,
) {
    HourlyCardContainer {
        Text(
            text = if (item.isCurrent) stringResource(R.string.home_now) else item.hourLabel,
            style = if (item.isCurrent) {
                SkyCastTheme.typography.bodySmall.medium().primary()
            } else {
                SkyCastTheme.typography.bodySmall.secondary().copy(fontFeatureSettings = "tnum")
            },
            maxLines = 1,
        )

        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(item.condition.toIconRes(item.isDay)),
            contentDescription = null,
        )

        Text(
            text = item.temperature.formatSignedTemperature(),
            style = SkyCastTheme.typography.bodyLarge.bold().primary().copy(
                fontFeatureSettings = "tnum",
            ),
            maxLines = 1,
        )

        Text(
            text = item.precipitationProbability.formatPercent(),
            style = SkyCastTheme.typography.labelSmall.secondary().copy(
                fontFeatureSettings = "tnum",
            ),
            maxLines = 1,
        )
    }
}

@Composable
private fun SunEventForecastCard(
    item: SunEvent,
) {
    HourlyCardContainer {
        Text(
            text = item.timeLabel,
            style = SkyCastTheme.typography.bodySmall.secondary().copy(
                fontFeatureSettings = "tnum",
            ),
            maxLines = 1,
        )

        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(item.type.toIconRes()),
            contentDescription = null,
        )

        Text(
            text = stringResource(item.type.toTitleRes()),
            style = SkyCastTheme.typography.bodySmall.medium().primary().copy(
                lineHeight = 20.sp,
            ),
            maxLines = 1,
        )

        Text(
            text = "",
            style = SkyCastTheme.typography.labelSmall,
        )
    }
}

@Composable
private fun HourlyCardContainer(
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .width(68.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(SkyCastTheme.colors.basicDark)
            .padding(horizontal = 6.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        content = content,
    )
}

@Composable
private fun DailyForecastSection(
    daily: ImmutableList<DailyForecast>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
    ) {
        Text(
            modifier = Modifier.padding(bottom = 12.dp),
            text = stringResource(R.string.home_daily_title),
            style = SkyCastTheme.typography.titleSmall.primary(),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SkyCastTheme.colors.basicDark),
        ) {
            daily.forEach { item ->
                DailyForecastRow(item = item)
                if (daily.lastOrNull() != item) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 16.dp),
                        color = SkyCastTheme.colors.divider,
                        thickness = 1.dp,
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyForecastRow(
    item: DailyForecast,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.width(64.dp),
            text = if (item.isToday) stringResource(R.string.home_today) else stringResource(item.dayOfWeek.toShortRes()),
            style = SkyCastTheme.typography.bodyMedium.medium().primary(),
            maxLines = 1,
        )

        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(item.condition.toIconRes(isDay = true)),
            contentDescription = null,
        )

        Text(
            modifier = Modifier.width(40.dp),
            text = item.minTemperature.formatSignedTemperature(),
            style = SkyCastTheme.typography.bodyMedium.secondary().align(TextAlign.End).copy(
                fontFeatureSettings = "tnum",
            ),
            maxLines = 1,
        )

        TemperatureRangeBar(
            modifier = Modifier.weight(1f),
            startFraction = item.rangeStartFraction,
            widthFraction = item.rangeWidthFraction,
        )

        Text(
            modifier = Modifier.width(40.dp),
            text = item.maxTemperature.formatSignedTemperature(),
            style = SkyCastTheme.typography.bodyMedium.medium().primary().align(TextAlign.End).copy(
                fontFeatureSettings = "tnum",
            ),
            maxLines = 1,
        )
    }
}

@Composable
private fun TemperatureRangeBar(
    modifier: Modifier = Modifier,
    startFraction: Float,
    widthFraction: Float,
) {
    Box(
        modifier = modifier
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(SkyCastTheme.colors.buttonDisabled),
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .layout { measurable, constraints ->
                    val totalWidth = constraints.maxWidth
                    val minFillPx = 4.dp.roundToPx().coerceAtMost(totalWidth)
                    val startPx = (totalWidth * startFraction)
                        .roundToInt()
                        .coerceIn(0, (totalWidth - minFillPx).coerceAtLeast(0))
                    val fillWidthPx = (totalWidth * widthFraction)
                        .roundToInt()
                        .coerceIn(minFillPx, (totalWidth - startPx).coerceAtLeast(0))
                    val placeable = measurable.measure(
                        Constraints.fixed(width = fillWidthPx, height = constraints.maxHeight),
                    )
                    layout(totalWidth, constraints.maxHeight) {
                        placeable.placeRelative(x = startPx, y = 0)
                    }
                }
                .clip(RoundedCornerShape(2.dp))
                .background(SkyCastTheme.colors.tempBarBrush()),
        )
    }
}

@Composable
private fun WeatherDetailsSection(
    current: CurrentWeather,
    windSpeedUnit: WindSpeedUnit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            modifier = Modifier.padding(bottom = 12.dp),
            text = stringResource(R.string.home_details_title),
            style = SkyCastTheme.typography.titleSmall.primary(),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            WeatherDetailCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                iconRes = R.drawable.ic_wind,
                labelRes = R.string.home_wind_label,
                valueText = stringResource(
                    windSpeedUnit.toValueFormatRes(),
                    current.windSpeed.formatWindSpeed(),
                ),
                footer = {
                    Text(
                        text = stringResource(
                            R.string.home_wind_gusts,
                            stringResource(current.windDirection.toShortRes()),
                            current.windGusts.toString(),
                        ),
                        style = SkyCastTheme.typography.bodySmall.secondary().copy(
                            fontFeatureSettings = "tnum",
                        ),
                    )
                },
            )

            WeatherDetailCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                iconRes = R.drawable.ic_humidity,
                labelRes = R.string.home_humidity_label,
                valueText = current.humidity.formatPercent(),
                footer = {
                    Text(
                        text = stringResource(
                            R.string.home_dew_point,
                            current.dewPoint.formatSignedTemperature(),
                        ),
                        style = SkyCastTheme.typography.bodySmall.secondary().copy(
                            fontFeatureSettings = "tnum",
                        ),
                    )
                },
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            WeatherDetailCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                iconRes = R.drawable.ic_pressure,
                labelRes = R.string.home_pressure_label,
                valueText = stringResource(R.string.home_pressure_value, current.pressureHpa),
            )

            WeatherDetailCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                iconRes = R.drawable.ic_uv,
                labelRes = R.string.home_uv_label,
                valueText = current.uvIndex.toString(),
                footer = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val statusColor =
                            if (current.uvLevel.isLowOrModerate()) SkyCastTheme.colors.basicSuccess
                            else SkyCastTheme.colors.basicError

                        Spacer(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(statusColor),
                        )
                        Text(
                            text = stringResource(current.uvLevel.toTitleRes()),
                            style = SkyCastTheme.typography.bodySmall.secondary(),
                        )
                    }
                },
            )
        }
    }
}

@Composable
private fun WeatherDetailCard(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    @StringRes labelRes: Int,
    valueText: String,
    footer: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(SkyCastTheme.colors.basicDark)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = SkyCastTheme.colors.textSecondary,
            )
            Text(
                text = stringResource(labelRes),
                style = SkyCastTheme.typography.bodySmall.secondary(),
            )
        }

        if (footer == null) Spacer(modifier = Modifier.weight(1f))
        else Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = valueText,
            style = SkyCastTheme.typography.titleSmall.bold().primary().copy(
                fontFeatureSettings = "tnum",
            ),
        )

        footer?.invoke()
    }
}

@Composable
private fun HomeShimmerView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(
                modifier = Modifier
                    .width(88.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmer(),
            )
            Spacer(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .shimmer(),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 24.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Spacer(
                    modifier = Modifier
                        .width(116.dp)
                        .height(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .shimmer(),
                )
                Column(
                    modifier = Modifier.padding(bottom = 6.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Spacer(
                        modifier = Modifier
                            .width(72.dp)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmer(),
                    )
                    Spacer(
                        modifier = Modifier
                            .width(64.dp)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmer(),
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(
                    modifier = Modifier
                        .width(176.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmer(),
                )
                Spacer(
                    modifier = Modifier
                        .width(96.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmer(),
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
        ) {
            Spacer(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    .width(92.dp)
                    .height(22.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmer(),
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false,
            ) {
                items(5) {
                    Spacer(
                        modifier = Modifier
                            .width(68.dp)
                            .height(112.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .shimmer(),
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
        ) {
            Spacer(
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .width(152.dp)
                    .height(22.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmer(),
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(342.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .shimmer(),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
        ) {
            Spacer(
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .width(72.dp)
                    .height(22.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmer(),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .height(102.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .shimmer(),
                )
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .height(102.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .shimmer(),
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .height(102.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .shimmer(),
                )
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .height(102.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .shimmer(),
                )
            }
        }
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
        title = stringResource(error.toErrorTitleRes()),
        errorMessage = stringResource(error.toErrorMessageRes()),
        onRefresh = onRefresh,
        backgroundColor = Color.Transparent,
        content = {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(R.drawable.ic_wifi_off),
                contentDescription = null,
                tint = SkyCastTheme.colors.textSecondary,
            )
        },
    )
}

private val homeControllerStub = object : HomeController {
    override fun onStart() = Unit
    override fun onRefresh() = Unit
    override fun onSettingsClick() = Unit
}

private val weatherForecastCelsiusStub = WeatherForecast(
    current = CurrentWeather(
        temperature = 14,
        apparentTemperature = 12,
        minTemperature = 7,
        maxTemperature = 18,
        condition = WeatherCondition.PartlyCloudy,
        isDay = true,
        windSpeed = 4.2,
        windDirection = WindDirection.NorthWest,
        windGusts = 7,
        humidity = 54,
        dewPoint = 5,
        pressureHpa = 1015,
        uvIndex = 2,
        uvLevel = UvLevel.Low,
    ),
    hourlyTimeline = persistentListOf(
        Hour("2026-11-21T16:00", "16:00", 14, 10, WeatherCondition.PartlyCloudy, isDay = true, isCurrent = true),
        Hour("2026-11-21T17:00", "17:00", 13, 10, WeatherCondition.PartlyCloudy, isDay = true, isCurrent = false),
        SunEvent("2026-11-21T17:18", "17:18", SunEventType.Sunset),
        Hour("2026-11-21T18:00", "18:00", 12, 15, WeatherCondition.Overcast, isDay = false, isCurrent = false),
        Hour("2026-11-21T19:00", "19:00", 11, 15, WeatherCondition.Overcast, isDay = false, isCurrent = false),
        Hour("2026-11-21T20:00", "20:00", 10, 5, WeatherCondition.Clear, isDay = false, isCurrent = false),
        SunEvent("2026-11-22T07:42", "07:42", SunEventType.Sunrise),
    ),
    daily = persistentListOf(
        DailyForecast("2026-11-21", DayOfWeek.SATURDAY, isToday = true, WeatherCondition.PartlyCloudy, 7, 18, 0.2f, 0.73f),
        DailyForecast("2026-11-22", DayOfWeek.SUNDAY, isToday = false, WeatherCondition.Rain, 5, 11, 0.07f, 0.40f),
        DailyForecast("2026-11-23", DayOfWeek.MONDAY, isToday = false, WeatherCondition.Overcast, 4, 12, 0.0f, 0.53f),
        DailyForecast("2026-11-24", DayOfWeek.TUESDAY, isToday = false, WeatherCondition.Clear, 6, 16, 0.13f, 0.67f),
        DailyForecast("2026-11-25", DayOfWeek.WEDNESDAY, isToday = false, WeatherCondition.Clear, 8, 19, 0.27f, 0.73f),
        DailyForecast("2026-11-26", DayOfWeek.THURSDAY, isToday = false, WeatherCondition.PartlyCloudy, 7, 17, 0.20f, 0.67f),
        DailyForecast("2026-11-27", DayOfWeek.FRIDAY, isToday = false, WeatherCondition.Overcast, 5, 14, 0.07f, 0.60f),
    ),
    temperatureUnit = TemperatureUnit.Celsius,
    windSpeedUnit = WindSpeedUnit.MetersPerSecond,
)

private val weatherForecastFahrenheitStub = weatherForecastCelsiusStub.copy(
    current = weatherForecastCelsiusStub.current.copy(
        temperature = 57,
        apparentTemperature = 54,
        minTemperature = 45,
        maxTemperature = 64,
        windSpeed = 15.1,
        windGusts = 25,
        dewPoint = 41,
        uvIndex = 8,
        uvLevel = UvLevel.VeryHigh,
    ),
    temperatureUnit = TemperatureUnit.Fahrenheit,
    windSpeedUnit = WindSpeedUnit.KilometersPerHour,
)

private data class HomeScreenPreviewParameter(
    val state: ViewState<WeatherForecast>,
)

private class HomeScreenPreviewParameterProvider :
    PreviewParameterProvider<HomeScreenPreviewParameter> {

    override val values: Sequence<HomeScreenPreviewParameter> = sequenceOf(
        HomeScreenPreviewParameter(
            state = ViewState.Data(weatherForecastCelsiusStub),
        ),
        HomeScreenPreviewParameter(
            state = ViewState.Data(weatherForecastFahrenheitStub),
        ),
        HomeScreenPreviewParameter(
            state = ViewState.Loading.Skeleton(),
        ),
        HomeScreenPreviewParameter(
            state = ViewState.Error(ConnectionError()),
        ),
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF2B83E2,
)
@Composable
private fun HomeScreenPreview(
    @PreviewParameter(HomeScreenPreviewParameterProvider::class)
    params: HomeScreenPreviewParameter,
) {
    HomeScreen(
        state = params.state,
        controller = homeControllerStub,
    )
}
