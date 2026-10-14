package com.example.pw1_tv_32_tymoshchenko.advancedScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pw1_tv_32_tymoshchenko.Screen
import com.example.pw1_tv_32_tymoshchenko.components.MenuButton
import com.example.pw1_tv_32_tymoshchenko.components.ScreenTitle
import com.example.pw1_tv_32_tymoshchenko.viewmodel.MicrogridViewModel

/**
 * Екран форм введення параметрів мікрогріду з валідацією та підказками
 */
@Composable
fun MicrogridForecastScreen(
    navController: NavHostController,
    operatorName: String,
    viewModel: MicrogridViewModel
) {
    val state by viewModel.uiState.collectAsState()

    // Локальні стани помилок для валідації полів
    var stationError by rememberSaveable { mutableStateOf(false) }
    var powerError by rememberSaveable { mutableStateOf(false) }
    var efficiencyError by rememberSaveable { mutableStateOf(false) }
    var loadError by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenTitle("Параметри мікрогріду")
        Text("Оператор: $operatorName", fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary)

        Spacer(modifier = Modifier.height(16.dp))

        // 1. Поле введення назви станції (підтримує українську)
        OutlinedTextField(
            value = state.stationName,
            onValueChange = {
                viewModel.updateStationName(it)
                stationError = false
            },
            label = { Text("Назва станції / об'єкта") },
            isError = stationError,
            modifier = Modifier.fillMaxWidth()
        )
        if (stationError) {
            Text("Поле не може бути порожнім", color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
        } else {
            Text("Введіть назву об'єкта (можна українською)", color = MaterialTheme.colorScheme.outline, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. Потужність ВДЕ
        OutlinedTextField(
            value = state.powerInput,
            onValueChange = {
                viewModel.updatePowerInput(it)
                powerError = false
            },
            label = { Text("Номінальна потужність ВДЕ (Вт)") },
            isError = powerError,
            modifier = Modifier.fillMaxWidth()
        )
        if (powerError) {
            Text("Введіть додатне число (наприклад: 1500)", color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
        } else {
            Text("Наприклад: 1500 Вт", color = MaterialTheme.colorScheme.outline, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 3. Ефективність інвертора
        OutlinedTextField(
            value = state.efficiencyInput,
            onValueChange = {
                viewModel.updateEfficiencyInput(it)
                efficiencyError = false
            },
            label = { Text("Ефективність інвертора (%)") },
            isError = efficiencyError,
            modifier = Modifier.fillMaxWidth()
        )
        if (efficiencyError) {
            Text("Значення має бути від 1 до 100", color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
        } else {
            Text("Діапазон від 1% до 100%", color = MaterialTheme.colorScheme.outline, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 4. Очікуване навантаження споживачів
        OutlinedTextField(
            value = state.loadInput,
            onValueChange = {
                viewModel.updateLoadInput(it)
                loadError = false
            },
            label = { Text("Навантаження споживачів (Вт)") },
            isError = loadError,
            modifier = Modifier.fillMaxWidth()
        )
        if (loadError) {
            Text("Введіть коректне число для навантаження", color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
        } else {
            Text("Наприклад: 1200 Вт", color = MaterialTheme.colorScheme.outline, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
        }

        if (state.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка розрахунку з комплексною валідацією
        MenuButton("Розрахувати енергобаланс") {
            val powerVal = state.powerInput.toIntOrNull()
            val effVal = state.efficiencyInput.toIntOrNull()
            val loadVal = state.loadInput.toDoubleOrNull()

            stationError = state.stationName.isBlank()
            powerError = powerVal == null || powerVal <= 0
            efficiencyError = effVal == null || effVal !in 1..100
            loadError = loadVal == null || loadVal < 0

            if (!stationError && !powerError && !efficiencyError && !loadError) {
                viewModel.performCalculation()
                navController.navigate(Screen.AdvancedScreen.SolarDetails.route)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        MenuButton("Повернутися в меню") {
            navController.navigate(Screen.AdvancedScreen.MainScreen.route)
        }
    }
}