package com.example.loggerbuddy.ui

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
import android.content.Context

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
        val color = colorForLevel(holder.itemView.context, log.level)

        holder.level.text = log.level.name
        holder.tag.text = log.tag
        holder.message.text = log.message
        holder.accentView.setBackgroundColor(color)

        holder.level.background = GradientDrawable().apply {
            cornerRadius = 30f
            setColor(color)
        }

        holder.time.text = timeFormatter.format(Date(log.timestamp))
    }

    private fun colorForLevel(
        context: Context,
        level: LogLevel
    ): Int {
        return when (level) {
            LogLevel.INFO -> context.getColor(R.color.logger_info)
            LogLevel.WARNING -> context.getColor(R.color.logger_warning)
            LogLevel.ERROR -> context.getColor(R.color.logger_error)
            LogLevel.DEBUG -> context.getColor(R.color.logger_debug)
        }
    }

    private val timeFormatter = SimpleDateFormat(
        "HH:mm:ss",
        Locale.getDefault()
    )
}