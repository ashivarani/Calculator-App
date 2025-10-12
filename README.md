Android Calculator App

A modern, fully-featured calculator Android app with expression evaluation, real-time results, history management, and notification support.

Features
**Basic Arithmetic Operations**

Addition (+), Subtraction (-), Multiplication (*), Division (/)

Percentage (%) and Decimal (.) support

Real-time calculation as user types

**Expression Evaluation**

Handles complex expressions using exp4j library

Correctly handles operator precedence and multiple operations

Displays result in real-time below input

**Input Management**

Numeric input buttons (0–9)

Operator buttons (+, -, *, /, %)

Decimal point input with validation (prevents multiple dots in a number segment)

Clear (C) button: deletes last character

All Clear (AC) button: resets input and result

Delect Records (DEL): deletes db stored history records

**Calculation History**

Stores expressions and results in SQLite database (DatabaseHelper)

History includes full expression with result (e.g., 2+2=4)

History can be viewed via History menu

Displayed as cards in a scrollable list (DisplayActivity)

Option to clear all history with OFF button

**Notifications**

Shows a welcome notification when the app opens

Compatible with Android 13+ permission system

Non-persistent notification that opens app when tapped

Handles permission denied scenario with App Settings dialog (NotificationHelper)

**Logging and Debugging**

All major actions, errors, and events are logged using AppLogger

#### Logs include:

Button presses

Expression evaluation results

Database operations (insert, fetch, delete)

Notification permission and display events

**UI Features**

Modern Material Design layout

Toolbar with back navigation in history screen (DisplayActivity)

History displayed in CardView for clean visualization

Dynamic updates for input and result display

**Compatibility**

Supports Android API level 21+

Special handling for Android O+ (notification channels)

Special handling for Android 13+ (POST_NOTIFICATIONS permission)

**Project Structure**
_com.android.calculator_
│
├─ MainActivity.java           # Handles calculator UI, button interactions, history menu
├─ ExpressionEvaluator.java   # Evaluates expressions, manages input, real-time result
├─ DatabaseHelper.java        # SQLite database helper for storing calculation history
├─ DisplayActivity.java       # Displays history in CardView layout
├─ NotificationHelper.java    # Handles notifications and permissions
├─ AppLogger.java             # Central logging utility for debugging

**Dependencies**

exp4j
— Expression evaluation library

AndroidX libraries for Material Design (MaterialButton, MaterialToolbar, CardView)

_Usage_

Open the app.

Input numbers and operators using buttons.

Results are updated in real-time below input.

Press = to evaluate expression and save it to history.

Open History menu to view all previous calculations.

Press OFF to clear all calculation history.

Notifications appear on app launch (permission required on Android 13+).

_Logging_

Use AppLogger for debugging:

logger.d("Debug message") for info

logger.v("Verbose message") for detailed info

logger.w("Warning message") for potential issues

logger.e("Error message", throwable) for exceptions
