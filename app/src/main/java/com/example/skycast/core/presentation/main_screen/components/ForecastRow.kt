package com.example.skycast.core.presentation.main_screen.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import com.example.skycast.core.domain.model.DailyForecastData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastRow(
    forecast: DailyForecastData
) {
    val dayName = runCatching {
        LocalDate.parse(forecast.date, dateFormatter).format(dayNameFormatter)
    }.getOrDefault(forecast.date)

    val skyLabel = forecast.weatherType.label
    val maxTemp = "${forecast.maxTempC.toInt()}°"
    val minTemp = "${forecast.minTempC.toInt()}°"
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = dayName,
                    fontSize = 14.sp,
                    color = Color(0xFF_363B64),
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    lineHeight = 24.sp
                )
                Text(
                    text = skyLabel,
                    fontSize = 14.sp,
                    color = Color(0xFF_A098AE),
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    lineHeight = 24.sp
                )
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = maxTemp,
                    fontSize = 14.sp,
                    color = Color(0xFF_363B64),
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    lineHeight = 24.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = minTemp,
                    fontSize = 14.sp,
                    color = Color(0xFF_363B64),
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    lineHeight = 24.sp
                )
            }
            Spacer(Modifier.width(10.dp))
            VerticalDivider(
                thickness = 1.dp,
                color = Color(0xFF_363B64),
                modifier = Modifier
                    .height(36.dp)
            )
            Spacer(Modifier.width(24.dp))

            Icon(
                painter = painterResource(id = forecast.weatherType.dailyIcon),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
@RequiresApi(Build.VERSION_CODES.O)
private val dayNameFormatter = DateTimeFormatter.ofPattern("EEEE")