package com.example.pw1_tv_32_tymoshchenko.model

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Репозиторій для керування джерелами даних бази Room із підтримкою кешування та обробки помилок.
 */
class MicrogridRepository(private val microgridDao: MicrogridDao) {

    // Кеш у пам'яті для швидкого доступу до останніх завантажених записів
    private val _cachedRecords = MutableStateFlow<List<MicrogridEntity>>(emptyList())
    val cachedRecords = _cachedRecords.asStateFlow()

    // Потік даних із локальної бази Room
    val allRecords: Flow<List<MicrogridEntity>> = microgridDao.getAllRecords()

    /**
     * Безпечна вставка запису в базу даних із обробкою винятків та оновленням кешу.
     */
    suspend fun insert(record: MicrogridEntity) {
        try {
            microgridDao.insertRecord(record)
            // Оновлюємо локальний кеш у пам'яті
            _cachedRecords.update { currentList ->
                listOf(record) + currentList
            }
        } catch (e: Exception) {
            Log.e("MicrogridRepository", "Помилка під час збереження розрахунку в Room: ${e.localizedMessage}")
        }
    }

    /**
     * Очищення історії з обробкою можливих помилок.
     */
    suspend fun clear() {
        try {
            microgridDao.clearHistory()
            // Очищуємо кеш у пам'яті
            _cachedRecords.value = emptyList()
        } catch (e: Exception) {
            Log.e("MicrogridRepository", "Помилка під час очищення бази даних: ${e.localizedMessage}")
        }
    }
}