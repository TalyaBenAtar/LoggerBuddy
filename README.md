# 📱 LoggerBuddy

[![](https://jitpack.io/v/TalyaBenAtar/LoggerBuddy.svg)](https://jitpack.io/#TalyaBenAtar/LoggerBuddy)
![Platform](https://img.shields.io/badge/Platform-Android-green)
![Language](https://img.shields.io/badge/Kotlin-2.0-blue)
![API](https://img.shields.io/badge/API-23+-brightgreen)
![License](https://img.shields.io/badge/License-MIT-yellow)

A lightweight yet powerful Android logging library that enables developers to collect, inspect, filter, export and analyze application logs directly from inside their app—without relying on Logcat or Android Studio.

LoggerBuddy was built to simplify debugging during development, QA, beta testing and production by providing a complete in-app logging solution together with optional remote dashboard support.

---

# 📚 Table of Contents

- [✨ Why LoggerBuddy?](#-why-loggerbuddy)
- [🎯 Project Goals](#-project-goals)
- [✨ Features](#-features)
- [🏗 Architecture](#-architecture)
- [📸 LoggerBuddy Console Screenshots](#-loggerbuddy-console-screenshots)
- [📱 Demo Application Screenshots](#-demo-application-screenshots)
- [🎥 Demo Video](#-demo-video)
- [🚀 Installation](#-installation)
- [📋 Requirements](#-requirements)
- [⚡ Quick Start](#-quick-start)
- [⚙ Configuration](#-configuration)
- [📖 Core Capabilities](#-core-capabilities)
- [📂 Repository Structure](#-repository-structure)
- [🛠 Usage](#-usage)
- [🔍 Searching & Filtering](#-searching--filtering)
- [📊 Statistics](#-statistics)
- [📄 Log Details](#-log-details)
- [📋 Copy to Clipboard](#-copy-to-clipboard)
- [🗑 Log Management](#-log-management)
- [📤 Exporting Logs](#-exporting-logs)
- [📦 Exported JSON](#-exported-json)
- [🌐 Remote Dashboard Support](#-remote-dashboard-support)
- [📖 Public API](#-public-api)
- [📱 Demo Application](#-demo-application)
- [🛡 Edge Cases](#-edge-cases)
- [📌 Design Decisions](#-design-decisions)
- [💡 Developer Capabilities](#-developer-capabilities)
- [⚠ Limitations](#-limitations)
- [📋 Developer Constraints](#-developer-constraints)
- [🚀 Performance Considerations](#-performance-considerations)
- [🧰 Technologies Used](#-technologies-used)
- [📚 Third-Party Libraries](#-third-party-libraries)
- [📄 License](#-license)
- [👩‍💻 Author](#-author)

---

# ✨ Why LoggerBuddy?

Android developers often rely on **Logcat** while debugging their applications.

Although Logcat is extremely useful during development, it has several limitations:

- Testers cannot access it.
- Clients cannot send meaningful debugging information.
- Production crashes are difficult to investigate.
- Logs disappear when the application is closed.
- Searching through thousands of logs is inconvenient.
- Sharing logs with a development team requires additional work.

LoggerBuddy solves these problems by embedding a complete logging system directly inside the application.

With only a few lines of code developers can:

- Record logs
- Monitor crashes
- Search and filter logs
- Export logs as JSON
- Share logs
- Save logs to the device
- Analyze logs using an optional dashboard

All while keeping the integration simple and lightweight.

---

# 🎯 Project Goals

LoggerBuddy was designed around the following principles.

## ⚡ Simple Integration

Logging should require as little code as possible.

Developers should be able to initialize the library once and immediately start writing logs.

---

## 💾 Persistent Logging

Logs should survive application restarts.

Instead of temporary Logcat output, LoggerBuddy stores logs locally using Room.

---

## 🔍 Easy Debugging

Developers should be able to quickly locate important information through searching, filtering and detailed log inspection.

---

## 🪲 Automatic Crash Collection

Unexpected application crashes should automatically be stored as logs, including stack traces, making post-crash debugging significantly easier.

---

## 📤 Portable Log Export

Collected logs should be exportable as structured JSON files that can be:

- saved locally
- shared
- archived
- uploaded
- visualized externally

---

## 📊 External Visualization

LoggerBuddy keeps ownership of the data in the developer's hands.

The library exports structured JSON while allowing developers to visualize their own data using any dashboard solution they prefer.

An example dashboard implementation is included inside the repository documentation.

---

# ✨ Features

## 📝 Logging

- Custom log messages
- Automatic caller detection
- Custom tags
- INFO logs
- WARNING logs
- ERROR logs
- DEBUG logs
- Exception logging
- Automatic crash logging
- Full stack trace support
- Date and time recorded for every log

---

## 📱 Built-in Console

- Beautiful Material Design interface
- Search bar
- Filter by log level
- Filter by date
- Cross-filtering (search + date + level together)
- View complete log details
- View complete stack traces
- Copy logs to clipboard
- Live statistics

---

## 🗂 Log Management

- Delete a single log
- Delete all logs
- Delete only filtered logs
- Automatic storage cleanup
- Configurable maximum stored logs

---

## 📤 Export

- Export all logs
- Export only filtered logs
- JSON export
- Save directly to device
- Share using Android Share Sheet

---

## 📱 Metadata

Every exported file automatically includes:

- Device manufacturer
- Device model
- Android version
- Application version
- Export date and time
- Applied filters
- Export type
- Complete log collection

---

## 🌐 Dashboard Support

LoggerBuddy includes compatibility for remote dashboard integrations.

Developers can build their own dashboard using the exported JSON format or connect the library to compatible remote services.

An example implementation using **Retool** is documented separately inside:

```
docs/remote-dashboard/
```

---

# 🏗 Architecture

```
                 Developer

                     │

                     ▼

             LoggerBuddy API

                     │

      ┌──────────────┼──────────────┐

      ▼              ▼              ▼

 Crash Monitor    Room Storage   Configuration

      │              │

      │              ▼

      │        Log Viewer UI

      │              │

      └──────► Export Engine

                     │

           Save / Share JSON

                     │

                     ▼

          Optional Dashboard
```

---

# 📸 LoggerBuddy Console Screenshots

| Console | Search | Filters | Export |
|:-------:|:------:|:-------:|:-----------:|
| <img src="https://github.com/user-attachments/assets/2bbc1e3b-da50-4815-8622-44ff19047e3b" width="180"/> | <img src="https://github.com/user-attachments/assets/ef9b81f4-3662-46f8-b4ca-1d470e7b1b38" width="180"/> | <img  src="https://github.com/user-attachments/assets/d5a316f6-9722-45c3-9ab8-d2403d16ec52" width="180"/> | <img src="https://github.com/user-attachments/assets/1f4ae9e6-7c07-4d75-a5ae-396ccc0966db" width="180"/> | 

| Details | Details | Delete | Search |
|:------:|:-----:|:----------:|:-----------:|
| <img src="https://github.com/user-attachments/assets/2d69422d-2f81-45f7-807a-fb07d05f0b7d" width="180"/> | <img src="https://github.com/user-attachments/assets/b75d59ae-3cb0-4484-871d-dab8e494c877" width="180"/> | <img  src="https://github.com/user-attachments/assets/84599935-1213-420a-bdfb-25e0aab7156b" width="180"/>  | <img src="https://github.com/user-attachments/assets/9ae9d902-802c-4624-9073-afa5a97c2a68" width="180"/> | <img  src="https://github.com/user-attachments/assets/9575df39-a73c-4be4-a66c-8922d0e6f3cc" width="180"/> |

---

# 📱 Demo Application Screenshots

The repository includes a demo application showcasing how LoggerBuddy can be integrated into an Android project.

| Automatic Logs | Manual Logs | Manual Logs Example |
|:-------:|:----------:|:-------------:|
| <img src="https://github.com/user-attachments/assets/bdb3de90-217c-408b-911d-75db5c059545" width="180"/> | <img src="https://github.com/user-attachments/assets/9a001632-55db-4ccd-8c42-eaf570938925" width="180"/> | <img  src="https://github.com/user-attachments/assets/596484d3-571d-451a-b5d7-8df4634f45fe" width="180"/> |

---

# 🎥 Demo Video

### LoggerBuddy in Action

> https://drive.google.com/file/d/190vj8Ij9uU6-ChGSt2DhB1JtR_IC-zdr/view?usp=sharing

---

# 🚀 Installation

LoggerBuddy is available through **JitPack**.

## Step 1

Add JitPack to your project.

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}
```

## Step 2

Add the dependency.

```kotlin
dependencies {
    implementation("com.github.TalyaBenAtar:LoggerBuddy:2.0.0")
}
```

---

# 📋 Requirements

- Android API 23+
- Kotlin
- AndroidX

---

# ⚡ Quick Start

Initialize LoggerBuddy once.

```kotlin
LoggerBuddy.initialize(this)
```

Record logs.

```kotlin
LoggerBuddy.info("Application started")

LoggerBuddy.warning("Battery is low")

LoggerBuddy.error("Login failed")

LoggerBuddy.debug("Counter = $counter")
```

Open the built-in console.

```kotlin
LoggerBuddy.showConsole(this)
```

That's it.

LoggerBuddy will automatically store logs inside its local database.

---

# ⚙ Configuration

LoggerBuddy can be customized using `LoggerBuddyConfig`.

Example:

```kotlin
LoggerBuddy.initialize(
    context = this,
    config = LoggerBuddyConfig(

        enableCrashCatcher = true,

        automaticCleanup = true,

        maxStoredLogs = 5000,

        enableDashboardUpload = false
    )
)
```

## Available Configuration Options

| Option | Description |
|---------|-------------|
| `enableCrashCatcher` | Enables automatic crash logging. |
| `automaticCleanup` | Automatically removes old logs when storage reaches the configured limit. |
| `maxStoredLogs` | Maximum number of logs stored locally before cleanup begins. |
| `enableDashboardUpload` | Enables compatibility with remote dashboard integrations. |

---

# 📖 Core Capabilities

LoggerBuddy provides a complete logging workflow.

✔ Log application events

✔ Automatically capture crashes

✔ Persist logs locally

✔ Search logs

✔ Filter logs

✔ Export logs

✔ Share logs

✔ Save logs

✔ Inspect detailed stack traces

✔ Analyze exported data using external dashboards

---

# 📂 Repository Structure

```
LoggerBuddy

│

├── loggerbuddy/
│   ├── data/
│   ├── export/
│   ├── filter/
│   ├── remote/
│   ├── ui/
│   └── LoggerBuddy.kt
│
├── app/
│   └── Demo Application
│
├── docs/
│   └── remote-dashboard/
│
├── LICENSE
└── README.md
```

---

# 🛠️ Usage

<details>

<summary><strong>View Usage Examples</strong></summary>

<br>

## Initialize LoggerBuddy

Initialize the library once, preferably inside your `Application` class or before logging any events.

```kotlin
LoggerBuddy.initialize(this)
```

---

## Create an INFO Log

```kotlin
LoggerBuddy.info("Application started")
```

---

## Create a WARNING Log

```kotlin
LoggerBuddy.warning("Battery level is low")
```

---

## Create an ERROR Log

```kotlin
LoggerBuddy.error("Login failed")
```

---

## Create a DEBUG Log

```kotlin
LoggerBuddy.debug("Current value = $value")
```

---

## Create a Custom Log

```kotlin
LoggerBuddy.log(
    message = "User clicked Login",
    tag = "LoginActivity",
    level = LogLevel.INFO
)
```

If no tag is supplied, LoggerBuddy automatically detects the caller class.

---

## Log Exceptions

```kotlin
try {

    ...

}
catch(exception: Exception){

    LoggerBuddy.exception(exception)

}
```

The complete stack trace is stored together with the exception message.

---

## Automatic Crash Logging

When crash monitoring is enabled, uncaught exceptions are automatically captured before the application terminates.

Every crash is saved as an **ERROR** log and includes:

- Exception type
- Exception message
- Complete stack trace
- Date
- Time

No additional code is required once crash monitoring is enabled.

---

## Open the Console

```kotlin
LoggerBuddy.showConsole(this)
```

The built-in console provides immediate access to all stored logs.

</details> 


---

# 🔍 Searching & Filtering

LoggerBuddy allows developers to quickly locate relevant logs without scrolling through large log collections.

Available filters include:

- Search by text
- Filter by log level
- Filter by date

Filters can also be combined.

For example:

```
ERROR

+

Today

+

"Login"
```

returns only today's ERROR logs containing the word **Login**.

The search results automatically update while filters are applied.

---

# 📊 Statistics

LoggerBuddy continuously displays useful statistics about the current log collection.

Statistics include:

- Total number of logs
- Number of INFO logs
- Number of WARNING logs
- Number of ERROR logs
- Number of DEBUG logs

When filters are applied, the statistics automatically update to reflect only the visible logs.

---

# 📄 Log Details

Selecting any log opens a dedicated details screen.

The details page displays:

- Date
- Time
- Level
- Tag
- Full message
- Full stack trace (when available)

Long messages remain fully readable without being truncated.

---

# 📋 Copy to Clipboard

Every log can be copied directly to the clipboard.

This makes it easy to:

- send logs to teammates
- create bug reports
- paste logs into GitHub Issues
- attach logs to support tickets

---

# 🗑️ Log Management

LoggerBuddy provides several ways to manage stored logs.

## Delete a Single Log

Remove an individual log entry.

---

## Delete Filtered Logs

Delete only the logs currently displayed after filtering.

This is especially useful when cleaning up:

- old logs
- DEBUG logs
- specific dates
- search results

without affecting the remaining database.

---

## Delete All Logs

Completely clears the local LoggerBuddy database.

---

## Automatic Cleanup

When enabled through `LoggerBuddyConfig`, LoggerBuddy automatically removes older logs after reaching the configured storage limit.

This prevents unlimited database growth while preserving recent information.

---

# 📤 Exporting Logs

LoggerBuddy exports logs using the JSON format.

Developers may choose between:

- Export all logs
- Export only filtered logs

The exported file can then be:

- Saved to the device
- Shared using Android's Share Sheet
- Imported into external visualization tools
- Archived for later analysis

---

# 📦 Exported JSON

Every exported file contains both log data and useful metadata.

## Device Information

- Manufacturer
- Device model
- Android version

---

## Application Information

- Application version

---

## Export Information

- Export date
- Export time
- Export type
- Active filters

---

## Log Collection

Each log contains:

- Date
- Time
- Level
- Tag
- Message
- Stack trace (when available)

---

# 🌐 Remote Dashboard Support

LoggerBuddy was designed to work with optional external dashboards.

The library itself does **not** host or manage any remote dashboard.

Instead, it provides:

- structured JSON exports
- Retrofit compatibility
- configurable remote upload support

This approach allows every developer to remain in complete control of their own data.

An example implementation using **Retool** is documented separately inside:

```
docs/remote-dashboard/
```

The dashboard documentation includes:

- setup guide
- screenshots
- dashboard export
- customization instructions
- architecture explanation

---

# ⚙ Configuration Reference

| Configuration | Description |
|---------------|-------------|
| `enableCrashCatcher` | Enables automatic crash monitoring. |
| `automaticCleanup` | Automatically removes old logs when storage reaches the configured limit. |
| `maxStoredLogs` | Maximum number of logs stored locally. |
| `enableDashboardUpload` | Enables remote dashboard compatibility. |

---

# 📖 Public API

<details>

<summary><strong>View Public API</strong></summary>

<br>

| Function | Description |
|-----------|-------------|
| `initialize()` | Initializes LoggerBuddy. |
| `log()` | Creates a custom log. |
| `info()` | Creates an INFO log. |
| `warning()` | Creates a WARNING log. |
| `error()` | Creates an ERROR log. |
| `debug()` | Creates a DEBUG log. |
| `exception()` | Logs an exception and its stack trace. |
| `showConsole()` | Opens the built-in LoggerBuddy console. |
| `clearLogs()` | Deletes all stored logs. |
| `exportLogs()` | Exports logs as JSON. |

</details>

---

# 📱 Demo Application

The repository includes a complete demo application demonstrating every major LoggerBuddy feature.

The demo showcases:

- Manual logging
- Automatic logging
- Crash testing
- Searching
- Filtering
- Statistics
- Exporting logs
- Saving exported files
- Sharing exported files
- Configuration options
- Remote dashboard compatibility

The demo application can be used as a reference implementation when integrating LoggerBuddy into your own projects.

---

# 🛡 Edge Cases

LoggerBuddy was designed to handle common situations that frequently occur in real-world applications. The following table summarizes how these scenarios are handled.

| Edge Case | Behavior | Implementation |
|-----------|----------|----------------|
| Application crashes unexpectedly | Uncaught exceptions are automatically captured and stored as ERROR logs before application termination. | [Crash capture and synchronous storage](https://github.com/TalyaBenAtar/LoggerBuddy/blob/21043f13c6f50b319f0c3cd0c240f6c4f61b5b03/loggerbuddy/src/main/java/com/example/loggerbuddy/LoggerBuddy.kt#L392-L443) |
| Exception logging | Complete exception messages and stack traces are preserved for easier debugging. | [Exception message and stack trace formatting](https://github.com/TalyaBenAtar/LoggerBuddy/blob/21043f13c6f50b319f0c3cd0c240f6c4f61b5b03/loggerbuddy/src/main/java/com/example/loggerbuddy/LoggerBuddy.kt#L347-L373) |
| No custom tag provided | LoggerBuddy automatically detects the calling class and uses it as the log tag. | [Automatic caller-tag detection](https://github.com/TalyaBenAtar/LoggerBuddy/blob/21043f13c6f50b319f0c3cd0c240f6c4f61b5b03/loggerbuddy/src/main/java/com/example/loggerbuddy/LoggerBuddy.kt#L474) |
| Empty search results | The console displays an empty state instead of failing or showing invalid data. | [Empty console and search-result states](https://github.com/TalyaBenAtar/LoggerBuddy/blob/5c3ff4817098f65ed9a0e243d271ec877e3e0a22/loggerbuddy/src/main/java/com/example/loggerbuddy/ui/LogViewerActivity.kt#L432-L460) |
| Multiple active filters | Search, date and level filters are combined safely using cross-filtering. | [Search, level and date cross-filtering](https://github.com/TalyaBenAtar/LoggerBuddy/blob/5c3ff4817098f65ed9a0e243d271ec877e3e0a22/loggerbuddy/src/main/java/com/example/loggerbuddy/filter/LogFilterEngine.kt#L10-L31) |
| Export after filtering | Developers may export either the filtered logs or the complete database. | [Filtered-log JSON export](https://github.com/TalyaBenAtar/LoggerBuddy/blob/5c3ff4817098f65ed9a0e243d271ec877e3e0a22/loggerbuddy/src/main/java/com/example/loggerbuddy/ui/LogViewerActivity.kt#L759-L796) |
| Export with no logs | Exporting an empty log collection is rejected safely with a developer-readable failure result instead of creating an invalid file or throwing an uncaught exception. | [Safe handling of empty exports](https://github.com/TalyaBenAtar/LoggerBuddy/blob/5c3ff4817098f65ed9a0e243d271ec877e3e0a22/loggerbuddy/src/main/java/com/example/loggerbuddy/export/LogExporter.kt#L18-L73) |
| Very large log collections | Logs are displayed using RecyclerView view holders, while persistence is handled through Room and background storage operations. | [RecyclerView-backed log rendering](https://github.com/TalyaBenAtar/LoggerBuddy/blob/5c3ff4817098f65ed9a0e243d271ec877e3e0a22/loggerbuddy/src/main/java/com/example/loggerbuddy/ui/LogAdapter.kt#L17) |
| Maximum storage reached | Automatic cleanup removes older logs when enabled through configuration. | [Automatic maximum-storage cleanup](https://github.com/TalyaBenAtar/LoggerBuddy/blob/5c3ff4817098f65ed9a0e243d271ec877e3e0a22/loggerbuddy/src/main/java/com/example/loggerbuddy/data/LogStorage.kt#L108-L119) |
| Normal log without exception | Normal logs are stored and displayed without requiring exception data; stack-trace text is added only when a Throwable is explicitly logged or an uncaught crash is captured. | [Normal log creation without exception data](https://github.com/TalyaBenAtar/LoggerBuddy/blob/5c3ff4817098f65ed9a0e243d271ec877e3e0a22/loggerbuddy/src/main/java/com/example/loggerbuddy/LoggerBuddy.kt#L302-L338) |

> **Note:**  
> The placeholder links above should be replaced with GitHub **permalinks** pointing directly to the implementation of each feature. Using permalinks ensures the documentation always references the exact code version, even if files change in future commits.

---

# 📌 Design Decisions

Several architectural decisions were made to keep LoggerBuddy lightweight, reliable and easy to integrate.

## Local-First Storage

LoggerBuddy stores logs locally using Room instead of relying on remote services.

This provides:

- Fast access to logs
- Offline availability
- No external infrastructure required
- Complete developer ownership of collected data

---

## JSON Export

Rather than enforcing a specific cloud service or dashboard, LoggerBuddy exports structured JSON.

This approach allows developers to:

- archive logs
- share logs
- upload logs
- visualize logs
- integrate with existing systems

without changing the library itself.

---

## Optional Dashboard

LoggerBuddy does **not** require a dashboard.

The dashboard is an optional companion that demonstrates one possible way to visualize exported logs.

Developers remain free to use any technology that best fits their own workflow.

---

## Configurable Behavior

Features such as crash monitoring and automatic cleanup are configurable, allowing developers to adapt LoggerBuddy to projects of different sizes and requirements.

---

# 💡 Developer Capabilities

LoggerBuddy allows developers to:

- Monitor application behavior directly on the device
- Capture unexpected crashes automatically
- Search large collections of logs
- Filter logs using multiple criteria simultaneously
- Inspect complete exception stack traces
- Export logs for external analysis
- Save exported logs locally
- Share exported logs with testers or teammates
- Build custom dashboards using the exported JSON format

---

# ⚠ Limitations

LoggerBuddy intentionally focuses on local application logging.

The current version does **not** include:

- Built-in cloud storage
- Automatic synchronization between devices
- User authentication
- Multi-user log management
- Real-time remote monitoring
- Automatic crash reporting to third-party services

These capabilities can be implemented externally using the exported JSON files or the optional remote upload functionality.

---

# 📋 Developer Constraints

To ensure correct behavior, developers should keep the following constraints in mind:

- LoggerBuddy must be initialized before creating logs.
- Crash monitoring only captures **uncaught** exceptions.
- Exported JSON files are intended for debugging and should not contain sensitive user information.
- Automatic cleanup only runs when enabled through the configuration.
- Remote dashboard support is optional and requires additional setup.
- LoggerBuddy is currently supported only on Android.

---

# 🚀 Performance Considerations

LoggerBuddy was designed to minimize runtime overhead.

Performance optimizations include:

- Room database for efficient local storage
- Background database operations using Kotlin Coroutines
- RecyclerView virtualization for large log collections
- Filtering performed only on the displayed dataset
- Automatic cleanup to prevent unlimited database growth

For most applications, LoggerBuddy introduces negligible performance impact during normal use.

---

# 🧰 Technologies Used

## Android

- Kotlin
- Android SDK
- AndroidX

---

## Storage

- Room Persistence Library

---

## Networking

- Retrofit

---

## Concurrency

- Kotlin Coroutines

---

## User Interface

- RecyclerView
- Material Design Components
- ConstraintLayout

---

## Development Tools

- Android Studio
- Gradle
- GitHub

---

# 📚 Third-Party Libraries

| Library | Purpose |
|----------|---------|
| Room | Persistent local storage |
| Retrofit | Remote dashboard compatibility |
| Kotlin Coroutines | Background operations |
| RecyclerView | Displaying large log collections |
| Material Components | User interface |
| AndroidX | Android support libraries |

---

# 📄 License

LoggerBuddy is released under the **MIT License**.

See the [LICENSE](LICENSE) file for complete license information.

---

# 👩‍💻 Author

**Talya Ben Atar**

B.Sc. Computer Science

Afeka Academic College of Engineering

---

# ⭐ Support LoggerBuddy

If LoggerBuddy helped you or your team, consider giving the repository a ⭐ on GitHub.

Feedback, suggestions and contributions are always welcome.
