package com.example.loggerbuddy.remote

import android.content.Context
import android.os.Build
import android.util.Log
import com.example.loggerbuddy.LogEntry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient

internal class RemoteLogUploader(
    context: Context,
    private val endpoint: String,
    private val apiKey: String
) {

    private val applicationContext = context.applicationContext

    private val sessionId = UUID.randomUUID().toString()


    private val appVersion: String by lazy {
        getApplicationVersion()
    }

    private val api: LoggerBuddyRemoteApi

    init {
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(3, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.SECONDS)
            .build()

        api = Retrofit.Builder()
            // Retrofit requires a base URL even though @Url supplies the real URL.
            .baseUrl("https://api.retool.com/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoggerBuddyRemoteApi::class.java)
    }

    /**
     * Uploads a log asynchronously.
     *
     * Upload failures never interrupt local LoggerBuddy behavior.
     */
    fun upload(
        entry: LogEntry,
        isCrash: Boolean = false,
        stackTrace: String = ""
    ) {
        val request = createRequest(
            entry = entry,
            isCrash = isCrash,
            stackTrace = stackTrace
        )

        api.uploadLog(
            endpoint = endpoint,
            apiKey = apiKey,
            request = request
        ).enqueue(
            object : Callback<RemoteLogResponse> {

                override fun onResponse(
                    call: Call<RemoteLogResponse>,
                    response: Response<RemoteLogResponse>
                ) {
                    if (!response.isSuccessful) {
                        Log.w(
                            INTERNAL_TAG,
                            "Remote log upload failed with HTTP ${response.code()}."
                        )
                    }
                }

                override fun onFailure(
                    call: Call<RemoteLogResponse>,
                    throwable: Throwable
                ) {
                    Log.w(
                        INTERNAL_TAG,
                        "Remote log upload failed: ${throwable.message}"
                    )
                }
            }
        )
    }

    /**
     * Best-effort synchronous upload for uncaught crashes.
     *
     * The short network timeout prevents crash reporting from hanging forever.
     */
    fun uploadCrashSynchronously(
        entry: LogEntry,
        stackTrace: String
    ) {
        val uploadThread = Thread {
            try {
                val request = createRequest(
                    entry = entry,
                    isCrash = true,
                    stackTrace = stackTrace
                )

                val response = api.uploadLog(
                    endpoint = endpoint,
                    apiKey = apiKey,
                    request = request
                ).execute()

                if (!response.isSuccessful) {
                    Log.w(
                        INTERNAL_TAG,
                        "Crash upload failed with HTTP ${response.code()}."
                    )
                } else {
                    Log.d(
                        INTERNAL_TAG,
                        "Crash uploaded successfully."
                    )
                }
            } catch (throwable: Throwable) {
                Log.w(
                    INTERNAL_TAG,
                    "Crash upload failed: ${throwable.message}",
                    throwable
                )
            }
        }

        uploadThread.start()

        try {
            // Give Retool a few seconds to receive the crash before Android kills the app.
            uploadThread.join(CRASH_UPLOAD_WAIT_MS)
        } catch (interruptedException: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    private fun createRequest(
        entry: LogEntry,
        isCrash: Boolean,
        stackTrace: String
    ): RemoteLogRequest {
        return RemoteLogRequest(
            timestamp = formatTimestamp(entry.timestamp),
            level = entry.level.name,
            tag = entry.tag,
            message = entry.message,
            isCrash = isCrash,
            stackTrace = stackTrace,
            deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}".trim(),
            androidVersion = Build.VERSION.RELEASE ?: "Unknown",
            appVersion = appVersion,
            sessionId = sessionId
        )
    }

    private fun formatTimestamp(timestamp: Long): String {
        val formatter = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.US
        )

        formatter.timeZone = TimeZone.getTimeZone("UTC")

        return formatter.format(Date(timestamp))
    }

    @Suppress("DEPRECATION")
    private fun getApplicationVersion(): String {
        return try {
            val packageInfo = applicationContext.packageManager.getPackageInfo(
                applicationContext.packageName,
                0
            )

            packageInfo.versionName ?: "Unknown"
        } catch (_: Exception) {
            "Unknown"
        }
    }

    private companion object {
        const val INTERNAL_TAG = "LoggerBuddyRemote"
        private const val CRASH_UPLOAD_WAIT_MS = 4_000L
    }
}