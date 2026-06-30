package com.example.loggerbuddy.ui

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loggerbuddy.LogEntry
import com.example.loggerbuddy.LogLevel
import com.example.loggerbuddy.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LogAdapter(
    private val logs: List<LogEntry>
) : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    class LogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val accentView: View = view.findViewById(R.id.accentView)
        val level: TextView = view.findViewById(R.id.levelTextView)
        val time: TextView = view.findViewById(R.id.timeTextView)
        val tag: TextView = view.findViewById(R.id.tagTextView)
        val message: TextView = view.findViewById(R.id.messageTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_log_entry, parent, false)

        return LogViewHolder(view)
    }

    override fun getItemCount() = logs.size

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val log = logs[position]
        val color = colorForLevel(log.level)

        holder.level.text = log.level.name
        holder.tag.text = log.tag
        holder.message.text = log.message
        holder.accentView.setBackgroundColor(color)

        holder.level.background = GradientDrawable().apply {
            cornerRadius = 30f
            setColor(color)
        }

        holder.time.text = SimpleDateFormat(
            "HH:mm:ss",
            Locale.getDefault()
        ).format(Date(log.timestamp))
    }

    private fun colorForLevel(level: LogLevel): Int {
        return when (level) {
            LogLevel.INFO -> Color.parseColor("#22C55E")
            LogLevel.WARNING -> Color.parseColor("#F59E0B")
            LogLevel.ERROR -> Color.parseColor("#EF4444")
            LogLevel.DEBUG -> Color.parseColor("#3B82F6")
        }
    }
}