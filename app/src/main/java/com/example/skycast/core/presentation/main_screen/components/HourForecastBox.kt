package com.example.skycast.core.presentation.main_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.R
import com.example.skycast.core.domain.model.HourlyForecast

@Composable
fun HourForecastBox(
    hourlyForecastList: List<HourlyForecast>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White.copy(0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.hourly_forecast),
                fontSize = 14.sp,
                color = Color(0xFF_A098AE),
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                lineHeight = 24.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(hourlyForecastList) { forecast ->
                        ForecastColumn(
                            hourlyForecast = forecast
                        )
                    }
                }
            }
        }
    }
}