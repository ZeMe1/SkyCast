package com.example.skycast.core.presentation.main_screen.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.R
import com.example.skycast.core.domain.model.DailyForecastData

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyForecastCard(
    forecasts: List<DailyForecastData>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.forecast),
                fontSize = 16.sp,
                color = Color(0xFF_A098AE),
                fontFamily = FontFamily(Font(R.font.poppins_regular))
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.seven_days_forecast),
                fontSize = 10.sp,
                color = Color(0xFF_363B64),
                fontFamily = FontFamily(Font(R.font.poppins_regular))
            )

            Spacer(Modifier.height(16.dp))

            forecasts.forEach { forecast ->
                ForecastRow(forecast = forecast)
            }
        }
    }
}