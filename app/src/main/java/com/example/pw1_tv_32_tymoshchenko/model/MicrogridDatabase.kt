package com.example.pw1_tv_32_tymoshchenko.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Абстрактний клас бази даних Room.
 */
@Database(entities = [MicrogridEntity::class], version = 1, exportSchema = false)
abstract class MicrogridDatabase : RoomDatabase() {
    abstract fun microgridDao(): MicrogridDao

    companion object {
        @Volatile
        private var INSTANCE: MicrogridDatabase? = null

        fun getDatabase(context: Context): MicrogridDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MicrogridDatabase::class.java,
                    "microgrid_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}