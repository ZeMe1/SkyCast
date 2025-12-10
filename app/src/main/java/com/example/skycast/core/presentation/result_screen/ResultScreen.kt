package com.example.skycast.core.presentation.result_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.skycast.core.presentation.main_screen.components.DailyForecastCard
import com.example.skycast.core.presentation.main_screen.components.HourForecastBox
import com.example.skycast.core.presentation.result_screen.components.ResultScreenTopBar
import com.example.skycast.core.presentation.search_screen.SearchViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ResultScreen(
    navController: NavController,
    viewModel: SearchViewModel
) {
    val currentWeather by viewModel.currentWeather.collectAsState()
    val dailyForecast by viewModel.dailyForecasts.collectAsState()
    val hourlyForecast by viewModel.hourlyForecasts.collectAsState()
    val error by viewModel.error.collectAsState()
    val locationLabel by viewModel.location.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    else {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .background(color = Color(0xFF_3D70D1))
                ) {

                }

            },
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(25.dp)
                        .background(color = Color(0xFF_F2F2F2))
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 24.dp)
                    .background(color = Color(0xFF_F2F2F2))
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        ResultScreenTopBar(
                            location = locationLabel,
                            currentWeather = currentWeather,
                            onGoBack = {
                                navController.popBackStack()
                            }
                        )
                        HourForecastBox(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = 265.dp),
                            hourlyForecastList = hourlyForecast
                        )
                    }
                }
                item {
                    DailyForecastCard(
                        forecasts = dailyForecast,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp, bottom = 24.dp)
                    )
                }
                if (error != null) {
                    item {
                        Text(
                            text = error ?: "",
                            color = Color.Red,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 16.dp)
                        )
                    }
                }
            }
        }
    }
}