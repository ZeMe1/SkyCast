package com.example.skycast.core.presentation.splash_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skycast.R
import com.example.skycast.core.presentation.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = koinViewModel(),
    navController: NavController
) {
    val shouldNavigate by viewModel.shouldNavigate.collectAsState()

    if (shouldNavigate) navController.navigate(Screen.MainScreen) {
        popUpTo(Screen.SplashScreen) {
            inclusive = true
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF_3C6FD1).copy(0.9f),
                        Color(0xFF_3C6FD1).copy(0.8f),
                        Color(0xFF_7CA9FF).copy(0.8f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_splash_logo),
                contentDescription = "Logo",
                tint = Color.Unspecified,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 32.sp,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.poppins_bold))
            )
        }
    }
}