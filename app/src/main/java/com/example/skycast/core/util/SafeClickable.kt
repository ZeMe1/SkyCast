package com.example.skycast.core.util

import android.annotation.SuppressLint
import android.os.SystemClock
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

private const val DEFAULT_INTERVAL_TIME = 800

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.safeClickable(
    enabled: Boolean = true,
    clickInterval: Int? = null,
    indication: Indication? = null,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    onClick: () -> Unit
) = composed {
    val defaultInterval = remember { clickInterval ?: DEFAULT_INTERVAL_TIME }
    var lastTime by remember { mutableLongStateOf(0L) }
    clickable(
        enabled = enabled,
        interactionSource = interactionSource,
        indication = indication,
        onClick = {
            if (SystemClock.elapsedRealtime() - lastTime >= defaultInterval) {
                lastTime = SystemClock.elapsedRealtime()
                onClick()
            }
        }
    )
}