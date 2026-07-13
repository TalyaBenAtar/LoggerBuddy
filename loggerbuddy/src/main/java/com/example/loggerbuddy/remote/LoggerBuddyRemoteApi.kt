package com.example.loggerbuddy.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

internal interface LoggerBuddyRemoteApi {

    @POST
    fun uploadLog(
        @Url endpoint: String,
        @Header("X-Workflow-Api-Key") apiKey: String,
        @Body request: RemoteLogRequest
    ): Call<RemoteLogResponse>
}