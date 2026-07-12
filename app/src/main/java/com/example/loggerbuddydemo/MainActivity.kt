package com.example.loggerbuddydemo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loggerbuddy.LogLevel
import com.example.loggerbuddy.LoggerBuddy
import com.example.loggerbuddy.LoggerBuddyConfig

class MainActivity : AppCompatActivity() {

    private lateinit var messageEditText: EditText
    private lateinit var tagEditText: EditText
    private lateinit var levelRadioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        LoggerBuddy.initialize(this)
        LoggerBuddy.initialize(
            context = this,
            config = LoggerBuddyConfig(
                minimumLevel = LogLevel.DEBUG,
                maximumStoredLogs = 5_000,
                crashCatchingEnabled = true
            )
        )
        messageEditText = findViewById(R.id.messageEditText)
        tagEditText = findViewById(R.id.tagEditText)
        levelRadioGroup = findViewById(R.id.levelRadioGroup)

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            LoggerBuddy.info(this, "User clicked login")
            Toast.makeText(this, "Login log added", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.loadDataButton).setOnClickListener {
            LoggerBuddy.debug(this, "Loading data")
            Toast.makeText(this, "Debug log added", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.warningButton).setOnClickListener {
            LoggerBuddy.warning(this, "Slow network")
            Toast.makeText(this, "Warning log added", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.errorButton).setOnClickListener {
            LoggerBuddy.error(this, "Failed to save profile")
            Toast.makeText(this, "Error log added", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.addLogButton).setOnClickListener {
            addCustomLog()
        }

        findViewById<Button>(R.id.openConsoleButton).setOnClickListener {
            LoggerBuddy.showConsole(this)
        }
    }

    private fun addCustomLog() {
        val message = messageEditText.text.toString().trim()
            .ifEmpty { "Demo log message" }

        val customTag = tagEditText.text.toString().trim()
            .ifEmpty { null }

        val level = when (levelRadioGroup.checkedRadioButtonId) {
            R.id.warningRadioButton -> LogLevel.WARNING
            R.id.errorRadioButton -> LogLevel.ERROR
            R.id.debugRadioButton -> LogLevel.DEBUG
            else -> LogLevel.INFO
        }

        LoggerBuddy.log(
            context = this,
            message = message,
            tag = customTag,
            level = level
        )

        Toast.makeText(this, "Log added", Toast.LENGTH_SHORT).show()
        messageEditText.text.clear()
        tagEditText.text.clear()
    }
}