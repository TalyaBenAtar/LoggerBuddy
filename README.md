# 📱 LoggerBuddy
[![](https://jitpack.io/v/TalyaBenAtar/LoggerBuddy.svg)](https://jitpack.io/#TalyaBenAtar/LoggerBuddy)
![Platform](https://img.shields.io/badge/Platform-Android-green)
![Language](https://img.shields.io/badge/Kotlin-2.0-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

> A lightweight Android logging library that allows developers to log and view application events directly from inside their app.

LoggerBuddy was created as an Android library project for developers who want an easy way to record and inspect application logs without connecting Logcat or Android Studio. 
The library provides a simple API for creating logs and includes a built-in log viewer for reviewing them directly on the device.

---

# ✨ Features

- 📝 Save custom logs
- 🏷️ Automatic caller detection or custom tags
- 🚦 Multiple log levels
  - INFO
  - WARNING
  - ERROR
  - DEBUG
- 📅 Timestamp for every log
- 📱 Built-in log viewer activity
- 🗑️ Clear all logs
- 💾 Persistent storage using Room
- ⚡ Simple one-line logging API
- 📚 Fully documented public API

---

# ⚙️ Implementation Overview

LoggerBuddy stores every log inside a local Room database.

Whenever a log is created:

1. The API receives the log request.
2. A `LogEntry` object is created.
3. If no tag is supplied, LoggerBuddy automatically detects the caller class.
4. The log is stored in the local Room database.
5. The Log Viewer retrieves logs from the database and displays them.
6. Users can clear logs directly from the built-in console.

---

# 📸 Screenshots

| Log Viewer | Automatic Logs | Manual Logs |
|-------------|---------|----------|
| <img src="https://github.com/user-attachments/assets/f8402514-8b6d-4382-8dca-e7dc1e2c9582" width="220"/> | <img src="https://github.com/user-attachments/assets/bbb9d373-e5c6-4d22-a71a-d6a7e001faec" width="220"/> | <img src="https://github.com/user-attachments/assets/7435fd1a-b0f3-4b6e-9642-71b21d83ef45" width="220"/> |
| *LoggerBuddy Console* | *Automatic Logs* | *Manual Logs* |

---

# 🎥 Demo Video

### ▶️ Watch LoggerBuddy in action


https://github.com/user-attachments/assets/e611376a-45a2-4abc-9075-4f5e95c0c04d


---

# 🚀 Quick Start

Initialize LoggerBuddy once, log a message, and open the built-in console.

```kotlin
LoggerBuddy.initialize(this)

LoggerBuddy.info("Application started")

LoggerBuddy.showConsole(this)
```

---

# 📋 Requirements

- Android API 23+
- Kotlin
- AndroidX

---

# 📦 Installation

LoggerBuddy is available through **JitPack**.

### Step 1: Add JitPack

In `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### Step 2: Add LoggerBuddy

In your app-level `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.TalyaBenAtar:LoggerBuddy:1.0.0")
}
```

### Step 3: Initialize LoggerBuddy

Initialize LoggerBuddy once inside your `Application` or `MainActivity`.

```kotlin
LoggerBuddy.initialize(this)
```

---

# 🛠️ How To Use

## Save an Info Log

```kotlin
LoggerBuddy.log("Application started")
```

---

## Save a Warning

```kotlin
LoggerBuddy.warning("Low battery")
```

---

## Save an Error

```kotlin
LoggerBuddy.error("Login failed")
```

---

## Save a Debug Message

```kotlin
LoggerBuddy.debug("Current value = $value")
```

---

## Save a Custom Log

```kotlin
LoggerBuddy.log(
    message = "User clicked button",
    tag = "MainActivity",
    level = LogLevel.INFO
)
```

---

## Log an Exception

```kotlin
try {
    ...
}
catch(e: Exception){
    LoggerBuddy.exception(e)
}
```

---

## Open the Logger Console

```kotlin
LoggerBuddy.showConsole(this)
```

---

## Clear All Logs

```kotlin
LoggerBuddy.clearLogs()
```

---

# 📖 Public API

| Function | Description |
|-----------|-------------|
| `initialize()` | Initializes LoggerBuddy |
| `log()` | Saves a custom log |
| `info()` | Saves an INFO log |
| `warning()` | Saves a WARNING log |
| `error()` | Saves an ERROR log |
| `debug()` | Saves a DEBUG log |
| `exception()` | Saves an exception and stack trace |
| `showConsole()` | Opens the built-in log viewer |
| `clearLogs()` | Deletes all saved logs |

---

# 🚀 Planned Features (Final Project)

The following features will be added in the final version of LoggerBuddy in the coming month:

- 📅 Filter logs by date
- 🔍 Search logs
- 🎯 Filter logs by level
- 📄 Export logs
- 🪲 Automatic crash logging
- 📱 Device information logging
- 📦 App information logging
- 🗑️ Delete logs by type
- 🕒 Display date alongside time

Bonus: ☁️ Web dashboard

---

# 🧰 Technologies Used

### Android

- Kotlin
- Android SDK
- AndroidX

### UI

- RecyclerView
- CardView
- Material Design Components
- ConstraintLayout

### Concurrency

- Kotlin Coroutines

### Storage

- Room Persistence Library

### Development

- Android Studio
- Gradle
- GitHub

---

# 📚 Libraries

| Library | Purpose |
|----------|---------|
| Room | Local database |
| Kotlin Coroutines | Background operations |
| RecyclerView | Display logs |
| Material Components | UI Components |
| AndroidX | Android support libraries |

---

# 📄 License

This project is licensed under the **MIT License**.

See the [LICENSE](LICENSE) file for full details.


---


# 👩‍💻 Author

**Talya Ben Atar**

Computer Science Student

Afeka Academic College of Engineering

---

# ⭐ If you like LoggerBuddy...

Give the repository a ⭐ on GitHub!
