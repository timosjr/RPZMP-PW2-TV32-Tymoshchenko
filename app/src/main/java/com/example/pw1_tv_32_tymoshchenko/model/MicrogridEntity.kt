package com.example.pw1_tv_32_tymoshchenko.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Дата клас визначення сутності Entity бази даних
 */

@Entity(tableName = "microgrid_history")
data class MicrogridEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val stationName: String,
    val nominalPower: Double,
    val efficiency: Double,
    val load: Double,
    val effectivePower: Double,
    val energyBalance: Double,
    val timestamp: Long = System.currentTimeMillis()
)