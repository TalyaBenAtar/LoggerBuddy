package com.example.loggerbuddy.filter

import com.example.loggerbuddy.LogLevel

/**
 * Represents the currently selected filters in LoggerBuddy.
 */
data class LogFilter(

    /**
     * The selected log levels.
     *
     * By default, every level is enabled.
     */
    val levels: Set<LogLevel> = LogLevel.entries.toSet(),

    /**
     * Optional search query.
     *
     * Searches both the log message and tag.
     */
    val searchQuery: String = "",

    /**
     * Optional minimum timestamp (inclusive).
     */
    val fromTimestamp: Long? = null,

    /**
     * Optional maximum timestamp (inclusive).
     */
    val toTimestamp: Long? = null
)