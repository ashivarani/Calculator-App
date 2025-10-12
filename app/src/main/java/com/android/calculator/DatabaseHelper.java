package com.android.calculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseHelper manages all SQLite database operations for the Calculator app.
 * Provides methods to insert, fetch, and delete calculation history records.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private final AppLogger logger = new AppLogger("DatabaseHelper");

    private static final String DATABASE_NAME = "CalculatorDatabase";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CALCULATION_HISTORY = "calculation_history";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EXPRESSION = "expression";

    /**
     * Constructs a DatabaseHelper instance.
     * @param context the application context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        logger.d("DatabaseHelper initialized with DB: " + DATABASE_NAME);
    }

    /**
     * Called when the database is created for the first time.
     * Creates the calculation_history table if it does not exist.
     * @param db the SQLite database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_CALCULATION_HISTORY + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EXPRESSION + " TEXT NOT NULL)";
            db.execSQL(createTableQuery);
            logger.d("Calculation history table created successfully");
            logger.v("Executed query: " + createTableQuery);
        } catch (Exception e) {
            logger.e("Failed to create table", e);
            logger.w("onCreate encountered an exception");
        }
    }

    /**
     * Called when the database needs to be upgraded.
     * Drops the old table and recreates it.
     * @param db         the SQLite database
     * @param oldVersion the old database version
     * @param newVersion the new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALCULATION_HISTORY);
            logger.v("Dropped table if exists: " + TABLE_CALCULATION_HISTORY);
            onCreate(db);
            logger.d("Database upgraded: " + oldVersion + " → " + newVersion);
        } catch (Exception e) {
            logger.e("Failed during onUpgrade", e);
            logger.w("Database upgrade from " + oldVersion + " to " + newVersion + " failed");
        }
    }

    /**
     * Inserts a new calculation expression into the database.
     * @param expression the calculation expression to store
     * @return the row ID of the inserted record, or -1 if insertion failed
     */
    public long insertCalculation(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            logger.w("Empty expression, skipping insert");
            return -1;
        }

        long id = -1;
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_EXPRESSION, expression.trim());
            id = db.insert(TABLE_CALCULATION_HISTORY, null, values);
            if (id > 0) {
                logger.d("Inserted record ID=" + id + " Expression=[" + expression + "]");
                logger.v("ContentValues: " + values);
            } else {
                logger.e("Insert failed for expression: " + expression, (Throwable) null);
                logger.w("insertCalculation returned -1");
            }
        } catch (Exception e) {
            logger.e("Exception during insertCalculation", e);
            logger.w("Failed to insert calculation: " + expression);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
                logger.v("Database closed after insert");
            }
        }
        return id;
    }

    /**
     * Retrieves all calculation history expressions from the database.
     * @return a list of expressions in descending order of insertion
     */
    public List<String> getAllRecordsList() {
        List<String> records = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = null;

        try {
            db = getReadableDatabase();
            String query = "SELECT " + COLUMN_EXPRESSION + " FROM " + TABLE_CALCULATION_HISTORY +
                    " ORDER BY " + COLUMN_ID + " DESC";
            cursor = db.rawQuery(query, null);
            logger.v("Executed query: " + query);

            if (cursor.moveToFirst()) {
                do {
                    String expression = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPRESSION));
                    if (expression != null && !expression.trim().isEmpty()) {
                        records.add(expression.trim());
                        logger.d("Fetched record: " + expression);
                    } else {
                        logger.w("Skipped empty/null record in DB");
                    }
                } while (cursor.moveToNext());
            } else {
                logger.v("No records found in DB");
            }
        } catch (Exception e) {
            logger.e("Error fetching calculation history", e);
            logger.w("getAllRecordsList encountered an error");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                logger.v("Cursor closed after fetching records");
            }
            if (db != null && db.isOpen()) {
                db.close();
                logger.v("Database closed after fetching records");
            }
        }

        logger.d("Total records fetched: " + records.size());
        return records;
    }

    /**
     * Deletes all calculation history records from the database.
     * @return true if deletion succeeded, false otherwise
     */
    public boolean deleteAllRecords() {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            int rowsDeleted = db.delete(TABLE_CALCULATION_HISTORY, null, null);
            logger.d("Deleted " + rowsDeleted + " records");
            logger.v("deleteAllRecords executed successfully");
            return true;
        } catch (Exception e) {
            logger.e("Failed to delete all records", e);
            logger.w("deleteAllRecords failed");
            return false;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
                logger.v("Database closed after deleteAllRecords");
            }
        }
    }
}
