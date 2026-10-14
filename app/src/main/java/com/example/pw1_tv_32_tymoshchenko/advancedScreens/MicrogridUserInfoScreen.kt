package com.example.pw1_tv_32_tymoshchenko.advancedScreens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pw1_tv_32_tymoshchenko.Screen
import com.example.pw1_tv_32_tymoshchenko.components.MenuButton
import com.example.pw1_tv_32_tymoshchenko.components.ScreenTitle

/**
 * Сторінка інформації оператора
 */

@Composable
fun MicrogridUserInfoScreen(navController: NavHostController) {
    var operatorName by rememberSaveable { mutableStateOf("") }
    var gridStation by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenTitle("Ідентифікація оператора")

        OutlinedTextField(
            value = operatorName,
            onValueChange = { operatorName = it },
            label = { androidx.compose.material3.Text("Ім'я диспетчера") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = gridStation,
            onValueChange = { gridStation = it },
            label = { androidx.compose.material3.Text("Станція / Об'єкт") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MenuButton("Назад") {
                navController.popBackStack()
            }
            MenuButton("До прогнозу") {
                val nameToSend = if (operatorName.isBlank()) "Черговий" else operatorName
                navController.navigate("${Screen.AdvancedScreen.SolarForecast.route}/$nameToSend")
            }
        }
    }
}