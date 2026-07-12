package com.example.loggerbuddy

import android.content.Context
import android.content.Intent
import com.example.loggerbuddy.data.LogStorage
import com.example.loggerbuddy.ui.LogViewerActivity
import java.io.PrintWriter
import java.io.StringWriter
import com.example.loggerbuddy.export.ExportResult
import com.example.loggerbuddy.export.LogExportFilter
import com.example.loggerbuddy.export.LogExporter

object LoggerBuddy {

    private var storage: LogStorage? = null
    private var config: LoggerBuddyConfig = LoggerBuddyConfig()

    private const val DEFAULT_TAG = "App"
    private const val EMPTY_MESSAGE_PLACEHOLDER = "(empty log message)"

    /**
     * Initializes LoggerBuddy using the default configuration.
     *
     * Recommended place:
     * Application.onCreate()
     */
    fun initialize(context: Context) {
        initialize(
            context = context,
            config = LoggerBuddyConfig()
        )
    }

    /**
     * Initializes LoggerBuddy using a custom configuration.
     *
     * Recommended place:
     * Application.onCreate()
     */
    fun initialize(
        context: Context,
        config: LoggerBuddyConfig
    ) {
        this.config = config
        storage = LogStorage(context.applicationContext)

        /*
         * Crash catching will be connected here later.
         *
         * if (config.crashCatchingEnabled) {
         *     LoggerBuddyCrashHandler.install(...)
         * }
         */
    }

    /**
     * Saves a log entry with a custom level.
     *
     * If tag is null, LoggerBuddy tries to detect the caller class.
     */
    fun log(
        message: String,
        tag: String? = null,
        level: LogLevel = LogLevel.INFO
    ) {
        saveLog(
            message = message,
            tag = tag,
            level = level
        )
    }

    /**
     * Saves a log entry with a custom level and uses the Context class name
     * as the default tag.
     */
    fun log(
        context: Context,
        message: String,
        tag: String? = null,
        level: LogLevel = LogLevel.INFO
    ) {
        saveLog(
            message = message,
            tag = tag ?: context.javaClass.simpleName,
            level = level
        )
    }

    /**
     * Saves an INFO log.
     */
    fun info(
        message: String,
        tag: String? = null
    ) {
        saveLog(
            message = message,
            tag = tag,
            level = LogLevel.INFO
        )
    }

    /**
     * Saves an INFO log and uses the Context class name as the default tag.
     */
    fun info(
        context: Context,
        message: String,
        tag: String? = null
    ) {
        saveLog(
            message = message,
            tag = tag ?: context.javaClass.simpleName,
            level = LogLevel.INFO
        )
    }

    /**
     * Saves a DEBUG log.
     */
    fun debug(
        message: String,
        tag: String? = null
    ) {
        saveLog(
            message = message,
            tag = tag,
            level = LogLevel.DEBUG
        )
    }

    /**
     * Saves a DEBUG log and uses the Context class name as the default tag.
     */
    fun debug(
        context: Context,
        message: String,
        tag: String? = null
    ) {
        saveLog(
            message = message,
            tag = tag ?: context.javaClass.simpleName,
            level = LogLevel.DEBUG
        )
    }

    /**
     * Saves a WARNING log.
     */
    fun warning(
        message: String,
        tag: String? = null
    ) {
        saveLog(
            message = message,
            tag = tag,
            level = LogLevel.WARNING
        )
    }

    /**
     * Saves a WARNING log and uses the Context class name as the default tag.
     */
    fun warning(
        context: Context,
        message: String,
        tag: String? = null
    ) {
        saveLog(
            message = message,
            tag = tag ?: context.javaClass.simpleName,
            level = LogLevel.WARNING
        )
    }

    /**
     * Saves an ERROR log.
     */
    fun error(
        message: String,
        tag: String? = null
    ) {
        saveLog(
            message = message,
            tag = tag,
            level = LogLevel.ERROR
        )
    }

    /**
     * Saves an ERROR log and uses the Context class name as the default tag.
     */
    fun error(
        context: Context,
        message: String,
        tag: String? = null
    ) {
        saveLog(
            message = message,
            tag = tag ?: context.javaClass.simpleName,
            level = LogLevel.ERROR
        )
    }

    /**
     * Saves an ERROR log with Throwable details.
     */
    fun error(
        message: String,
        throwable: Throwable,
        tag: String? = null
    ) {
        saveLog(
            message = buildErrorMessage(message, throwable),
            tag = tag,
            level = LogLevel.ERROR
        )
    }

    /**
     * Saves an ERROR log with Throwable details and uses the Context class name
     * as the default tag.
     */
    fun error(
        context: Context,
        message: String,
        throwable: Throwable,
        tag: String? = null
    ) {
        saveLog(
            message = buildErrorMessage(message, throwable),
            tag = tag ?: context.javaClass.simpleName,
            level = LogLevel.ERROR
        )
    }

