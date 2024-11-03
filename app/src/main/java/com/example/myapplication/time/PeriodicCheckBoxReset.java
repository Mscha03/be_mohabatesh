package com.example.myapplication.time;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.example.myapplication.database.taskDataBase.habits.DailyHabitDB;

public class PeriodicCheckBoxReset {
    private static DailyHabitDB db;
    private static final String TAG = "PeriodicCheckBoxReset";

    public static int checkDay(int routineId, int changeDay, int changeWeek, int changeMonth, int changeYear, Context context) {

        db = new DailyHabitDB(context);
        Cursor cursor = db.getDays(routineId, changeDay, changeMonth, changeYear);
        int check = 0;
        if (cursor.moveToFirst()) {
            Log.d(TAG, "checkDay: fetching tasks from database");
            do {
                check = cursor.getInt(cursor.getColumnIndexOrThrow("isdone"));
            } while (cursor.moveToNext());

        }
        return check;
    }
}
