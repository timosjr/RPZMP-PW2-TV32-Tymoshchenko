package com.example.pw1_tv_32_tymoshchenko.advancedScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pw1_tv_32_tymoshchenko.Screen
import com.example.pw1_tv_32_tymoshchenko.components.MenuButton
import com.example.pw1_tv_32_tymoshchenko.components.ScreenTitle
import com.example.pw1_tv_32_tymoshchenko.model.MicrogridRepository
import java.text.SimpleDateFormat
import java.util.*

/**
 * Екран з історією
 */

@Composable
fun MicrogridHistoryScreen(
    navController: NavHostController,
    repository: MicrogridRepository
) {
    val historyList by repository.allRecords.collectAsState(initial = emptyList())

    var filterMode by remember { mutableStateOf("ALL") }

    val filteredList = when (filterMode) {
        "SURPLUS" -> historyList.filter { it.energyBalance >= 0 }
        "DEFICIT" -> historyList.filter { it.energyBalance < 0 }
        else -> historyList
    }

    // Форматер для дати та часу у форматі dd/MM/yy HH:mm
    val dateFormat = remember { SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenTitle("Історія розрахунків (Room)")
        Spacer(modifier = Modifier.height(8.dp))

        // Кнопки фільтрації
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { filterMode = "ALL" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (filterMode == "ALL") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text("Усі", fontSize = 12.sp)
            }
            Button(
                onClick = { filterMode = "SURPLUS" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (filterMode == "SURPLUS") MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text("Надлишок", fontSize = 12.sp)
            }
            Button(
                onClick = { filterMode = "DEFICIT" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (filterMode == "DEFICIT") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text("Дефіцит", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Записів за цим фільтром немає", color = MaterialTheme.colorScheme.outline)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredList) { record ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Об'єкт: ${record.stationName}", fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                                // Виведення відформатованої дати та часу
                                Text(
                                    text = dateFormat.format(Date(record.timestamp)),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Потужність: ${record.nominalPower} Вт | ККД: ${record.efficiency}%")
                            Text(text = "Генерація: %.1f Вт | Навантаження: %.1f Вт".format(record.effectivePower, record.load))
                            val balanceStr = if (record.energyBalance >= 0) "+%.1f Вт (надлишок)".format(record.energyBalance) else "%.1f Вт (дефіцит)".format(record.energyBalance)
                            Text(text = "Баланс: $balanceStr", color = if (record.energyBalance >= 0) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        MenuButton("Повернутися в меню") {
            navController.navigate(Screen.AdvancedScreen.MainScreen.route)
        }
    }
}