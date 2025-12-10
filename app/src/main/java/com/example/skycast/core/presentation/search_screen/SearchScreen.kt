package com.example.skycast.core.presentation.search_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skycast.R
import com.example.skycast.core.presentation.navigation.Screen
import com.example.skycast.core.util.safeClickable

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    navController: NavController
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = Color(0xFF_F2F2F2)
    ) {
        paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(top = 32.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .safeClickable {
                            navController.popBackStack()
                        }
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.explore),
                    fontSize = 16.sp,
                    color = Color(0xFF_363B64),
                    fontFamily = FontFamily(Font(R.font.poppins_semibold))
                )
            }
            Spacer(Modifier.height(12.dp))

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = searchQuery,
                onValueChange = viewModel::onValueChange,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color(0xFF_A098AE),
                    unfocusedTextColor = Color(0xFF_A098AE)
                ),
                placeholder = {
                    Text(
                        text = stringResource(R.string.enter_cities),
                        fontSize = 14.sp,
                        color = Color(0xFF_A098AE),
                        fontFamily = FontFamily(Font(R.font.poppins_regular))
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_loupe),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )
                },
                shape = RoundedCornerShape(20.dp),
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.onSearch(queryLocation = searchQuery)
                        focusManager.clearFocus()
                        navController.navigate(Screen.ResultScreen)
                        viewModel.clearTextField()
                    },
                )
            )
        }
    }
}