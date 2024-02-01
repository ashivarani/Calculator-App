package com.example.mycalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Time;


public class DabaseHelper extends SQLiteOpenHelper {

    //To create SQLite database
    public DabaseHelper( Context context,  String dbname, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbname, factory, version);
    }
    //creatng table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table mycalculator(Firstvalue varchar(50),Operation varchar(50),Secondvalue varchar(50),Result varchar(50), Timestamp varchar(50))");
        Log.d("DatabaseHelper","mycalculator table is created successfully");

    }

    //drop the table when the version changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists mycalculator");
        Log.d("onUpgrade","Drop the table as the version changed");
        onCreate(db);

    }
    //To save Calculator data
    public long caldata(String Firstvalue,String Operation,String Secondvalue,String Result, String Timestamp) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Firstvalue",Firstvalue);
        cv.put("Operation",Operation);
        cv.put("Secondvalue",Secondvalue);
        cv.put("Result",Result);
        cv.put("Timestamp",Timestamp );
        Log.d("caldata","Values inserted into table successfully");

        return db.insert("mycalculator",null,cv);
    }
    //To view all the records from users table
    public String getallrecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery("select * from mycalculator ORDER BY Timestamp DESC",null);
        String output = "";
        while(cursor.moveToNext()) {
            String Firstvalue = cursor.getString(0);
            String Operation = cursor.getString(1);
            String Secondvalue = cursor.getString(2);
            String Result = cursor.getString(3);
            String Timestamp = cursor.getString(4);
            output += Firstvalue+Operation+Secondvalue+"="+Result + " " + "["+Timestamp + "]\n";
        }
        Log.d("getallrecords","Read values from table successfully");
        return output;
    }
}
