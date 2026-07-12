package com.example.loggerbuddy.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.loggerbuddy.LogLevel
import com.example.loggerbuddy.LoggerBuddy
import com.example.loggerbuddy.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.widget.ImageButton

class LogDetailsActivity : AppCompatActivity() {

    private var logId: Long = INVALID_LOG_ID
    private lateinit var fullLogText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_details)
        supportActionBar?.apply {
            title = "Log Details"
            setDisplayHomeAsUpEnabled(true)
        }

        val levelName = intent.getStringExtra(EXTRA_LEVEL)
        val tag = intent.getStringExtra(EXTRA_TAG)
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val timestamp = intent.getLongExtra(
            EXTRA_TIMESTAMP,
            INVALID_TIMESTAMP
        )

        logId = intent.getLongExtra(
            EXTRA_LOG_ID,
            INVALID_LOG_ID
        )

        if (
            levelName == null ||
            tag == null ||
            message == null ||
            timestamp == INVALID_TIMESTAMP ||
            logId == INVALID_LOG_ID
        ) {
            Toast.makeText(
                this,
                "Unable to open log details.",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        val level = runCatching {
            LogLevel.valueOf(levelName)
        }.getOrElse {
            LogLevel.INFO
        }

        displayLog(
            level = level,
            tag = tag,
            message = message,
            timestamp = timestamp
        )

        setupActions()
    }

    private fun displayLog(
        level: LogLevel,
        tag: String,
        message: String,
        timestamp: Long
    ) {
        val levelTextView =
            findViewById<TextView>(R.id.detailsLevelTextView)

        val dateTextView =
            findViewById<TextView>(R.id.detailsDateTextView)

        val tagTextView =
            findViewById<TextView>(R.id.detailsTagTextView)

        val messageTextView =
            findViewById<TextView>(R.id.detailsMessageTextView)

        val levelColor = colorForLevel(level)

        levelTextView.text = level.name
        levelTextView.background = GradientDrawable().apply {
            cornerRadius = 30f
            setColor(levelColor)
        }

        val formattedDate = DATE_FORMATTER.format(
            Date(timestamp)
        )

        dateTextView.text = formattedDate
        tagTextView.text = tag
        messageTextView.text = message

        fullLogText = buildString {
            appendLine("Level: ${level.name}")
            appendLine("Date: $formattedDate")
            appendLine("Tag: $tag")
            appendLine()
            append(message)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupActions() {
        findViewById<Button>(R.id.copyLogButton)
            .setOnClickListener {
                copyLogToClipboard()
            }

        findViewById<Button>(R.id.deleteLogButton)
            .setOnClickListener {
                showDeleteConfirmation()
            }

        findViewById<ImageButton>(R.id.backButton)
            .setOnClickListener {
                finish()
            }
    }

    private fun copyLogToClipboard() {
        val clipboardManager = getSystemService(
            ClipboardManager::class.java
        )

        val clip = ClipData.newPlainText(
            "LoggerBuddy log",
            fullLogText
        )

        clipboardManager.setPrimaryClip(clip)

        Toast.makeText(
            this,
            "Log copied to clipboard.",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Delete this log?")
            .setMessage("This action cannot be undone.")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Delete") { dialog, _ ->

                lifecycleScope.launch {
                    val wasDeleted = LoggerBuddy.deleteLog(logId)

                    if (wasDeleted) {
                        Toast.makeText(
                            this@LogDetailsActivity,
                            "Log deleted.",
                            Toast.LENGTH_SHORT
                        ).show()

                        finish()
                    } else {
                        Toast.makeText(
                            this@LogDetailsActivity,
                            "The log no longer exists.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                dialog.dismiss()
            }
            .show()
    }

    private fun colorForLevel(level: LogLevel): Int {
        return when (level) {
            LogLevel.INFO -> getColor(R.color.logger_info)
            LogLevel.DEBUG -> getColor(R.color.logger_debug)
            LogLevel.WARNING -> getColor(R.color.logger_warning)
            LogLevel.ERROR -> getColor(R.color.logger_error)
        }
    }

    companion object {
        const val EXTRA_LOG_ID = "loggerbuddy.extra.LOG_ID"
        const val EXTRA_TIMESTAMP = "loggerbuddy.extra.TIMESTAMP"
        const val EXTRA_LEVEL = "loggerbuddy.extra.LEVEL"
        const val EXTRA_TAG = "loggerbuddy.extra.TAG"
        const val EXTRA_MESSAGE = "loggerbuddy.extra.MESSAGE"

        private const val INVALID_LOG_ID = -1L
        private const val INVALID_TIMESTAMP = -1L

        private val DATE_FORMATTER = SimpleDateFormat(
            "dd MMM yyyy • HH:mm:ss",
            Locale.getDefault()
        )
    }
}