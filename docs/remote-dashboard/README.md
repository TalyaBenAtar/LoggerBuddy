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

# Why Retool?

Retool was chosen for the example implementation because it allows developers to build dashboards quickly with minimal setup while remaining completely customizable.

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
        │
        ▼
   LoggerBuddy
        │
        ▼
   Export Logs
      (JSON)
        │
        ▼
 Import into Retool
        │
        ▼
 Dashboard
 ├── Search
 ├── Filters
 ├── Charts
 ├── Statistics
 └── Log Details

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
- Grafana
- Power BI
- React
- Python
- Any custom solution

Only the exported JSON format is required.

---

# 🤖 AI Prompt

The repository includes an AI prompt that can recreate or customize the example dashboard.

See

```

dashboard-retool-ai-prompt.md

```

---

# 📄 Dashboard Template

Contains the complete dashboard. Import it into Retool.

```
LoggerBuddy-Dashboard.zip

```

---

# 📄 Sample Export

```
loggerbuddy_sample-export.json

```

---

# 🚀 Importing the Example Dashboard

Follow either of these options to upload the `LoggerBuddy-Dashboard.zip` file into your own Retool workspace.

## Option 1: Using the New Retool App Builder (Easiest)
If you just clicked **Create** > **App** and are looking at the AI assistant panel on the left:
1. Locate the **file attachment icon** or drag-and-drop zone in the AI panel.
2. Drag and drop the downloaded `LoggerBuddy-Dashboard.zip` template file straight into the chat/upload window.
3. The Retool assistant will parse the configuration and instantly populate the dashboard for you.

---

## Option 2: Using the Classic Uploader
If you prefer to bypass the AI assistant and create a standalone clone of the dashboard:
1. Go to your Retool home dashboard.
2. Click the blue **Create** button in the top right.
3. Select **Classic app** from the dropdown menu.
4. In the configuration popup that opens, look for the option labeled **From JSON / ZIP** (usually at the bottom or top corner of the window).
5. Upload the `LoggerBuddy-Dashboard.zip` file, give your new app a name, and click **Create app**.

---

## ⚠️ **Note on Data Connectivity:** 
This template contains only the UI layout, front-end logic, and query structures. It does **not** connect to the original database or leak any private data. 
After importing, you will need to open the app editor, click on the queries, and re-link them to your own Retool data resources. You can use the included loggerbuddy_sample_export.json as sample data while building or testing your own dashboard. Once your dashboard is working, simply replace it with JSON exports generated by your own LoggerBuddy integration.

---

# 🔌 Reconnecting Your Data Resources

Because this template is shared as a privacy-safe template, it contains the UI structure and query text, but **cannot access the original databases**. To make the dashboard functional in your environment, follow these steps:

### Step 1: Open the Query Library / Bottom Panel
1. With your newly imported app open in the Retool Editor, look at the bottom half of the screen.
2. If the query panel is hidden, press `Ctrl + ~` (or `Cmd + ~` on Mac), or click the **bottom bar icon** in the lower panel to open it.

### Step 2: Update the Resource Selection
1. Look at the left sidebar of the bottom panel—this lists all the queries running behind the dashboard (e.g., `getSalesData`, `fetchUsers`).
2. Click on the first query in the list.
3. In the query configuration area, look for the **Resource** dropdown field at the very top.
4. It will likely show a red error or say *"Resource not found"*. Click the dropdown and select your own database or API connection (e.g., your PostgreSQL, MySQL, or REST API instance).

## Step 3: Match the Data Schema (If Needed)
* **Exact Match:** If your database uses the exact same table and column names as the original template, the query will work immediately.
* **Custom Match:** If your database has different column names, adjust the SQL or API parameters inside the query box to match your schema.

## Step 4: Save and Repeat
1. Click **Save** or **Save and Run** in the top right of the query panel to apply the change.
2. Repeat this process for any other queries listed in the bottom panel until all components populate with your live data.

---

# 💡 Final Notes

The Remote Dashboard is intended as an optional companion to LoggerBuddy.

It demonstrates one possible way to visualize exported logs outside the Android application, but developers are encouraged to adapt, extend or completely replace it according to their own needs.

LoggerBuddy itself remains lightweight and fully functional without the dashboard.
