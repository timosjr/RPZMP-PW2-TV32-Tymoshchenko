package com.example.pw1_tv_32_tymoshchenko.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO для виконання операцій з базою даних Room.
 */
@Dao
interface MicrogridDao {
    @Query("SELECT * FROM microgrid_history ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<MicrogridEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: MicrogridEntity)

    @Query("DELETE FROM microgrid_history")
    suspend fun clearHistory()
}