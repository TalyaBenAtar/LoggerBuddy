package com.example.loggerbuddy

import android.content.Context
import android.content.Intent
import com.example.loggerbuddy.data.LogStorage
import com.example.loggerbuddy.ui.LogViewerActivity
import kotlinx.coroutines.runBlocking
import java.io.PrintWriter
import java.io.StringWriter

object LoggerBuddy {

    private var storage: LogStorage? = null

    /**
     * Initializes LoggerBuddy.
     *
     * Must be called once before saving or viewing logs.
     * Recommended place: Application.onCreate() or MainActivity.onCreate().
     */
    fun initialize(context: Context) {
        storage = LogStorage(context.applicationContext)
    }

    /**
     * Saves a log entry with a custom level.
     *
     * If tag is null, LoggerBuddy tries to detect the caller class as a fallback.
     * For reliable Activity names, prefer using log(context, message, tag, level).
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
     * Saves a log entry with a custom level and uses the Context class name as the tag.
     *
     * Recommended when logging from an Activity or other Context.
     *
     * Example:
     * LoggerBuddy.log(this, "User clicked button", level = LogLevel.INFO)
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
     *
     * Use INFO for normal app events, such as opening a screen,
     * clicking a button, or completing an action.
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
     * Saves an INFO log and uses the Context class name as the tag.
     *
     * Example:
     * LoggerBuddy.info(this, "Login button clicked")
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
     *
     * Use DEBUG for development details, such as variable values,
     * loading states, or flow checkpoints.
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
     * Saves a DEBUG log and uses the Context class name as the tag.
     *
     * Example:
     * LoggerBuddy.debug(this, "User data loading started")
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
     *
     * Use WARNING for issues that are not crashes, but may still need attention,
     * such as slow internet or missing optional data.
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
     * Saves a WARNING log and uses the Context class name as the tag.
     *
     * Example:
     * LoggerBuddy.warning(this, "Internet connection is slow")
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
     *
     * Use ERROR for failed actions, broken flows, or problems that should be checked.
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
     * Saves an ERROR log and uses the Context class name as the tag.
     *
     * Example:
     * LoggerBuddy.error(this, "Failed to save profile")
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
     * Saves an ERROR log with exception details.
     *
     * The saved message includes the custom message and the Throwable stack trace.
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
     * Saves an ERROR log with exception details and uses the Context class name as the tag.
     *
     * Recommended when catching exceptions inside an Activity.
     *
     * Example:
     * LoggerBuddy.error(this, "Failed to load data", exception)
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
     * Returns all saved logs.
     *
     * Used by LoggerBuddy's console screen, and can also be used by the host app.
     */
    fun getLogs(): List<LogEntry> {
        return runBlocking {
            requireStorage().getLogs()
        }
    }

    /**
     * Deletes all saved logs.
     */
    fun clearLogs() {
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
     * Saves the final log entry.
     *
     * All public logging functions go through this function to keep the saving
     * logic in one place.
     */
    private fun saveLog(
        message: String,
        tag: String?,
        level: LogLevel
    ) {
        val entry = LogEntry(
            timestamp = System.currentTimeMillis(),
            level = level,
            tag = tag ?: detectCallerTag(),
            message = message
        )

        requireStorage().saveLog(entry)
    }

    /**
     * Combines a developer message with a Throwable stack trace.
     */
    private fun buildErrorMessage(
        message: String,
        throwable: Throwable
    ): String {
        return buildString {
            appendLine(message)
            appendLine()
            appendLine(throwable.stackTraceToStringSafe())
        }
    }

    /**
     * Returns the initialized storage object.
     *
     * Throws a clear error if LoggerBuddy.initialize(context) was not called first.
     */
    private fun requireStorage(): LogStorage {
        return storage
            ?: throw IllegalStateException(
                "LoggerBuddy must be initialized before use. Call LoggerBuddy.initialize(context) first."
            )
    }

    /**
     * Best-effort fallback for detecting the caller class.
     *
     * Android stack traces can include framework classes such as RuntimeInit,
     * so this should not be treated as the main tagging method.
     *
     * Recommended developer usage:
     * LoggerBuddy.info(this, "Message")
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
            ?: "App"
    }

    /**
     * Converts a Throwable stack trace into text.
     */
    private fun Throwable.stackTraceToStringSafe(): String {
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        printStackTrace(printWriter)
        return stringWriter.toString()
    }
}