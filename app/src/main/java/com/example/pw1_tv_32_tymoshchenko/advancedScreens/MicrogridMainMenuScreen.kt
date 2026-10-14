package com.example.pw1_tv_32_tymoshchenko.advancedScreens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pw1_tv_32_tymoshchenko.Screen
import com.example.pw1_tv_32_tymoshchenko.components.MenuButton
import com.example.pw1_tv_32_tymoshchenko.components.ScreenTitle

/**
 * Екран головного меню
 */

@Composable
fun MicrogridMainMenuScreen(navController: NavHostController) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isLargeScreen = maxWidth > 600.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenTitle("Управління Мікрогрідом")

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопки перегляду даних оператора та переходу о вікна прогнозу ВДЕ
            if (isLargeScreen) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    MenuButton("Дані оператора") {
                        navController.navigate(Screen.AdvancedScreen.UserInfo.route)
                    }
                    MenuButton("Прогноз ВДЕ") {
                        navController.navigate("${Screen.AdvancedScreen.SolarForecast.route}/Головний диспетчер")
                    }
                }
            } else {
                MenuButton("Дані оператора") {
                    navController.navigate(Screen.AdvancedScreen.UserInfo.route)
                }
                MenuButton("Прогноз ВДЕ") {
                    navController.navigate("${Screen.AdvancedScreen.SolarForecast.route}/Головний диспетчер")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Кнопка перегляду історії збережених розрахунків з Room Database
            MenuButton("Переглянути історію (Room)") {
                navController.navigate("microgrid_history")
            }
        }
    }
}