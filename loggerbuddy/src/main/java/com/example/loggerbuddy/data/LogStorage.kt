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

    private val storageScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun saveLog(logEntry: LogEntry) {
        storageScope.launch {
            logDao.insertLog(logEntry)
        }
    }

    suspend fun getLogs(): List<LogEntry> {
        return withContext(Dispatchers.IO) {
            logDao.getAllLogs()
        }
    }

    fun clearLogs() {
        storageScope.launch {
            logDao.clearLogs()
        }
    }
}