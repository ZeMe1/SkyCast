package com.example.skycast.core.util

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.abs

@Composable
fun rememberCustomFlingBehavior(): FlingBehavior {
    val density = LocalDensity.current

    return remember(density) {
        object : FlingBehavior {
            private val decayAnimationSpec = exponentialDecay<Float>(
                frictionMultiplier = 1.75f,
                absVelocityThreshold = 0.5f
            )

            override suspend fun ScrollScope.performFling(
                initialVelocity: Float
            ): Float {
                var remainingVelocity = initialVelocity
                var lastValue = 0f

                AnimationState(
                    initialValue = 0f,
                    initialVelocity = initialVelocity
                ).animateDecay(decayAnimationSpec) {
                    val delta = value - lastValue
                    val consumed = scrollBy(delta)
                    lastValue = value
                    remainingVelocity = velocity

                    if (abs(delta - consumed) > 0.5f) {
                        cancelAnimation()
                    }
                }

                return remainingVelocity
            }
        }
    }
}