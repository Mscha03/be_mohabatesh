package com.example.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class RoutineDB extends SQLiteOpenHelper {

    private static final String TAG = "ROUTINE_DATA_BASE";

    private static final String DB_NAME = "routine_db";
    private static final int DB_VERSION = 2;
    public static final String ROUTINE_TABLE_NAME = "routine";
    private static final String ROUTINE_ID_COL = "id";
    private static final String ROUTINE_NAME_COL = "name";
    private static final String ROUTINE_DESCRIPTION_COL = "description";
    private static final String ROUTINE_PERIOD_COL = "period";
    private static final String ROUTINE_YEAR = "year";

    public static final String DAYS_TABLE_NAME = "habits";
    private static final String DAYS_TABLE_ID = "id";
    private static final String DAYS_ROUTINE_TABLE_ID = "routine_id";
    private static final String DAYS_DONE_COL = "isdone";
    private static final String DAYS_DAY = "changeday";
    private static final String DAYS_Week = "changeweek";
    private static final String DAYS_MONTH = "changemonth";
    private static final String DAYS_YEAR = "changeyear";


    public RoutineDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createRoutineTable =
                "CREATE TABLE " + ROUTINE_TABLE_NAME + " ( "
                        + ROUTINE_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + ROUTINE_NAME_COL + " TEXT, "
                        + ROUTINE_DESCRIPTION_COL + " TEXT, "
                        + ROUTINE_PERIOD_COL + " TEXT, "
                        + ROUTINE_YEAR + " int)";

        String createDaysTable =
                "CREATE TABLE " + DAYS_TABLE_NAME + " ( "
                    + DAYS_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DAYS_ROUTINE_TABLE_ID + " INTEGER, "
                    + DAYS_DONE_COL + " INTEGER, "
                    + DAYS_DAY + " INTEGER, "
                    + DAYS_Week + " INTEGER, "
                    + DAYS_MONTH + " INTEGER, "
                    + DAYS_YEAR + " INTEGER, "
                    + "FOREIGN KEY(" + DAYS_ROUTINE_TABLE_ID + ") REFERENCES " + ROUTINE_TABLE_NAME + "( " + ROUTINE_ID_COL + " ))";


        db.execSQL(createRoutineTable);
        db.execSQL(createDaysTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ROUTINE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DAYS_TABLE_NAME);
        onCreate(db);
    }

    // Create
    public long insertRecord(
            String name, String description, String period, int year) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ROUTINE_NAME_COL, name);
        values.put(ROUTINE_DESCRIPTION_COL, description);
        values.put(ROUTINE_PERIOD_COL, period);
        values.put(ROUTINE_YEAR, year);
        return db.insert(ROUTINE_TABLE_NAME, null, values);
    }

    public void insertDays(
            int routine_id, int done, int day, int week, int month, int year){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DAYS_ROUTINE_TABLE_ID, routine_id);
        values.put(DAYS_DONE_COL, done);
        values.put(DAYS_DAY, day);
        values.put(DAYS_Week, week);
        values.put(DAYS_MONTH, month);
        values.put(DAYS_YEAR, year);

        db.insert(DAYS_TABLE_NAME, null, values);
    }

    // Read
    public Cursor getAllRecords(String table_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(table_name, null, null, null, null, null, null);
    }


    public Cursor getRecord(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ROUTINE_TABLE_NAME, null, ROUTINE_ID_COL + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getDays(int routineId, int day, int week, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DAYS_TABLE_NAME, null,
                DAYS_ROUTINE_TABLE_ID + " = ?" + " AND "
                        + DAYS_DAY + " = ?" + " AND "+ DAYS_Week + " = ?" + " AND "
                        + DAYS_MONTH + " = ?" + " AND "+ DAYS_YEAR + " = ?",
                new String[]{String.valueOf(routineId), String.valueOf(day), String.valueOf(week), String.valueOf(month), String.valueOf(year)}
                , null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    // Update
    public void updateRecord(
            int id, String name, String description, String period, int year) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ROUTINE_NAME_COL, name);
        values.put(ROUTINE_DESCRIPTION_COL, description);
        values.put(ROUTINE_PERIOD_COL, period);
        values.put(ROUTINE_YEAR, year);
        db.update(ROUTINE_TABLE_NAME, values, ROUTINE_ID_COL + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateDays(
            int routine_id, int done, int day, int week, int month, int year
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DAYS_ROUTINE_TABLE_ID, routine_id);
        values.put(DAYS_DONE_COL, done);
        values.put(DAYS_DAY, day);
        values.put(DAYS_Week, week);
        values.put(DAYS_MONTH, month);
        values.put(DAYS_YEAR, year);

        db.update(DAYS_TABLE_NAME, values,
                DAYS_DAY + " = ?" + " AND "+ DAYS_Week + " = ?" + " AND "+ DAYS_MONTH + " = ?" + " AND "+ DAYS_YEAR + " = ?"
                , new String[]{String.valueOf(day), String.valueOf(week), String.valueOf(month), String.valueOf(year)});
    }

    // Delete
    public void deleteRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ROUTINE_TABLE_NAME, ROUTINE_ID_COL + " = ?", new String[]{String.valueOf(id)});
    }

}


