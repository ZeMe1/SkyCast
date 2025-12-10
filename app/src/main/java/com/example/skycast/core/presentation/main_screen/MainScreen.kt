package com.example.skycast.core.presentation.main_screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.skycast.core.presentation.main_screen.components.DailyForecastCard
import com.example.skycast.core.presentation.main_screen.components.HourForecastBox
import com.example.skycast.core.presentation.main_screen.components.MainScreenTopBar
import com.example.skycast.core.presentation.navigation.Screen
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val currentWeather by viewModel.currentWeather.collectAsState()
    val dailyForecast by viewModel.dailyForecasts.collectAsState()
    val hourlyForecast by viewModel.hourlyForecasts.collectAsState()
    val error by viewModel.error.collectAsState()
    val locationLabel by viewModel.location.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var locationEnabled by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    var shouldCheckLocation by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = result[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                result[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            viewModel.fetchWeatherForCurrentLocation()
        } else {
            viewModel.setError("Location permission not granted")
        }
    }

    val locationSettingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        isRefreshing = false
        if (result.resultCode == Activity.RESULT_OK) {

            locationEnabled = true
            viewModel.fetchWeatherForCurrentLocation()
        } else {

            locationEnabled = false
        }
    }

    LaunchedEffect(shouldCheckLocation) {
        if (shouldCheckLocation) {
            isRefreshing = true
            checkAndRequestLocationSettings(
                context = context,
                onSettingsOk = {
                    isRefreshing = false
                    locationEnabled = true
                    viewModel.fetchWeatherForCurrentLocation()
                },
                onNeedResolution = { intentSenderRequest ->
                    locationSettingsLauncher.launch(intentSenderRequest)
                }
            )
            shouldCheckLocation = false
        }
    }

    LaunchedEffect(Unit) {
        val hasFine = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        if (hasFine || hasCoarse) {
            shouldCheckLocation = true
        } else {
            permissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
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
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {

                    val hasFine = ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                    val hasCoarse = ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED

                    if (hasFine || hasCoarse) {

                        shouldCheckLocation = true
                    } else {
                        isRefreshing = false
                        viewModel.setError("Location permission not granted")
                    }
                },
                modifier = Modifier.fillMaxSize()
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
                            MainScreenTopBar(
                                location = locationLabel,
                                currentWeather = currentWeather,
                                onSearchClick = {
                                    navController.navigate(Screen.SearchScreen)
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
                            androidx.compose.material3.Text(
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
}

fun checkAndRequestLocationSettings(
    context: Context,
    onSettingsOk: () -> Unit,
    onNeedResolution: (IntentSenderRequest) -> Unit
) {
    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        10000L
    ).build()

    val builder = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)
        .setAlwaysShow(true)

    val client: SettingsClient = LocationServices.getSettingsClient(context)
    val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

    task.addOnSuccessListener {
        onSettingsOk()
    }

    task.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                val intentSenderRequest = IntentSenderRequest.Builder(
                    exception.resolution
                ).build()
                onNeedResolution(intentSenderRequest)
            } catch (sendEx: IntentSender.SendIntentException) {
            }
        }
    }
}