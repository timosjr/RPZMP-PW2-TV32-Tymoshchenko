package com.example.pw1_tv_32_tymoshchenko.model

/**
 * Клас бізнес-логіки для розрахунку енергетичних показників мікрогріду
 * з урахуванням ефективності інвертора.
 */
class MicrogridCalculator {
    /**
     * Обчислює ефективну генерацію потужності ВДЕ.
     * @ номінальна потужність (Вт)
     * @ efficiency коефіцієнт ефективності інвертора в відсотках (1-100)
     */
    fun calculateEffectivePower(nominalPower: Int, efficiency: Int): Double {
        return nominalPower * (efficiency / 100.0)
    }

    /**
     * Обчислює баланс потужності (надлишок або дефіцит) відносно навантаження.
     */
    fun calculateEnergyBalance(effectivePower: Double, estimatedLoad: Double): Double {
        return effectivePower - estimatedLoad
    }
}