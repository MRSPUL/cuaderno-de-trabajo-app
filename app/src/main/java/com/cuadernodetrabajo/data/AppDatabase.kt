package com.cuadernodetrabajo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cuadernodetrabajo.model.Corte

@Database(entities = [Corte::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun corteDao(): CorteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cortes_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}