package com.example.loggerbuddy.data

import android.content.Context
import com.example.loggerbuddy.LogEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogStorage(context: Context) {

    private val database = LoggerDatabase.getInstance(context)
    private val logDao = database.logDao()

    private val storageScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    /**
     * Saves a log entry without blocking the calling thread.
     *
     * After insertion, the oldest logs are removed when the configured
     * maximum storage limit has been exceeded.
     */
    fun saveLog(
        logEntry: LogEntry,
        maximumStoredLogs: Int
    ) {
        storageScope.launch {
            logDao.insertLog(logEntry)
            trimLogsIfNeeded(maximumStoredLogs)
        }
    }

    /**
     * Returns all stored logs from newest to oldest.
     */
    suspend fun getLogs(): List<LogEntry> {
        return withContext(Dispatchers.IO) {
            logDao.getAllLogs()
        }
    }

    /**
     * Deletes one log by its unique ID.
     *
     * Returns true when a log was deleted and false when no matching
     * log existed.
     */
    suspend fun deleteLog(logId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            logDao.deleteLogById(logId) > 0
        }
    }

    /**
     * Deletes multiple logs using their unique IDs.
     *
     * Empty input is handled without accessing the database.
     *
     * @return the number of logs that were deleted.
     */
    suspend fun deleteLogs(logIds: Collection<Long>): Int {
        if (logIds.isEmpty()) {
            return 0
        }

        val uniqueIds = logIds
            .filter { it > 0 }
            .distinct()

        if (uniqueIds.isEmpty()) {
            return 0
        }

        return withContext(Dispatchers.IO) {
            logDao.deleteLogsByIds(uniqueIds)
        }
    }

    /**
     * Deletes all stored logs.
     */
    suspend fun clearLogs() {
        withContext(Dispatchers.IO) {
            logDao.clearLogs()
        }
    }

    /**
     * Removes the oldest logs when the configured maximum is exceeded.
     */
    private suspend fun trimLogsIfNeeded(maximumStoredLogs: Int) {
        val currentLogCount = logDao.getLogCount()
        val excessLogCount = currentLogCount - maximumStoredLogs

        if (excessLogCount > 0) {
            logDao.deleteOldestLogs(excessLogCount)
        }
    }
}