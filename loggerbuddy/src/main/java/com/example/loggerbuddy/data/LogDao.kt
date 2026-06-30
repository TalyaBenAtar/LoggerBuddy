package com.example.loggerbuddy.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.loggerbuddy.LogEntry

@Dao
interface LogDao {

    @Insert
    suspend fun insertLog(logEntry: LogEntry)

    @Query("SELECT * FROM logs ORDER BY timestamp DESC")
    suspend fun getAllLogs(): List<LogEntry>

    @Query("DELETE FROM logs")
    suspend fun clearLogs()
}