package kz.zeme.skycast.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kz.zeme.skycast.core.res.R
import kz.zeme.skycast.core.res.theme.SkyCastTheme
import kz.zeme.skycast.core.res.theme.bold
import kz.zeme.skycast.core.res.theme.medium

@Composable
fun DefaultErrorScreen(
    modifier: Modifier = Modifier,
    title: String? = null,
    errorMessage: String = stringResource(R.string.shared_something_went_wrong),
    buttonText: String = stringResource(R.string.shared_retry),
    onRefresh: (() -> Unit)? = null,
    backgroundColor: Color = SkyCastTheme.colors.mainBg,
    content: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (title != null) {
            Text(
                text = title,
                style = SkyCastTheme.typography.titleMedium.bold(),
                color = SkyCastTheme.colors.textPrimary,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            text = errorMessage,
            style = SkyCastTheme.typography.bodyLarge,
            color = SkyCastTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )

        if (content != null) {
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }

        if (onRefresh != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRefresh,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SkyCastTheme.colors.buttonPrimary,
                    contentColor = SkyCastTheme.colors.textPrimary,
                ),
            ) {
                Text(
                    text = buttonText,
                    style = SkyCastTheme.typography.labelLarge.medium(),
                )
            }
        }
    }
}
