package com.example.skycast.core.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    object SplashScreen : Screen
    @Serializable
    object MainScreen : Screen
    @Serializable
    object SearchScreen : Screen
    @Serializable
    object ResultScreen : Screen
}

