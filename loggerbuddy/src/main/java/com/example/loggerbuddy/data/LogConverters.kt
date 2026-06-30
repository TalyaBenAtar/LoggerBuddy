package com.example.loggerbuddy.data

import androidx.room.TypeConverter
import com.example.loggerbuddy.LogLevel

class LogConverters {

    @TypeConverter
    fun fromLogLevel(level: LogLevel): String {
        return level.name
    }

    @TypeConverter
    fun toLogLevel(value: String): LogLevel {
        return LogLevel.valueOf(value)
    }
}