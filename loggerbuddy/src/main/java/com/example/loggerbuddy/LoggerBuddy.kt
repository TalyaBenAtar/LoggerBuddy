package com.example.loggerbuddy

import android.content.Context
import com.example.loggerbuddy.data.LogStorage
import kotlinx.coroutines.runBlocking
import android.content.Intent
import com.example.loggerbuddy.ui.LogViewerActivity

object LoggerBuddy {

    private var storage: LogStorage? = null

    fun initialize(context: Context) {
        storage = LogStorage(context.applicationContext)
    }

    fun log(
        message: String,
        tag: String = "App",
        level: LogLevel = LogLevel.INFO
    ) {
        val logStorage = requireStorage()

        val entry = LogEntry(
            timestamp = System.currentTimeMillis(),
            level = level,
            tag = tag,
            message = message
        )

        logStorage.saveLog(entry)
    }

    fun getLogs(): List<LogEntry> {
        return runBlocking {
            requireStorage().getLogs()
        }
    }

    fun clearLogs() {
        requireStorage().clearLogs()
    }

    private fun requireStorage(): LogStorage {
        return storage
            ?: throw IllegalStateException("LoggerBuddy must be initialized before use.")
    }

    fun showLogs(context: Context) {
        val intent = Intent(context, LogViewerActivity::class.java)
        context.startActivity(intent)
    }

    fun showConsole(context: Context) {
        showLogs(context)
    }
}