    /**
     * Returns all saved logs from newest to oldest.
     *
     * This is a suspend function because it reads from the Room database.
     */
    suspend fun getLogs(): List<LogEntry> {
        return requireStorage().getLogs()
    }

    /**
     * Deletes one log by its unique ID.
     *
     * Returns true when a log was deleted.
     */
    suspend fun deleteLog(logId: Long): Boolean {
        return requireStorage().deleteLog(logId)
    }

    /**
     * Deletes multiple logs using their unique IDs.
     *
     * Duplicate and invalid IDs are safely ignored.
     *
     * @return the number of logs deleted from local storage.
     */
    suspend fun deleteLogs(logIds: Collection<Long>): Int {
        return requireStorage().deleteLogs(logIds)
    }

    /**
     * Deletes all saved logs.
     */
    suspend fun clearLogs() {
        requireStorage().clearLogs()
    }

    /**
     * Opens the built-in LoggerBuddy console screen.
     */
    fun showConsole(context: Context) {
        val intent = Intent(context, LogViewerActivity::class.java)
        context.startActivity(intent)
    }

    /**
     * Returns the configuration currently used by LoggerBuddy.
     */
    fun getConfig(): LoggerBuddyConfig {
        return config
    }

    /**
     * Saves the final log entry.
     *
     * All public logging functions go through this function.
     */
    private fun saveLog(
        message: String,
        tag: String?,
        level: LogLevel
    ) {
        if (!shouldSave(level)) {
            return
        }

        val finalMessage = message.ifBlank {
            EMPTY_MESSAGE_PLACEHOLDER
        }

        val finalTag = tag
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
            ?: detectCallerTag()

        val entry = LogEntry(
            timestamp = System.currentTimeMillis(),
            level = level,
            tag = finalTag,
            message = finalMessage
        )

        requireStorage().saveLog(
            logEntry = entry,
            maximumStoredLogs = config.maximumStoredLogs
        )
    }

    /**
     * Returns true when the log level meets the configured minimum level.
     */
    private fun shouldSave(level: LogLevel): Boolean {
        return level.priority >= config.minimumLevel.priority
    }

    /**
     * Combines a developer message with a Throwable stack trace.
     */
    private fun buildErrorMessage(
        message: String,
        throwable: Throwable
    ): String {
        val safeMessage = message.ifBlank {
            "An exception was logged."
        }

        return buildString {
            appendLine(safeMessage)
            appendLine()
            appendLine("Exception: ${throwable.javaClass.simpleName}")

            throwable.message
                ?.takeIf { it.isNotBlank() }
                ?.let {
                    appendLine("Message: $it")
                    appendLine()
                }

            appendLine("Stack trace:")
            append(throwable.stackTraceToStringSafe())
        }
    }

    /**
     * Returns the initialized storage object.
     */
    private fun requireStorage(): LogStorage {
        return storage
            ?: throw IllegalStateException(
                "LoggerBuddy must be initialized before use. " +
                        "Call LoggerBuddy.initialize(context) first."
            )
    }

    /**
     * Best-effort fallback for detecting the caller class.
     */
    private fun detectCallerTag(): String {
        val stackTrace = Throwable().stackTrace

        val caller = stackTrace.firstOrNull { element ->
            val className = element.className

            !className.startsWith("com.example.loggerbuddy") &&
                    !className.startsWith("java.") &&
                    !className.startsWith("javax.") &&
                    !className.startsWith("android.") &&
                    !className.startsWith("androidx.") &&
                    !className.startsWith("com.google.android.material.") &&
                    !className.startsWith("dalvik.") &&
                    !className.startsWith("kotlin.") &&
                    !className.startsWith("kotlinx.")
        }

        return caller?.className
            ?.substringAfterLast(".")
            ?.substringBefore("$")
            ?.takeIf { it.isNotBlank() }
            ?: DEFAULT_TAG
    }

    /**
     * Converts a Throwable stack trace into text.
     */
    private fun Throwable.stackTraceToStringSafe(): String {
        return try {
            val stringWriter = StringWriter()
            val printWriter = PrintWriter(stringWriter)

            printStackTrace(printWriter)
            printWriter.flush()

            stringWriter.toString()
        } catch (_: Exception) {
            "Stack trace could not be generated."
        }
    }


    /**
     * Creates a structured JSON export file from the supplied logs.
     *
     * The file contains:
     * - Application metadata
     * - Device metadata
     * - LoggerBuddy version
     * - Optional active-filter information
     * - Structured log entries
     */
    fun createJsonExport(
        context: Context,
        logs: List<LogEntry>,
        filter: LogExportFilter? = null
    ): ExportResult {
        return LogExporter.createJsonExport(
            context = context.applicationContext,
            logs = logs,
            filter = filter
        )
    }
}