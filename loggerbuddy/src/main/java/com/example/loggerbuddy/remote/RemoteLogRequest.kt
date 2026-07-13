package com.example.loggerbuddy.remote

import com.google.gson.annotations.SerializedName

/**
 * JSON structure sent to the LoggerBuddy Retool workflow.
 */
internal data class RemoteLogRequest(
    val timestamp: String,
    val level: String,
    val tag: String,
    val message: String,

    @SerializedName("is_crash")
    val isCrash: Boolean,

    @SerializedName("stack_trace")
    val stackTrace: String,

    @SerializedName("device_model")
    val deviceModel: String,

    @SerializedName("android_version")
    val androidVersion: String,

    @SerializedName("app_version")
    val appVersion: String,

    @SerializedName("session_id")
    val sessionId: String
)