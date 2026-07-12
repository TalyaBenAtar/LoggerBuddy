package com.example.loggerbuddy

/**
 * Controls LoggerBuddy's behavior.
 *
 * @property minimumLevel
 * The lowest log level that LoggerBuddy will save.
 *
 * For example, when set to [LogLevel.WARNING], DEBUG and INFO logs
 * will be ignored.
 *
 * @property maximumStoredLogs
 * The maximum number of logs LoggerBuddy keeps in local storage.
 *
 * When this limit is exceeded, LoggerBuddy automatically removes
 * the oldest logs.
 *
 * @property crashCatchingEnabled
 * Determines whether LoggerBuddy automatically captures uncaught crashes.
 *
 * Crash catching begins only after [LoggerBuddy.initialize] is called.
 */
data class LoggerBuddyConfig(
    val minimumLevel: LogLevel = LogLevel.DEBUG,
    val maximumStoredLogs: Int = DEFAULT_MAXIMUM_STORED_LOGS,
    val crashCatchingEnabled: Boolean = true
) {

    init {
        require(maximumStoredLogs > 0) {
            "maximumStoredLogs must be greater than 0."
        }
    }

    companion object {
        const val DEFAULT_MAXIMUM_STORED_LOGS = 5_000
    }
}