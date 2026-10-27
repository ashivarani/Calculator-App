# 🧮 Calculator App – Android Application

## 📌 Project Description

**Calculator App** is an Android application developed using **Java** in **Android Studio**. The application provides basic arithmetic operations, real-time calculation previews, and a history feature to store and view previous calculations.

It uses the **exp4j library** for mathematical expression evaluation and **SQLite** for storing calculation history. The application also includes a Material Design-style interface and welcome notifications.

This project demonstrates Android application development, mathematical expression handling, database integration, and notification management using Java and XML.

## ✨ Features

- ➕ **Basic Arithmetic Operations:** Perform addition, subtraction, multiplication, and division.
- 🔢 **Percentage and Decimal Support:** Enter percentage and decimal calculations.
- ⚡ **Real-Time Calculation:** Display calculation previews as users enter expressions.
- 🧮 **Expression Evaluation:** Evaluate mathematical expressions using the exp4j library.
- ⌫ **Clear Functionality:** Remove the last entered character using the `C` button.
- 🔄 **All Clear:** Reset the current expression and result using `AC`.
- 💾 **Calculation History:** Store completed calculations in a local SQLite database.
- 📜 **View History:** Display previous calculations in a scrollable CardView layout.
- 🗑️ **Delete History:** Clear stored calculation records using the `Del` button.
- 🔔 **Welcome Notification:** Display a notification when the application launches, subject to notification permissions.
- 🎨 **User-Friendly Interface:** Uses XML layouts and Material Design components.

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java | Application development |
| Android Studio | Development environment |
| XML | User interface design |
| Android SDK | Android application development |
| SQLite | Calculation history storage |
| exp4j | Mathematical expression evaluation |
| Material Components | UI components |
| AndroidX | Android support libraries |
| Gradle | Build automation |

## 📂 Project Structure

```text
Calculator-App-master/
│
├── app/
│   ├── src/main/
│   │   ├── java/com/android/calculator/
│   │   │   ├── MainActivity.java
│   │   │   ├── ExpressionEvaluator.java
│   │   │   ├── DatabaseHelper.java
│   │   │   ├── DisplayActivity.java
│   │   │   ├── NotificationHelper.java
│   │   │   └── AppLogger.java
│   │   │
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── main_activity.xml
│   │   │   │   ├── buttons_layout.xml
│   │   │   │   ├── display_activity.xml
│   │   │   │   └── item_history_card.xml
│   │   │   │
│   │   │   └── menu/
│   │   │       └── menu.xml
│   │   │
│   │   └── AndroidManifest.xml
│   │
│   └── build.gradle
│
├── build.gradle
├── settings.gradle
└── README.md
```

### Main Components

**MainActivity.java**

Handles calculator initialization, button interactions, history navigation, and notification setup.

**ExpressionEvaluator.java**

Performs expression evaluation, updates real-time results, handles input controls, and saves completed calculations.

**DatabaseHelper.java**

Manages SQLite database operations, including inserting, retrieving, and deleting calculation history records.

**DisplayActivity.java**

Displays previous calculations in a scrollable list of cards.

**NotificationHelper.java**

Manages welcome notifications, notification channels, and runtime permission requests.

**AppLogger.java**

Provides logging functionality for debugging application events and errors.

## 🚀 Installation and Setup

### Prerequisites

- Android Studio
- Android SDK
- Android device or emulator
- Internet connection for initial Gradle dependency downloads

### Step 1: Download the Project

Download the project ZIP file from GitHub and extract it, or clone the repository using Git.

### Step 2: Open in Android Studio

1. Launch Android Studio.
2. Select **Open**.
3. Navigate to the extracted Calculator App project folder.
4. Wait for Gradle synchronization to complete.

### Step 3: Run the Application

1. Connect an Android device or launch an emulator.
2. Select the target device.
3. Click **Run** in Android Studio.
4. The calculator application launches.
5. Allow notification permission if prompted.

## 📱 How to Use

1. Open the **Calculator App**.
2. Enter numbers using the calculator buttons.
3. Select an arithmetic operator such as `+`, `-`, `×`, or `/`.
4. View the calculation preview displayed below the expression.
5. Press `=` to calculate the final result and save the calculation.
6. Use `C` to remove the last character or `AC` to clear the current calculation.
7. Tap the **History** icon to view previous calculations.
8. Use **Del** to clear all stored calculation history.

## ⚙️ How It Works

### 1. Mathematical Expression Evaluation

The application uses the **exp4j** library to evaluate mathematical expressions.

It supports basic arithmetic operations and operator precedence.

### 2. Real-Time Result Display

As users enter numbers and valid expressions, the application evaluates the current input and displays a preview of the calculated result.

### 3. Calculation History

Completed calculations are stored using SQLite through `DatabaseHelper.java`.

The database maintains calculation expressions and their results.

### 4. History Display

The history screen retrieves saved calculations from SQLite and displays them using Material-style cards.

Records are displayed with the newest calculations first.

### 5. Notification Management

When the application launches, it attempts to display a welcome notification.

The implementation supports notification channels on Android 8.0 and later and requests notification permission on Android 13 and later.

### 6. Logging

The application uses `AppLogger` to record events such as button interactions, calculation results, database operations, and errors.

## 📌 Project Information

| Detail | Description |
|---|---|
| Project Name | Calculator App |
| Application Type | Android Calculator |
| Programming Language | Java |
| IDE | Android Studio |
| User Interface | XML |
| Expression Library | exp4j |
| Database | SQLite |
| Minimum SDK | API 24 (Android 7.0) |
| Target SDK | API 34 |
| Compile SDK | API 36 |
| Application Version | 1.0 |

## ⚠️ Current Limitations

- The project is configured for Android 7.0 and above, but the `=` button requires Android 8.0 or later in the current code.
- Percentage calculations are converted during final evaluation, so live percentage previews may differ from the final result.
- The `Del` button deletes all calculation history rather than individual records.
- The application does not include scientific calculator functions.
- Calculation history is stored locally without cloud synchronization.

## 🎯 Learning Outcomes

This project demonstrates:

- Developing Android applications using Java.
- Designing interfaces using XML layouts.
- Implementing arithmetic operations and expression evaluation.
- Integrating third-party Java libraries.
- Storing and retrieving information using SQLite.
- Displaying calculation history with Material UI components.
- Implementing Android notifications.
- Managing runtime permissions.
- Handling user interactions and application events.

## 📝 Conclusion

**Calculator App** is a Java-based Android project that demonstrates arithmetic expression evaluation, real-time result previews, calculation history management, and notification integration.

It provides practical experience in Android UI development, SQLite database operations, and third-party library integration, making it a useful project for learning Android application development.

