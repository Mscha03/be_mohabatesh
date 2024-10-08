package com.example.myapplication.database.TaskDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DeadLinedTaskDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "task_db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "task";
    private static final String ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String DESCRIPTION_COL = "description";
    private static final String ISDONE_COL = "isdone";
    private static final String DEADDAY = "deadday";
    private static final String DEADMONTH = "deadmonth";
    private static final String DEADYEAR = "deadyear";

    public DeadLinedTaskDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable =
                "CREATE TABLE " + TABLE_NAME + " ( "
                        + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + NAME_COL + " TEXT, "
                        + DESCRIPTION_COL + " TEXT, "
                        + ISDONE_COL + " int, "
                        + DEADDAY + " int, "
                        + DEADMONTH + " int, "
                        + DEADYEAR + " int )";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Create
    public void insertRecord(
            String name, String description,
            int isDone, int deadDay, int deadMonth, int deadYear) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, name);
        values.put(DESCRIPTION_COL, description);
        values.put(ISDONE_COL, isDone);
        values.put(DEADDAY, deadDay);
        values.put(DEADMONTH, deadMonth);
        values.put(DEADYEAR, deadYear);
        db.insert(TABLE_NAME, null, values);
    }

    // Read
    public Cursor getAllRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);

    }

    public Cursor getRecord(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, ID_COL + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    // Update
    public void updateRecord(
            int id, String name, String description,
            int isDone, int deadDay, int deadMonth, int deadYear) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, name);
        values.put(DESCRIPTION_COL, description);
        values.put(ISDONE_COL, isDone);
        values.put(DEADDAY, deadDay);
        values.put(DEADMONTH, deadMonth);
        values.put(DEADYEAR, deadYear);
        db.update(TABLE_NAME, values, ID_COL + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Delete
    public void deleteRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID_COL + " = ?", new String[]{String.valueOf(id)});
    }
}


