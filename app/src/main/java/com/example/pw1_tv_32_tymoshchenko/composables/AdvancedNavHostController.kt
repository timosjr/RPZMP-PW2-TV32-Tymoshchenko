package com.example.pw1_tv_32_tymoshchenko.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pw1_tv_32_tymoshchenko.Screen
import com.example.pw1_tv_32_tymoshchenko.advancedScreens.MicrogridMainMenuScreen
import com.example.pw1_tv_32_tymoshchenko.advancedScreens.MicrogridDetailsScreen
import com.example.pw1_tv_32_tymoshchenko.advancedScreens.MicrogridForecastScreen
import com.example.pw1_tv_32_tymoshchenko.advancedScreens.MicrogridHistoryScreen
import com.example.pw1_tv_32_tymoshchenko.advancedScreens.MicrogridUserInfoScreen
import com.example.pw1_tv_32_tymoshchenko.model.MicrogridDatabase
import com.example.pw1_tv_32_tymoshchenko.model.MicrogridRepository
import com.example.pw1_tv_32_tymoshchenko.viewmodel.MicrogridViewModel

/**
 * Розширений навігаційний контролер для просунутого рівня (ПЗ №5 - ПЗ №8).
 * Керує переходами між екранами, ViewModel та локальним сховищем Room.
 */
@Composable
fun AdvancedNavHostController(
    navController: NavHostController
) {
    val context = LocalContext.current
    // Ініціалізація бази даних та репозиторію
    val database = remember { MicrogridDatabase.getDatabase(context) }
    val repository = remember { MicrogridRepository(database.microgridDao()) }

    val sharedViewModel: MicrogridViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.AdvancedScreen.MainScreen.route) {

        composable(Screen.AdvancedScreen.MainScreen.route) {
            MicrogridMainMenuScreen(navController)
        }

        composable(Screen.AdvancedScreen.UserInfo.route) {
            MicrogridUserInfoScreen(navController)
        }

        composable(
            route = "${Screen.AdvancedScreen.SolarForecast.route}/{userName}",
            arguments = listOf(navArgument("userName") { type = NavType.StringType })
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: "Користувач"
            MicrogridForecastScreen(navController, userName, viewModel = sharedViewModel)
        }

        composable(Screen.AdvancedScreen.SolarDetails.route) {
            MicrogridDetailsScreen(navController, viewModel = sharedViewModel, repository = repository)
        }

        // Маршрут для екрана історії з Room Database
        composable("microgrid_history") {
            MicrogridHistoryScreen(navController, repository = repository)
        }
    }
}