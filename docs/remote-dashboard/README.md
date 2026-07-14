# 🌐 LoggerBuddy Remote Dashboard

The LoggerBuddy Remote Dashboard is an optional companion for the LoggerBuddy Android library.

It demonstrates one possible way to visualize exported logs outside the Android device.

The dashboard itself is **not part of LoggerBuddy**.

Instead, LoggerBuddy exports structured JSON files that can be imported into any visualization platform.

The example included in this repository was created using **Retool**, but developers are free to use any technology they prefer.

---

# 🎯 Purpose

The dashboard provides a convenient way to inspect large collections of logs.

Instead of scrolling through hundreds or thousands of logs on a mobile device, developers can visualize them using a desktop interface with searching, filtering and statistics.

The dashboard was designed to demonstrate what can be built using LoggerBuddy's exported JSON format.

---

# ⚠ Important

LoggerBuddy does **not** provide a hosted dashboard.

LoggerBuddy does **not** upload your logs.

LoggerBuddy does **not** collect developer data.

Every developer owns and manages their own exported log files.

This repository simply includes an example implementation.

---

# ✨ Dashboard Features

The example dashboard supports:

- Search
- Filter by level
- Filter by date
- Cross filtering
- View complete log messages
- View stack traces
- Statistics
- Charts
- Device metadata
- Application metadata

---

# 📸 Dashboard Screenshots

| Home |  
|:---:|
| <img width="1581" height="923" alt="dashboard_main_loggerbuddy" src="https://github.com/user-attachments/assets/3b54465d-1e78-436a-b9cc-b399bd9dd84e" /> |

| Filtering |
|:---:|
| <img width="1585" height="918" alt="dashboard_filtering_example_loggerbuddy" src="https://github.com/user-attachments/assets/133668ae-8b2d-4d8b-8c7b-af171b340245" /> |

| Crash Filtering |
|:---:|
| <img width="1587" height="925" alt="dashboard_crash_filtering_loggerbuddy" src="https://github.com/user-attachments/assets/f42cee67-1abb-4432-851b-011cc9a77c1b" /> |

| Log Details |
|:---:|
| <img width="1587" height="826" alt="dashboard_log_details_loggerbuddy" src="https://github.com/user-attachments/assets/f934536f-6ddc-4981-872b-3cd0026a28a2" /> | 

---

# 🎥 Dashboard Demo

> https://drive.google.com/file/d/1uY-49gfthPlT1iUIub_97PIDrsZpkA2r/view?usp=sharing

---

# 🏗 Dashboard Architecture

```

Android Application

↓

LoggerBuddy

↓

Export JSON

↓

Import JSON

↓

Retool Dashboard

↓

Search • Filter • Charts • Statistics

```

---

# 📦 JSON Structure

Every exported JSON file contains two sections.

## Metadata

```json
{
  "deviceManufacturer": "...",
  "deviceModel": "...",
  "androidVersion": "...",
  "appVersion": "...",
  "exportDate": "...",
  "exportTime": "...",
  "filters": { }
}
```

---

## Logs

```json
{
  "date": "...",
  "time": "...",
  "level": "...",
  "tag": "...",
  "message": "...",
  "stackTrace": "..."
}
```

---

# 🚀 Creating Your Own Dashboard

The included dashboard is only an example.

Developers may build their own dashboard using:

- Retool
- React
- Angular
- Vue
- ASP.NET
- Python
- Power BI
- Grafana
- Any custom solution

Only the exported JSON format is required.

---

# 🛠 Using the Example Dashboard

## Step 1

Export logs from LoggerBuddy.

Choose either:

- Export all logs

or

- Export filtered logs

---

## Step 2

Open your dashboard platform.

The example uses Retool.

---

## Step 3

Import the exported JSON.

Map the fields.

---

## Step 4

Build your tables, filters and charts.

---

# 🤖 AI Prompt

The repository includes an AI prompt that can recreate or customize the example dashboard.

See

```

dashboard-ai-prompt.md

```

---

# 📄 Dashboard Export

The repository also contains the exported dashboard configuration.

Import it directly into Retool.

```

dashboard-export.json

```

---

# 📌 Notes

The dashboard is completely optional.

LoggerBuddy works perfectly without it.

The purpose of the dashboard is to demonstrate how the exported JSON can be transformed into a powerful desktop debugging experience.

Developers are encouraged to customize or replace the example dashboard according to their own needs.
