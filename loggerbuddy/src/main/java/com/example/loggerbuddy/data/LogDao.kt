package com.example.loggerbuddy.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.loggerbuddy.LogEntry

@Dao
interface LogDao {

    /**
     * Saves a new log entry.
     */
    @Insert
    suspend fun insertLog(logEntry: LogEntry)

    /**
     * Returns all logs from newest to oldest.
     */
    @Query("SELECT * FROM logs ORDER BY timestamp DESC, id DESC")
    suspend fun getAllLogs(): List<LogEntry>

    /**
     * Returns the current number of stored logs.
     */
    @Query("SELECT COUNT(*) FROM logs")
    suspend fun getLogCount(): Int

    /**
     * Deletes the requested number of oldest logs.
     *
     * The ID is used as a secondary ordering value when multiple logs
     * have the same timestamp.
     */
    @Query(
        """
        DELETE FROM logs
        WHERE id IN (
            SELECT id
            FROM logs
            ORDER BY timestamp ASC, id ASC
            LIMIT :amount
        )
        """
    )
    suspend fun deleteOldestLogs(amount: Int)

    /**
     * Deletes a single log using its unique ID.
     */
    @Query("DELETE FROM logs WHERE id = :logId")
    suspend fun deleteLogById(logId: Long): Int

    /**
     * Deletes all logs whose IDs are included in the supplied list.
     *
     * Returns the number of deleted database rows.
     */
    @Query("DELETE FROM logs WHERE id IN (:logIds)")
    suspend fun deleteLogsByIds(logIds: List<Long>): Int

    /**
     * Deletes every saved log.
     */
    @Query("DELETE FROM logs")
    suspend fun clearLogs()
}