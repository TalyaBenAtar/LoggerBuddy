package com.example.loggerbuddydemo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loggerbuddy.LogLevel
import com.example.loggerbuddy.LoggerBuddy

class MainActivity : AppCompatActivity() {

    private lateinit var messageEditText: EditText
    private lateinit var tagEditText: EditText
    private lateinit var levelRadioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LoggerBuddy.initialize(this)

        messageEditText = findViewById(R.id.messageEditText)
        tagEditText = findViewById(R.id.tagEditText)
        levelRadioGroup = findViewById(R.id.levelRadioGroup)

        findViewById<Button>(R.id.addLogButton).setOnClickListener {
            addCustomLog()
        }

        findViewById<Button>(R.id.openConsoleButton).setOnClickListener {
            LoggerBuddy.showConsole(this)
        }

        findViewById<Button>(R.id.clearLogsButton).setOnClickListener {
            LoggerBuddy.clearLogs()
            Toast.makeText(this, "Logs cleared", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addCustomLog() {
        val message = messageEditText.text.toString().trim()
            .ifEmpty { "Demo log message" }

        val tag = tagEditText.text.toString().trim()
            .ifEmpty { "MainActivity" }

        val level = when (levelRadioGroup.checkedRadioButtonId) {
            R.id.warningRadioButton -> LogLevel.WARNING
            R.id.errorRadioButton -> LogLevel.ERROR
            R.id.debugRadioButton -> LogLevel.DEBUG
            else -> LogLevel.INFO
        }

        LoggerBuddy.log(
            message = message,
            tag = tag,
            level = level
        )

        Toast.makeText(this, "Log added", Toast.LENGTH_SHORT).show()
        messageEditText.text.clear()
    }
}