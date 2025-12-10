package com.example.skycast.core.presentation.result_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.R
import com.example.skycast.core.domain.model.CurrentWeather
import com.example.skycast.core.domain.model.WeatherType
import com.example.skycast.core.util.safeClickable

@Composable
fun ResultScreenTopBar(
    location: String,
    currentWeather: CurrentWeather?,
    onGoBack: ()-> Unit
) {
    val tempLabel = currentWeather?.tempC?.toInt()?.let { "$it°" } ?: "3°"
    val feelsLikeLabel = currentWeather?.feelslikeC?.toInt()?.let { "Feels like $it°" } ?: "Feels like -2°"
    val condition = currentWeather?.weatherType ?: WeatherType.PartlyCloudy
    val conditionLabel = condition.label
    val conditionIcon = condition.currentIcon

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.45f)
            .clip(
                shape = RoundedCornerShape(
                    bottomStart = 32.dp,
                    bottomEnd = 32.dp
                )
            )
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.05f to Color(0xFF_3D70D1),
                        1.0f to Color(0xFF_7CA9FF).copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp, bottom = 64.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .safeClickable {
                            onGoBack()
                        }
                )
                Spacer(Modifier.width(12.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_location),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Spacer(Modifier.width(24.dp))
                Text(
                    text = location,
                    fontSize = 20.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.poppins_regular))
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = tempLabel,
                        fontSize = 100.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.poppins_light))
                    )
                    Text(
                        text = feelsLikeLabel,
                        fontSize = 18.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.poppins_regular))
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.End
                ) {
                    Icon(
                        painter = painterResource(conditionIcon),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                    Text(
                        text = conditionLabel,
                        fontSize = 20.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        lineHeight = 28.sp,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}