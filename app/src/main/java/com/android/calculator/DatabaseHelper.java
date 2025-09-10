package com.android.calculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "MyCalculatorDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "calculator_history";

    // Columns
    private static final String COL_FIRST = "FirstValue";
    private static final String COL_OP = "Operation";
    private static final String COL_SECOND = "SecondValue";
    private static final String COL_RESULT = "Result";
    private static final String COL_TIME = "Timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_FIRST + " TEXT," +
                COL_OP + " TEXT," +
                COL_SECOND + " TEXT," +
                COL_RESULT + " TEXT," +
                COL_TIME + " TEXT)";
        db.execSQL(createTableQuery);
        Log.d(TAG, "Calculator history table created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        Log.d(TAG, "Table dropped due to version upgrade");
        onCreate(db);
    }

    /** Insert a calculation record into the database */
    public long insertCalculation(String firstValue, String operation,
                                  String secondValue, String result, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_FIRST, firstValue);
        cv.put(COL_OP, operation);
        cv.put(COL_SECOND, secondValue);
        cv.put(COL_RESULT, result);
        cv.put(COL_TIME, timestamp);

        long recordId = db.insert(TABLE_NAME, null, cv);
        Log.d(TAG, "Record inserted with id: " + recordId);
        return recordId;
    }

    /** Retrieve all calculation records as a formatted string (for TextView/ScrollView) */
    public String getAllRecordsString() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_TIME + " DESC", null);
        StringBuilder output = new StringBuilder();

        if (cursor.moveToFirst()) {
            do {
                String first = cursor.getString(cursor.getColumnIndexOrThrow(COL_FIRST));
                String op = cursor.getString(cursor.getColumnIndexOrThrow(COL_OP));
                String second = cursor.getString(cursor.getColumnIndexOrThrow(COL_SECOND));
                String result = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESULT));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COL_TIME));

                output.append(first)
                        .append(" ")
                        .append(op)
                        .append(" ")
                        .append(second)
                        .append(" = ")
                        .append(result)
                        .append("\n[").append(time).append("]\n")
                        .append("-----------------------------\n");
            } while (cursor.moveToNext());
        } else {
            output.append("No history available.");
        }

        cursor.close();
        return output.toString();
    }

    /** Retrieve all calculation records as a List (better for RecyclerView) */
    public List<String> getAllRecordsList() {
        List<String> records = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_TIME + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                String first = cursor.getString(cursor.getColumnIndexOrThrow(COL_FIRST));
                String op = cursor.getString(cursor.getColumnIndexOrThrow(COL_OP));
                String second = cursor.getString(cursor.getColumnIndexOrThrow(COL_SECOND));
                String result = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESULT));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COL_TIME));

                records.add(first + " " + op + " " + second + " = " + result + " [" + time + "]");
            } while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }
}
