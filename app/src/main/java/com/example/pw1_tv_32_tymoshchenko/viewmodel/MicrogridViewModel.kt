package com.example.pw1_tv_32_tymoshchenko.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pw1_tv_32_tymoshchenko.model.MicrogridCalculator
import com.example.pw1_tv_32_tymoshchenko.model.MicrogridEntity
import com.example.pw1_tv_32_tymoshchenko.model.MicrogridRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI State для екрану введення та розрахунку мікрогріду.
 */
data class MicrogridUiState(
    val stationName: String = "",
    val powerInput: String = "",
    val efficiencyInput: String = "",
    val loadInput: String = "",
    val effectivePower: Double = 0.0,
    val energyBalance: Double = 0.0,
    val isLoading: Boolean = false,
    val statusMessage: String = "",
    val isCalculationComplete: Boolean = false
)

/**
 * ViewModel для управління станом та бізнес-логікою у паттерні MVVM.
 */
class MicrogridViewModel : ViewModel() {
    private val calculator = MicrogridCalculator()

    private val _uiState = MutableStateFlow(MicrogridUiState())
    val uiState: StateFlow<MicrogridUiState> = _uiState.asStateFlow()

    fun updateStationName(name: String) {
        _uiState.update { it.copy(stationName = name) }
    }

    fun updatePowerInput(power: String) {
        _uiState.update { it.copy(powerInput = power) }
    }

    fun updateEfficiencyInput(efficiency: String) {
        _uiState.update { it.copy(efficiencyInput = efficiency) }
    }

    fun updateLoadInput(load: String) {
        _uiState.update { it.copy(loadInput = load) }
    }

    /**
     * Асинхронний розрахунок з використанням корутин та бізнес-логіки калькулятора.
     */
    fun performCalculation() {
        val nominalPower = _uiState.value.powerInput.toIntOrNull() ?: 0
        val efficiency = _uiState.value.efficiencyInput.toIntOrNull() ?: 100
        val load = _uiState.value.loadInput.toDoubleOrNull() ?: 0.0

        _uiState.update { it.copy(isLoading = true, statusMessage = "Обчислення енергобалансу...") }

        viewModelScope.launch {
            delay(800) // Симуляція асинхронної обробки

            val effPower = calculator.calculateEffectivePower(nominalPower, efficiency)
            val balance = calculator.calculateEnergyBalance(effPower, load)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    effectivePower = effPower,
                    energyBalance = balance,
                    statusMessage = "Розрахунок успішно завершено!",
                    isCalculationComplete = true
                )
            }
        }
    }

    /**
     * Зберігає поточний розрахунок у локальну базу даних Room через Repository.
     */
    fun saveCurrentRecord(repository: MicrogridRepository) {
        viewModelScope.launch {
            val entity = MicrogridEntity(
                stationName = _uiState.value.stationName.ifBlank { "Без назви" },
                nominalPower = (_uiState.value.powerInput.toIntOrNull() ?: 0).toDouble(),
                efficiency = (_uiState.value.efficiencyInput.toIntOrNull() ?: 100).toDouble(),
                load = _uiState.value.loadInput.toDoubleOrNull() ?: 0.0,
                effectivePower = _uiState.value.effectivePower,
                energyBalance = _uiState.value.energyBalance
            )
            repository.insert(entity)
        }
    }
}