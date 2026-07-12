package com.example.loggerbuddy

/**
 * Represents the severity of a LoggerBuddy log entry.
 *
 * The priority value is used when a minimum log level is configured.
 * Higher values represent more severe logs.
 */
enum class LogLevel(val priority: Int) {
    DEBUG(priority = 0),
    INFO(priority = 1),
    WARNING(priority = 2),
    ERROR(priority = 3)
}