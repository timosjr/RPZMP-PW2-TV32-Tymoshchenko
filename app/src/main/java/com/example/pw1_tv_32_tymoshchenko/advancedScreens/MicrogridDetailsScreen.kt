package com.example.pw1_tv_32_tymoshchenko.advancedScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pw1_tv_32_tymoshchenko.Screen
import com.example.pw1_tv_32_tymoshchenko.components.InfoRow
import com.example.pw1_tv_32_tymoshchenko.components.MenuButton
import com.example.pw1_tv_32_tymoshchenko.components.ScreenTitle
import com.example.pw1_tv_32_tymoshchenko.model.MicrogridRepository
import com.example.pw1_tv_32_tymoshchenko.viewmodel.MicrogridViewModel

/**
 * Екран деталей та енергобалансу мікрогріду з можливістю збереження в Room
 */
@Composable
fun MicrogridDetailsScreen(
    navController: NavHostController,
    viewModel: MicrogridViewModel,
    repository: MicrogridRepository
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenTitle("Деталі та енергобаланс")

        Text("Об'єкт: ${state.stationName.ifBlank { "Не вказано" }}", fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))

        InfoRow(label = "Номінальна потужність ВДЕ", value = "${state.powerInput.ifBlank { "0" }} Вт")
        InfoRow(label = "Ефективність інвертора", value = "${state.efficiencyInput.ifBlank { "0" }} %")
        InfoRow(label = "Ефективна генерація", value = "%.1f Вт".format(state.effectivePower))
        InfoRow(label = "Орієнтовне навантаження", value = "${state.loadInput.ifBlank { "0" }} Вт")

        Spacer(modifier = Modifier.height(12.dp))

        val balanceText = if (state.energyBalance >= 0) {
            "Надлишок: +%.1f Вт".format(state.energyBalance)
        } else {
            "Дефіцит: %.1f Вт".format(state.energyBalance)
        }

        Text(
            text = balanceText,
            fontSize = 18.sp,
            color = if (state.energyBalance >= 0) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка збереження в базу даних Room
        MenuButton("Зберегти в базу даних") {
            viewModel.saveCurrentRecord(repository)
            navController.navigate("microgrid_history")
        }

        Spacer(modifier = Modifier.height(8.dp))

        MenuButton("Повернутися до прогнозу") {
            navController.navigate("${Screen.AdvancedScreen.SolarForecast.route}/Anonymous")
        }

        MenuButton("Повернутися в меню") {
            navController.navigate(Screen.AdvancedScreen.MainScreen.route)
        }
    }
}