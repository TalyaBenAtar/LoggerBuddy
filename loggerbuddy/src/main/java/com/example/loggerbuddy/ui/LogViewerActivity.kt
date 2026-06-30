package com.example.loggerbuddy.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loggerbuddy.LoggerBuddy
import com.example.loggerbuddy.R
import com.example.loggerbuddy.LogLevel

class LogViewerActivity : AppCompatActivity() {

    private lateinit var logsRecyclerView: RecyclerView
    private lateinit var emptyTextView: TextView
    private lateinit var statsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_viewer)

        logsRecyclerView = findViewById(R.id.logsRecyclerView)
        emptyTextView = findViewById(R.id.emptyTextView)
        statsTextView = findViewById(R.id.statsTextView)

        findViewById<Button>(R.id.clearConsoleButton).setOnClickListener {
            showClearConfirmation()
        }

        displayLogs()
    }

    private fun displayLogs() {
        val logs = LoggerBuddy.getLogs()

        val infoCount = logs.count { it.level == LogLevel.INFO }
        val warningCount = logs.count { it.level == LogLevel.WARNING }
        val errorCount = logs.count { it.level == LogLevel.ERROR }
        val debugCount = logs.count { it.level == LogLevel.DEBUG }

        statsTextView.text =
            "${logs.size} logs • $infoCount INFO • $warningCount WARN • $errorCount ERROR • $debugCount DEBUG"

//        if (logs.isEmpty()) {
//            emptyTextView.visibility = TextView.VISIBLE
//            logsRecyclerView.visibility = RecyclerView.GONE
//            return
//        }
        if (logs.isEmpty()) {
            logsRecyclerView.adapter = LogAdapter(emptyList())
            emptyTextView.visibility = TextView.VISIBLE
            logsRecyclerView.visibility = RecyclerView.GONE
            return
        }

        emptyTextView.visibility = TextView.GONE
        logsRecyclerView.visibility = RecyclerView.VISIBLE

        logsRecyclerView.layoutManager = LinearLayoutManager(this)
        logsRecyclerView.adapter = LogAdapter(logs)
    }

//    private fun showClearConfirmation() {
//        AlertDialog.Builder(this)
//            .setTitle("Clear all logs?")
//            .setMessage("This action cannot be undone.")
//            .setNegativeButton("Cancel", null)
//            .setPositiveButton("Clear") { _, _ ->
//                LoggerBuddy.clearLogs()
//                displayLogs()
//            }
//            .show()
//    }
    private fun showClearConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Clear all logs?")
            .setMessage("This action cannot be undone.")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Clear") { dialog, _ ->
                LoggerBuddy.clearLogs()

                logsRecyclerView.adapter = LogAdapter(emptyList())
                emptyTextView.visibility = TextView.VISIBLE
                logsRecyclerView.visibility = RecyclerView.GONE

                dialog.dismiss()

                logsRecyclerView.post {
                    displayLogs()
                }
            }
            .show()
    }

}