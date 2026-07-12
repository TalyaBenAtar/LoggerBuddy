package com.example.loggerbuddy.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loggerbuddy.LogEntry
import com.example.loggerbuddy.LogLevel
import com.example.loggerbuddy.LoggerBuddy
import com.example.loggerbuddy.R
import com.example.loggerbuddy.filter.LogFilter
import com.example.loggerbuddy.filter.LogFilterEngine
import kotlinx.coroutines.launch
import android.widget.Toast
import android.app.DatePickerDialog
import android.view.View
import android.widget.PopupMenu
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.loggerbuddy.export.ExportResult
import com.example.loggerbuddy.export.LogExportFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class LogViewerActivity : AppCompatActivity() {

    private lateinit var logsRecyclerView: RecyclerView
    private lateinit var emptyTextView: TextView
    private lateinit var statsTextView: TextView
    private lateinit var searchEditText: EditText

    private lateinit var infoCheckBox: CheckBox
    private lateinit var debugCheckBox: CheckBox
    private lateinit var warningCheckBox: CheckBox
    private lateinit var errorCheckBox: CheckBox
    private lateinit var dateFilterButton: Button
    private lateinit var resetDateFilterButton: Button
    private lateinit var deleteMatchingButton: Button
    private lateinit var exportMatchingButton: Button

    private var allLogs: List<LogEntry> = emptyList()
    private var currentFilter = LogFilter()
    private var filteredLogs: List<LogEntry> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_viewer)

        bindViews()
        setupRecyclerView()
        setupSearch()
        setupLevelFilters()
        setupDateFilter()
        setupActions()
    }

    override fun onResume() {
        super.onResume()
        loadLogs()
    }

    /**
     * Finds and stores references to the console views.
     */
    private fun bindViews() {
        logsRecyclerView = findViewById(R.id.logsRecyclerView)
        emptyTextView = findViewById(R.id.emptyTextView)
        statsTextView = findViewById(R.id.statsTextView)
        searchEditText = findViewById(R.id.searchEditText)

        infoCheckBox = findViewById(R.id.infoCheckBox)
        debugCheckBox = findViewById(R.id.debugCheckBox)
        warningCheckBox = findViewById(R.id.warningCheckBox)
        errorCheckBox = findViewById(R.id.errorCheckBox)

        dateFilterButton = findViewById(R.id.dateFilterButton)
        resetDateFilterButton = findViewById(R.id.resetDateFilterButton)
        deleteMatchingButton = findViewById(R.id.deleteMatchingButton)
        exportMatchingButton = findViewById(R.id.exportMatchingButton)
    }

    /**
     * Configures the log list.
     */
    private fun setupRecyclerView() {
        logsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    /**
     * Configures destructive console actions.
     */
    private fun setupActions() {
        findViewById<Button>(R.id.clearConsoleButton).setOnClickListener {
            showClearConfirmation()
        }

        deleteMatchingButton.setOnClickListener {
            showDeleteMatchingConfirmation()
        }

        exportMatchingButton.setOnClickListener {
            exportMatchingLogs()
        }
    }

    /**
     * Loads all stored logs without blocking the UI thread.
     */
    private fun loadLogs() {
        lifecycleScope.launch {
            allLogs = LoggerBuddy.getLogs()
            applyCurrentFilter()
        }
    }

    /**
     * Updates the search query while the developer types.
     */
    private fun setupSearch() {
        searchEditText.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    text: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // No action needed.
                }

                override fun onTextChanged(
                    text: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    currentFilter = currentFilter.copy(
                        searchQuery = text?.toString().orEmpty()
                    )

                    applyCurrentFilter()
                }

                override fun afterTextChanged(editable: Editable?) {
                    // No action needed.
                }
            }
        )
    }

    /**
     * Updates the selected levels whenever a checkbox changes.
     */
    private fun setupLevelFilters() {
        val listener = {
            updateSelectedLevels()
        }

        infoCheckBox.setOnCheckedChangeListener { _, _ ->
            listener()
        }

        debugCheckBox.setOnCheckedChangeListener { _, _ ->
            listener()
        }

        warningCheckBox.setOnCheckedChangeListener { _, _ ->
            listener()
        }

        errorCheckBox.setOnCheckedChangeListener { _, _ ->
            listener()
        }
    }

    /**
     * Builds a set containing every currently selected level.
     */
    private fun updateSelectedLevels() {
        val selectedLevels = buildSet {
            if (infoCheckBox.isChecked) {
                add(LogLevel.INFO)
            }

            if (debugCheckBox.isChecked) {
                add(LogLevel.DEBUG)
            }

            if (warningCheckBox.isChecked) {
                add(LogLevel.WARNING)
            }

            if (errorCheckBox.isChecked) {
                add(LogLevel.ERROR)
            }
        }

        currentFilter = currentFilter.copy(
            levels = selectedLevels
        )

        applyCurrentFilter()
    }

    /**
     * Applies search, level, and future date filters.
     */
    private fun applyCurrentFilter() {
        filteredLogs = LogFilterEngine.filter(
            logs = allLogs,
            filter = currentFilter
        )

        updateStatistics(filteredLogs)
        updateLogList(filteredLogs)
        updateMatchingActionButtons()
    }

    /**
     * Updates actions that operate on currently displayed logs.
     */
    private fun updateMatchingActionButtons() {
        val hasMatchingLogs = filteredLogs.isNotEmpty()

        deleteMatchingButton.isEnabled = hasMatchingLogs
        exportMatchingButton.isEnabled = hasMatchingLogs

        val buttonAlpha =
            if (hasMatchingLogs) {
                1f
            } else {
                0.5f
            }

        deleteMatchingButton.alpha = buttonAlpha
        exportMatchingButton.alpha = buttonAlpha

        deleteMatchingButton.text =
            if (hasMatchingLogs) {
                "Delete shown (${filteredLogs.size})"
            } else {
                "Delete shown"
            }

        exportMatchingButton.text =
            if (hasMatchingLogs) {
                "Export shown (${filteredLogs.size})"
            } else {
                "Export shown"
            }
    }

    /**
     * Confirms deletion of every log matching the active filters.
     */
    private fun showDeleteMatchingConfirmation() {
        if (filteredLogs.isEmpty()) {
            return
        }

        val matchingCount = filteredLogs.size
        val description = buildActiveFilterDescription()

        AlertDialog.Builder(this)
            .setTitle("Delete $matchingCount matching logs?")
            .setMessage(
                "This will permanently delete all logs currently displayed.\n\n" +
                        "Active filters:\n$description\n\n" +
                        "This action cannot be undone."
            )
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Delete $matchingCount") { dialog, _ ->
                deleteMatchingLogs()
                dialog.dismiss()
            }
            .show()
    }

    /**
     * Deletes every currently displayed log and refreshes the console.
     */
    private fun deleteMatchingLogs() {
        val idsToDelete = filteredLogs.map { log ->
            log.id
        }

        lifecycleScope.launch {
            val deletedCount = LoggerBuddy.deleteLogs(idsToDelete)

            Toast.makeText(
                this@LogViewerActivity,
                "$deletedCount logs deleted.",
                Toast.LENGTH_SHORT
            ).show()

            loadLogs()
        }
    }

    /**
     * Creates a readable summary of the active filters.
     */
    private fun buildActiveFilterDescription(): String {
        val selectedLevelText =
            if (currentFilter.levels.size == LogLevel.entries.size) {
                "Levels: All"
            } else if (currentFilter.levels.isEmpty()) {
                "Levels: None"
            } else {
                val levelNames = currentFilter.levels
                    .sortedBy { it.priority }
                    .joinToString(", ") { level ->
                        level.name
                    }

                "Levels: $levelNames"
            }

        val dateText = when {
            currentFilter.fromTimestamp == null &&
                    currentFilter.toTimestamp == null -> {
                "Date: All dates"
            }

            else -> {
                val fromText = currentFilter.fromTimestamp?.let {
                    dateDisplayFormatter.format(Date(it))
                } ?: "No start date"

                val toText = currentFilter.toTimestamp?.let {
                    dateDisplayFormatter.format(Date(it))
                } ?: "No end date"

                "Date: $fromText – $toText"
            }
        }

        val normalizedSearch = currentFilter.searchQuery.trim()

        val searchText =
            if (normalizedSearch.isEmpty()) {
                "Search: None"
            } else {
                "Search: \"$normalizedSearch\""
            }

        return buildString {
            appendLine(selectedLevelText)
            appendLine(dateText)
            append(searchText)
        }
    }


    /**
     * Updates information about matching and total logs.
     */
    private fun updateStatistics(filteredLogs: List<LogEntry>) {
        val infoCount = filteredLogs.count {
            it.level == LogLevel.INFO
        }

        val debugCount = filteredLogs.count {
            it.level == LogLevel.DEBUG
        }

        val warningCount = filteredLogs.count {
            it.level == LogLevel.WARNING
        }

        val errorCount = filteredLogs.count {
            it.level == LogLevel.ERROR
        }

        statsTextView.text =
            "${filteredLogs.size} matching • ${allLogs.size} total\n" +
                    "$infoCount INFO • " +
                    "$debugCount DEBUG • " +
                    "$warningCount WARN • " +
                    "$errorCount ERROR"
    }

    /**
     * Displays matching logs or the appropriate empty state.
     */
    private fun updateLogList(filteredLogs: List<LogEntry>) {
//        logsRecyclerView.adapter = LogAdapter(filteredLogs)
        logsRecyclerView.adapter = LogAdapter(
            logs = filteredLogs,
            onLogClicked = { log ->
                openLogDetails(log)
            }
        )

        if (filteredLogs.isEmpty()) {
            logsRecyclerView.visibility = RecyclerView.GONE
            emptyTextView.visibility = TextView.VISIBLE

            emptyTextView.text =
                if (allLogs.isEmpty()) {
                    "📄\n\nNo logs captured yet.\nAdd some logs from the demo app."
                } else {
                    "🔍\n\nNo logs match the current search, level, and date filters."
                }

            return
        }

        emptyTextView.visibility = TextView.GONE
        logsRecyclerView.visibility = RecyclerView.VISIBLE
    }

    /**
     * Opens the complete details of the selected log.
     */
    private fun openLogDetails(log: LogEntry) {
        val intent = Intent(
            this,
            LogDetailsActivity::class.java
        ).apply {
            putExtra(
                LogDetailsActivity.EXTRA_LOG_ID,
                log.id
            )

            putExtra(
                LogDetailsActivity.EXTRA_TIMESTAMP,
                log.timestamp
            )

            putExtra(
                LogDetailsActivity.EXTRA_LEVEL,
                log.level.name
            )

            putExtra(
                LogDetailsActivity.EXTRA_TAG,
                log.tag
            )

            putExtra(
                LogDetailsActivity.EXTRA_MESSAGE,
                log.message
            )
        }

        startActivity(intent)
    }

    /**
     * Shows confirmation before permanently deleting all stored logs.
     */
    private fun showClearConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Clear all logs?")
            .setMessage(
                "This will permanently delete all ${allLogs.size} stored logs.\n\n" +
                        "This action cannot be undone."
            )
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Clear all") { dialog, _ ->
                lifecycleScope.launch {
                    LoggerBuddy.clearLogs()
                    allLogs = emptyList()
                    applyCurrentFilter()
                }

                dialog.dismiss()
            }
            .show()
    }

    private val dateDisplayFormatter = SimpleDateFormat(
        "dd MMM yyyy",
        Locale.getDefault()
    )

    /**
     * Configures the date-filter controls.
     */
    private fun setupDateFilter() {
        dateFilterButton.setOnClickListener {
            showDateFilterMenu()
        }

        resetDateFilterButton.setOnClickListener {
            clearDateFilter()
        }
    }

    /**
     * Displays the available date-filter options.
     */
    private fun showDateFilterMenu() {
        val popupMenu = PopupMenu(this, dateFilterButton)

        popupMenu.menu.add("All dates")
        popupMenu.menu.add("Today")
        popupMenu.menu.add("Last 7 days")
        popupMenu.menu.add("Custom range")

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.title.toString()) {
                "All dates" -> {
                    clearDateFilter()
                    true
                }

                "Today" -> {
                    applyTodayFilter()
                    true
                }

                "Last 7 days" -> {
                    applyLastSevenDaysFilter()
                    true
                }

                "Custom range" -> {
                    showCustomStartDatePicker()
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }

    /**
     * Displays only logs created during the current calendar day.
     */
    private fun applyTodayFilter() {
        val startOfToday = startOfDay(System.currentTimeMillis())
        val endOfToday = endOfDay(System.currentTimeMillis())

        currentFilter = currentFilter.copy(
            fromTimestamp = startOfToday,
            toTimestamp = endOfToday
        )

        dateFilterButton.text = "Today"
        resetDateFilterButton.visibility = View.VISIBLE

        applyCurrentFilter()
    }

    /**
     * Displays logs from today and the previous six calendar days.
     */
    private fun applyLastSevenDaysFilter() {
        val calendar = Calendar.getInstance()

        calendar.add(Calendar.DAY_OF_YEAR, -6)

        val startTimestamp = startOfDay(calendar.timeInMillis)
        val endTimestamp = endOfDay(System.currentTimeMillis())

        currentFilter = currentFilter.copy(
            fromTimestamp = startTimestamp,
            toTimestamp = endTimestamp
        )

        dateFilterButton.text = "Last 7 days"
        resetDateFilterButton.visibility = View.VISIBLE

        applyCurrentFilter()
    }

    /**
     * Opens the first picker used to choose the custom range start date.
     */
    private fun showCustomStartDatePicker() {
        showDatePicker(
            title = "Select start date",
            initialTimestamp = currentFilter.fromTimestamp
                ?: System.currentTimeMillis()
        ) { selectedStartTimestamp ->

            showCustomEndDatePicker(
                selectedStartTimestamp = selectedStartTimestamp
            )
        }
    }

    /**
     * Opens the second picker used to choose the custom range end date.
     */
    private fun showCustomEndDatePicker(
        selectedStartTimestamp: Long
    ) {
        showDatePicker(
            title = "Select end date",
            initialTimestamp = currentFilter.toTimestamp
                ?: selectedStartTimestamp
        ) { selectedEndTimestamp ->

            val rangeStart = startOfDay(selectedStartTimestamp)
            val rangeEnd = endOfDay(selectedEndTimestamp)

            if (rangeStart > rangeEnd) {
                showInvalidDateRangeDialog()
                return@showDatePicker
            }

            currentFilter = currentFilter.copy(
                fromTimestamp = rangeStart,
                toTimestamp = rangeEnd
            )

            val startText = dateDisplayFormatter.format(Date(rangeStart))
            val endText = dateDisplayFormatter.format(Date(rangeEnd))

            dateFilterButton.text = "$startText – $endText"
            resetDateFilterButton.visibility = View.VISIBLE

            applyCurrentFilter()
        }
    }

    /**
     * Opens an Android date picker and returns the selected date.
     */
    private fun showDatePicker(
        title: String,
        initialTimestamp: Long,
        onDateSelected: (Long) -> Unit
    ) {
        val initialCalendar = Calendar.getInstance().apply {
            timeInMillis = initialTimestamp
        }

        val dialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }

                onDateSelected(selectedCalendar.timeInMillis)
            },
            initialCalendar.get(Calendar.YEAR),
            initialCalendar.get(Calendar.MONTH),
            initialCalendar.get(Calendar.DAY_OF_MONTH)
        )

        dialog.setTitle(title)
        dialog.show()
    }

    /**
     * Removes the active date filter.
     */
    private fun clearDateFilter() {
        currentFilter = currentFilter.copy(
            fromTimestamp = null,
            toTimestamp = null
        )

        dateFilterButton.text = "All dates"
        resetDateFilterButton.visibility = View.GONE

        applyCurrentFilter()
    }

    /**
     * Returns the first millisecond of the selected local calendar day.
     */
    private fun startOfDay(timestamp: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = timestamp

            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    /**
     * Returns the final millisecond of the selected local calendar day.
     */
    private fun endOfDay(timestamp: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = timestamp

            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis
    }

    /**
     * Explains why the selected custom range could not be applied.
     */
    private fun showInvalidDateRangeDialog() {
        AlertDialog.Builder(this)
            .setTitle("Invalid date range")
            .setMessage(
                "The end date cannot be earlier than the start date."
            )
            .setPositiveButton("OK", null)
            .show()
    }

    /**
     * Creates and shares a JSON export containing the currently displayed logs.
     */
    private fun exportMatchingLogs() {
        if (filteredLogs.isEmpty()) {
            return
        }

        exportMatchingButton.isEnabled = false
        exportMatchingButton.text = "Exporting..."

        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                LoggerBuddy.createJsonExport(
                    context = this@LogViewerActivity,
                    logs = filteredLogs,
                    filter = LogExportFilter.from(currentFilter)
                )
            }

            updateMatchingActionButtons()

            when (result) {
                is ExportResult.Success -> {
                    shareExportFile(result.file)
                }

                is ExportResult.Failure -> {
                    Toast.makeText(
                        this@LogViewerActivity,
                        result.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    /**
     * Opens Android's share sheet for a generated JSON export.
     */
    private fun shareExportFile(file: File) {
        val authority =
            "${applicationContext.packageName}.loggerbuddy.fileprovider"

        val contentUri = FileProvider.getUriForFile(
            this,
            authority,
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/json"

            putExtra(
                Intent.EXTRA_STREAM,
                contentUri
            )

            putExtra(
                Intent.EXTRA_SUBJECT,
                "LoggerBuddy log export"
            )

            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(
            shareIntent,
            "Share LoggerBuddy export"
        )

        try {
            startActivity(chooser)
        } catch (exception: Exception) {
            Toast.makeText(
                this,
                "No application is available to share the export.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}