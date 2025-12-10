package com.example.skycast.core.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skycast.core.presentation.main_screen.MainScreen
import com.example.skycast.core.presentation.main_screen.MainViewModel
import com.example.skycast.core.presentation.result_screen.ResultScreen
import com.example.skycast.core.presentation.search_screen.SearchScreen
import com.example.skycast.core.presentation.search_screen.SearchViewModel
import com.example.skycast.core.presentation.splash_screen.SplashScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation(
    searchViewModel: SearchViewModel = koinViewModel(),
    mainViewModel: MainViewModel = koinViewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen
    ) {
        composable<Screen.SplashScreen>(
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }
        ){
            SplashScreen(
                navController = navController
            )
        }
        composable<Screen.MainScreen>(
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }
        ){
            MainScreen(
                navController = navController,
                viewModel = mainViewModel
            )
        }
        composable<Screen.SearchScreen>(
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }
        ){
            SearchScreen(
                navController = navController,
                viewModel = searchViewModel
            )
        }
        composable<Screen.ResultScreen>(
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }
        ){
            ResultScreen(
                navController = navController,
                viewModel = searchViewModel
            )
        }
    }
}