package com.example.loggerbuddy.export

import com.example.loggerbuddy.LogLevel
import com.example.loggerbuddy.filter.LogFilter

/**
 * Snapshot of the filters used when logs were exported.
 *
 * This is kept separate from the UI filter model so the exported JSON
 * contains only stable, serializable values.
 */
data class LogExportFilter(
    val levels: List<String>,
    val searchQuery: String,
    val fromTimestamp: Long?,
    val toTimestamp: Long?
) {

    companion object {

        /**
         * Converts LoggerBuddy's current filter into export metadata.
         */
        fun from(filter: LogFilter): LogExportFilter {
            return LogExportFilter(
                levels = filter.levels
                    .sortedBy(LogLevel::priority)
                    .map(LogLevel::name),

                searchQuery = filter.searchQuery.trim(),
                fromTimestamp = filter.fromTimestamp,
                toTimestamp = filter.toTimestamp
            )
        }
    }
}