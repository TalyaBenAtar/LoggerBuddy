package com.example.loggerbuddy.export

import android.content.Context
import android.os.Build
import com.example.loggerbuddy.LogEntry
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Creates structured JSON exports from LoggerBuddy logs.
 */
object LogExporter {

    /**
     * Creates a JSON file containing the supplied logs, application metadata,
     * device metadata, and optional active-filter information.
     *
     * The file is stored inside the host application's cache directory.
     */
    fun createJsonExport(
        context: Context,
        logs: List<LogEntry>,
        filter: LogExportFilter? = null
    ): ExportResult {
        if (logs.isEmpty()) {
            return ExportResult.Failure(
                message = "No logs were supplied for export."
            )
        }

        return try {
            val generatedAt = System.currentTimeMillis()

            val exportDirectory = File(
                context.cacheDir,
                EXPORT_DIRECTORY_NAME
            )

            if (!exportDirectory.exists() && !exportDirectory.mkdirs()) {
                return ExportResult.Failure(
                    message = "LoggerBuddy could not create the export directory."
                )
            }

            val exportFile = File(
                exportDirectory,
                createFileName(generatedAt)
            )

            val jsonDocument = createJsonDocument(
                context = context,
                logs = logs,
                filter = filter,
                generatedAt = generatedAt
            )

            exportFile.writeText(
                text = jsonDocument.toString(JSON_INDENT_SPACES),
                charset = Charsets.UTF_8
            )

            ExportResult.Success(exportFile)
        } catch (exception: Exception) {
            ExportResult.Failure(
                message = "LoggerBuddy could not create the JSON export.",
                cause = exception
            )
        }
    }

    /**
     * Builds the complete root JSON object.
     */
    private fun createJsonDocument(
        context: Context,
        logs: List<LogEntry>,
        filter: LogExportFilter?,
        generatedAt: Long
    ): JSONObject {
        return JSONObject().apply {
            put(
                JSON_KEY_METADATA,
                createMetadataJson(
                    context = context,
                    generatedAt = generatedAt,
                    exportedLogCount = logs.size
                )
            )

            put(
                JSON_KEY_ACTIVE_FILTERS,
                filter?.let(::createFilterJson) ?: JSONObject.NULL
            )

            put(
                JSON_KEY_LOGS,
                createLogsJson(logs)
            )
        }
    }

    /**
     * Creates application, device, and export metadata.
     */
    private fun createMetadataJson(
        context: Context,
        generatedAt: Long,
        exportedLogCount: Int
    ): JSONObject {
        val packageManager = context.packageManager
        val packageName = context.packageName

        val applicationInfo = context.applicationInfo

        val applicationName = packageManager
            .getApplicationLabel(applicationInfo)
            .toString()

        val packageInfo = packageManager.getPackageInfo(
            packageName,
            0
        )

        val versionName = packageInfo.versionName
            ?.takeIf { it.isNotBlank() }
            ?: UNKNOWN_VALUE

        val versionCode =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toLong()
            }

        return JSONObject().apply {
            put("generatedAt", generatedAt)
            put(
                "generatedAtFormatted",
                DATE_TIME_FORMATTER.format(Date(generatedAt))
            )

            put("applicationName", applicationName)
            put("packageName", packageName)
            put("applicationVersionName", versionName)
            put("applicationVersionCode", versionCode)

            put("androidVersion", Build.VERSION.RELEASE)
            put("androidSdk", Build.VERSION.SDK_INT)
            put("deviceManufacturer", Build.MANUFACTURER)
            put("deviceModel", Build.MODEL)

            put("loggerBuddyVersion", LOGGER_BUDDY_VERSION)
            put("exportedLogCount", exportedLogCount)
        }
    }

    /**
     * Converts active filters into JSON metadata.
     */
    private fun createFilterJson(
        filter: LogExportFilter
    ): JSONObject {
        return JSONObject().apply {
            put(
                "levels",
                JSONArray(filter.levels)
            )

            put("searchQuery", filter.searchQuery)

            put(
                "fromTimestamp",
                filter.fromTimestamp ?: JSONObject.NULL
            )

            put(
                "toTimestamp",
                filter.toTimestamp ?: JSONObject.NULL
            )
        }
    }

    /**
     * Converts every exported log into structured JSON.
     */
    private fun createLogsJson(
        logs: List<LogEntry>
    ): JSONArray {
        return JSONArray().apply {
            logs.forEach { log ->
                put(
                    JSONObject().apply {
                        put("id", log.id)
                        put("timestamp", log.timestamp)

                        put(
                            "formattedDate",
                            DATE_TIME_FORMATTER.format(
                                Date(log.timestamp)
                            )
                        )

                        put("level", log.level.name)
                        put("tag", log.tag)
                        put("message", log.message)
                    }
                )
            }
        }
    }

    /**
     * Produces a filesystem-safe export filename.
     */
    private fun createFileName(
        generatedAt: Long
    ): String {
        val timestamp = FILE_NAME_FORMATTER.format(
            Date(generatedAt)
        )

        return "loggerbuddy_export_$timestamp.json"
    }

    private const val EXPORT_DIRECTORY_NAME =
        "loggerbuddy_exports"

    private const val JSON_KEY_METADATA =
        "exportMetadata"

    private const val JSON_KEY_ACTIVE_FILTERS =
        "activeFilters"

    private const val JSON_KEY_LOGS =
        "logs"

    private const val JSON_INDENT_SPACES = 2

    /*
     * We will later connect this to the actual published library version.
     */
    private const val LOGGER_BUDDY_VERSION = "2.0.0"

    private const val UNKNOWN_VALUE = "Unknown"

    private val DATE_TIME_FORMATTER =
        SimpleDateFormat(
            "dd MMM yyyy HH:mm:ss Z",
            Locale.getDefault()
        )

    private val FILE_NAME_FORMATTER =
        SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.US
        )
}