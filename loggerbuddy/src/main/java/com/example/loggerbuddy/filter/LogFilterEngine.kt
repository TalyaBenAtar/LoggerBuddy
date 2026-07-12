package com.example.loggerbuddy.filter

import com.example.loggerbuddy.LogEntry

/**
 * Applies search, level, and date filters to LoggerBuddy logs.
 */
object LogFilterEngine {

    /**
     * Returns only the logs that match all active filter categories.
     *
     * Filtering rules:
     * - Selected levels use OR logic.
     * - Search, level, and date categories use AND logic.
     * - Search is case-insensitive.
     * - Search checks both tag and message.
     * - Date boundaries are inclusive.
     */
    fun filter(
        logs: List<LogEntry>,
        filter: LogFilter
    ): List<LogEntry> {
        val normalizedQuery = filter.searchQuery.trim()

        return logs.filter { log ->
            matchesLevel(log, filter) &&
                    matchesSearch(log, normalizedQuery) &&
                    matchesDateRange(log, filter)
        }
    }

    /**
     * Returns true when the log's level is selected.
     *
     * When no levels are selected, no logs match.
     */
    private fun matchesLevel(
        log: LogEntry,
        filter: LogFilter
    ): Boolean {
        return log.level in filter.levels
    }

    /**
     * Returns true when the query is empty or appears in the log tag or message.
     */
    private fun matchesSearch(
        log: LogEntry,
        normalizedQuery: String
    ): Boolean {
        if (normalizedQuery.isEmpty()) {
            return true
        }

        return log.tag.contains(
            other = normalizedQuery,
            ignoreCase = true
        ) || log.message.contains(
            other = normalizedQuery,
            ignoreCase = true
        )
    }

    /**
     * Returns true when the log timestamp is inside the selected inclusive range.
     */
    private fun matchesDateRange(
        log: LogEntry,
        filter: LogFilter
    ): Boolean {
        val isAfterMinimum =
            filter.fromTimestamp == null ||
                    log.timestamp >= filter.fromTimestamp

        val isBeforeMaximum =
            filter.toTimestamp == null ||
                    log.timestamp <= filter.toTimestamp

        return isAfterMinimum && isBeforeMaximum
    }
}