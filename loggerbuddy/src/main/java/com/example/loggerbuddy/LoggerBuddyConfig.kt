package com.example.loggerbuddy

/**
 * Controls LoggerBuddy's behavior.
 *
 * @property minimumLevel
 * The lowest log level that LoggerBuddy will save.
 *
 * @property maximumStoredLogs
 * The maximum number of logs LoggerBuddy keeps in local storage.
 *
 * @property crashCatchingEnabled
 * Determines whether LoggerBuddy automatically captures uncaught crashes.
 *
 * @property remoteLoggingEnabled
 * Determines whether logs are also uploaded to a remote LoggerBuddy dashboard.
 *
 * @property remoteEndpoint
 * Full Retool workflow webhook endpoint.
 *
 * @property remoteApiKey
 * API key used to authenticate requests to the Retool webhook.
 */
data class LoggerBuddyConfig(
    val minimumLevel: LogLevel = LogLevel.DEBUG,
    val maximumStoredLogs: Int = DEFAULT_MAXIMUM_STORED_LOGS,
    val crashCatchingEnabled: Boolean = true,

    val remoteLoggingEnabled: Boolean = false,
    val remoteEndpoint: String? = null,
    val remoteApiKey: String? = null
) {

    init {
        require(maximumStoredLogs > 0) {
            "maximumStoredLogs must be greater than 0."
        }

        if (remoteLoggingEnabled) {
            require(!remoteEndpoint.isNullOrBlank()) {
                "remoteEndpoint must be provided when remote logging is enabled."
            }

            require(!remoteApiKey.isNullOrBlank()) {
                "remoteApiKey must be provided when remote logging is enabled."
            }
        }
    }

    companion object {
        const val DEFAULT_MAXIMUM_STORED_LOGS = 5_000
    }
}