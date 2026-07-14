# 🤖 LoggerBuddy Dashboard AI Prompt

This prompt can be used when creating a new dashboard in retool  or another Large Language Model (LLM) to recreate, customize, or extend the example dashboard for LoggerBuddy.

The dashboard included in this repository is only an example implementation.

Developers should replace the sample data with their own LoggerBuddy JSON exports.

---

## Prompt

You are building a professional web dashboard for **LoggerBuddy**, an Android logging library.

The dashboard is intended for developers who export logs from their Android applications and want to inspect them on a desktop interface.

The dashboard must **NOT** generate or manage data on its own.

Assume developers will import their own exported LoggerBuddy JSON files.

The dashboard should work entirely with the imported JSON.

---

### JSON Structure

The exported JSON contains two main sections:

#### Metadata

The metadata contains information about the export itself, including:

- Device manufacturer
- Device model
- Android version
- Application version
- Export date
- Export time
- Export type (All Logs / Filtered Logs)
- Active filters used during export

---

#### Logs

Each log contains:

- Date
- Time
- Log level
- Tag
- Message
- Stack trace (optional)

---

### Dashboard Goals

The dashboard should make large collections of logs easy to inspect and analyze.

It should feel like a professional debugging tool.

---

### Required Features

Create a responsive dashboard that includes:

### Log Table

- Display all logs in a table
- Sort columns
- Search by text
- Filter by log level
- Filter by date
- Combine multiple filters simultaneously
- Scroll efficiently through large datasets

---

### Log Details

Selecting a log should display:

- Complete message
- Complete stack trace
- Date
- Time
- Level
- Tag

Messages should never be truncated.

---

### Statistics

Display summary cards showing:

- Total logs
- INFO logs
- WARNING logs
- ERROR logs
- DEBUG logs

When filters are applied, statistics must update automatically.

---

### Charts

Include visualizations such as:

- Pie chart showing log level distribution
- Line chart showing logs over time
- Bar chart showing logs grouped by tag

Charts should automatically react to filtering.

---

### Metadata Panel

Display export metadata including:

- Device model
- Manufacturer
- Android version
- App version
- Export date
- Export time
- Export type
- Active filters

---

### User Experience

The dashboard should:

- Have a clean modern interface
- Support light and dark themes if possible
- Be responsive
- Keep filtering fast even with large datasets
- Prioritize readability over excessive animations

---

### Constraints

Do NOT modify the LoggerBuddy JSON schema.

Do NOT require a backend server.

Do NOT upload user data automatically.

Assume all data is loaded locally by the developer.

---

### Customization

The dashboard should be easy to extend with additional charts, filters or analytics while remaining compatible with the existing LoggerBuddy JSON format.

Developers should be able to replace the sample export with their own LoggerBuddy export without changing the dashboard logic.

---

Generate clean, well-structured code and explain any assumptions that are made.
