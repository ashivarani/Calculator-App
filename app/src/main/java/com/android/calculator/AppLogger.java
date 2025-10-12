package com.android.calculator;

import android.util.Log;

/**
 * AppLogger is a simple instance-based logging utility for the Calculator app.
 * Each class can create its own AppLogger instance with a custom tag for better log tracing.
 * This logger wraps Android's {@link android.util.Log} methods and provides null-safety for messages.
 */
public class AppLogger {

    /** The tag used for Android logging. Defaults to "Calculator" if not specified. */
    private final String tag;

    /**
     * Creates a new AppLogger instance with a custom tag.
     * @param tag the tag to identify log messages; if null or empty, "Calculator" is used.
     */
    public AppLogger(String tag) {
        this.tag = (tag == null || tag.isEmpty()) ? "Calculator" : tag;
    }

    /**
     * Logs a debug message.
     * @param msg the message to log; if null, "null" is logged.
     */
    public void d(String msg) {
        if (msg == null) msg = "null";
        Log.d(tag, msg);
    }

    /**
     * Logs a verbose message.
     * @param msg the message to log; if null, "null" is logged.
     */
    public void v(String msg) {
        if (msg == null) msg = "null";
        Log.v(tag, msg);
    }

    /**
     * Logs a warning message.
     * @param msg the message to log; if null, "null" is logged.
     */
    public void w(String msg) {
        if (msg == null) msg = "null";
        Log.w(tag, msg);
    }

    /**
     * Logs an error message with optional Throwable objects.
     * If one or more Throwable objects are provided, each will be logged along with the message.
     * If no Throwable is provided, only the message is logged.
     *
     * @param msg the error message; if null, "null" is logged.
     * @param throwables optional Throwable objects associated with the error.
     */
    public void e(String msg, Throwable... throwables) {
        if (msg == null) msg = "null";

        if (throwables == null || throwables.length == 0) {
            Log.e(tag, msg);
        } else {
            for (Throwable tr : throwables) {
                Log.e(tag, msg, tr);
            }
        }
    }
}
