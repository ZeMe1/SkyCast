package kz.zeme.skycast.settings.presentation.unitpicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import kz.zeme.skycast.core.error.BaseError
import kz.zeme.skycast.core.error.UnexpectedError
import kz.zeme.skycast.core.observable.ViewState
import kz.zeme.skycast.core.res.R
import kz.zeme.skycast.core.res.theme.SkyCastTheme
import kz.zeme.skycast.core.res.theme.primary
import kz.zeme.skycast.core.ui.components.DefaultErrorScreen
import kz.zeme.skycast.core.ui.compose.views.ModalBottomSheet
import kz.zeme.skycast.core.ui.extensions.compose.clickableHaptic
import kz.zeme.skycast.core.ui.extensions.shimmer
import kz.zeme.skycast.settings.presentation.unitpicker.UnitPickerItem.UnitCode

@Composable
internal fun UnitPickerScreen(
    unitPickerType: UnitPickerType,
    state: State<ViewState<List<UnitPickerItem>>?>,
    controller: UnitPickerController,
) {
    UnitPickerScreen(
        unitPickerType = unitPickerType,
        viewState = state.value,
        controller = controller,
    )
}

@Composable
private fun UnitPickerScreen(
    unitPickerType: UnitPickerType,
    viewState: ViewState<List<UnitPickerItem>>?,
    controller: UnitPickerController,
) {
    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = stringResource(unitPickerType.toTitleRes()),
            style = SkyCastTheme.typography.titleMedium.primary(),
        )

        when (viewState) {
            null,
            is ViewState.Loading.Skeleton -> UnitPickerShimmerView()

            is ViewState.Error -> DefaultErrorView(
                error = viewState.error,
                onRefresh = controller::onStart,
            )

            is ViewState.Loading.ProgressBar,
            is ViewState.Data -> viewState.data?.let {
                UnitListContent(
                    units = it,
                    controller = controller,
                )
            } ?: UnitPickerShimmerView()
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
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        errorMessage = error?.message ?: stringResource(R.string.shared_something_went_wrong),
        onRefresh = onRefresh,
        backgroundColor = SkyCastTheme.colors.sheetBg,
    )
}

@Composable
private fun UnitPickerShimmerView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        repeat(2) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(SkyCastTheme.colors.mainShimmer)
                        .shimmer(),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(0.55f)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(SkyCastTheme.colors.mainShimmer)
                        .shimmer(),
                )
            }
        }
    }
}

@Composable
private fun UnitListContent(
    units: List<UnitPickerItem>,
    controller: UnitPickerController,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
    ) {
        items(items = units) { item ->
            UnitItemRow(
                item = item,
                onClick = { controller.onUnitClick(item) },
            )
            if (units.lastOrNull() != item) {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 16.dp),
                    color = SkyCastTheme.colors.divider,
                    thickness = 1.dp,
                )
            }
        }
    }
}

@Composable
private fun UnitItemRow(
    item: UnitPickerItem,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableHaptic(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .padding(end = 12.dp)
                .size(24.dp)
                .alpha(if (item.isSelected) 1f else 0f),
            painter = painterResource(R.drawable.ic_check_mark),
            contentDescription = null,
            tint = SkyCastTheme.colors.textPrimary,
        )

        Text(
            text = stringResource(item.code.toTitleRes()),
            style = SkyCastTheme.typography.bodyLarge.primary(),
            textAlign = TextAlign.Start,
        )
    }
}

private val unitPickerControllerStub = object : UnitPickerController {
    override fun onUnitClick(item: UnitPickerItem) = Unit
    override fun onClose() = Unit
    override fun onStart() = Unit
}

private val temperatureUnitListStub = listOf(
    UnitPickerItem(isSelected = true, code = UnitCode.Celsius),
    UnitPickerItem(isSelected = false, code = UnitCode.Fahrenheit),
)

private data class UnitPickerScreenPreviewParameter(
    val type: UnitPickerType,
    val state: ViewState<List<UnitPickerItem>>,
)

private class UnitPickerScreenPreviewParameterProvider :
    PreviewParameterProvider<UnitPickerScreenPreviewParameter> {

    override val values: Sequence<UnitPickerScreenPreviewParameter> = sequenceOf(
        UnitPickerScreenPreviewParameter(
            type = UnitPickerType.Temperature,
            state = ViewState.Data(temperatureUnitListStub),
        ),
        UnitPickerScreenPreviewParameter(
            type = UnitPickerType.Temperature,
            state = ViewState.Data(emptyList()),
        ),
        UnitPickerScreenPreviewParameter(
            type = UnitPickerType.WindSpeed,
            state = ViewState.Loading.Skeleton(),
        ),
        UnitPickerScreenPreviewParameter(
            type = UnitPickerType.WindSpeed,
            state = ViewState.Error(UnexpectedError()),
        ),
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF2B83E2,
)
@Composable
private fun UnitPickerScreenPreview(
    @PreviewParameter(UnitPickerScreenPreviewParameterProvider::class)
    params: UnitPickerScreenPreviewParameter,
) {
    UnitPickerScreen(
        unitPickerType = params.type,
        viewState = params.state,
        controller = unitPickerControllerStub,
    )
}
