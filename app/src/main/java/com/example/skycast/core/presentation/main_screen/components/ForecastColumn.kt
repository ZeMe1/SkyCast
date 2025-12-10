package com.example.skycast.core.presentation.main_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.R
import com.example.skycast.core.domain.model.HourlyForecast
import com.example.skycast.core.domain.model.WeatherType

@Composable
fun ForecastColumn(
    hourlyForecast: HourlyForecast
) {
    Column(
        modifier = Modifier
            .padding(vertical = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = hourlyForecast.time,
            fontSize = 12.sp,
            color = Color(0xFF_A098AE),
            fontFamily = FontFamily(Font(R.font.poppins_regular))
        )
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .size(40.dp)
        ) {
            Icon(
                painter = painterResource(
                    WeatherType.hourlyIcon(
                        hourlyForecast.weatherType,
                        hourlyForecast.isDay
                    )
                ),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
        Text(
            text = hourlyForecast.displayTemp,
            fontSize = 14.sp,
            color = Color(0xFF_363B64),
            fontFamily = FontFamily(Font(R.font.poppins_regular))
        )
    }
}