package com.example.loggerbuddy.export

import java.io.File

/**
 * Represents the result of a LoggerBuddy export operation.
 */
sealed class ExportResult {

    /**
     * Export completed successfully.
     *
     * @property file The generated JSON export file.
     */
    data class Success(
        val file: File
    ) : ExportResult()

    /**
     * Export could not be completed.
     *
     * @property message A developer-readable explanation.
     * @property cause The original exception, when available.
     */
    data class Failure(
        val message: String,
        val cause: Throwable? = null
    ) : ExportResult()
}