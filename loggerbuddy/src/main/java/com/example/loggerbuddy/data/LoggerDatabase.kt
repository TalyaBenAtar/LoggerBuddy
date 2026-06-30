package com.example.loggerbuddy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.loggerbuddy.LogEntry

@Database(
    entities = [LogEntry::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(LogConverters::class)
abstract class LoggerDatabase : RoomDatabase() {

    abstract fun logDao(): LogDao

    companion object {
        @Volatile
        private var INSTANCE: LoggerDatabase? = null

        fun getInstance(context: Context): LoggerDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    LoggerDatabase::class.java,
                    "logger_buddy_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}