package com.example.myapplication.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.ali.uneversaldatetools.date.JalaliDateTime;
import com.example.myapplication.model.HistoryModel;
import com.example.myapplication.model.Period;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GetDates {

    private static final String TAG = "GetDates";

    static RoutineDB routineDB;
    static ArrayList<HistoryModel> tasksHistory;
    static HistoryModel[] taskModels;

    static JalaliDateTime jalaliDateTime = JalaliDateTime.Now();
    static Calendar calendar = java.util.Calendar.getInstance();


    public GetDates() {
        calendar.setFirstDayOfWeek(Calendar.SATURDAY);
    }

    public  HistoryModel[] getTaskHistoryArray(Context context, int routineId) {
        getDailyDates(context, routineId);

        taskModels = new HistoryModel[tasksHistory.size()];

        for (int i = 0; i < tasksHistory.size(); i++) {
            taskModels[i] = tasksHistory.get(i);

        }

        return taskModels;
    }

    public  List<HistoryModel> getTaskHistoryList(Context context, int routineId){
        getDailyDates(context, routineId);
        return tasksHistory;
    }


    private void getDailyDates(Context context, int routineId) {
        routineDB = new RoutineDB(context);
        Log.d(TAG, "onCreate: database initialized");

        tasksHistory = new ArrayList<>();


        Cursor routineCursor = routineDB.getRecord(routineId);
        Cursor dayscursor = routineDB.getHistory(routineId);

        int tDay = routineCursor.getInt(4);
        int tWeek = routineCursor.getInt(5);
        int tMonth = routineCursor.getInt(6);
        int tYear = routineCursor.getInt(7);

        String dbPeriod = routineCursor.getString(3);
        Period period;

        switch (dbPeriod) {
            case "daily":
                period = Period.daily;
                break;

            case "weekly":
                period = Period.weekly;
                break;

            case "monthly":
                period = Period.monthly;
                break;
            default:
                period = Period.yearly;
                break;
        }

        HistoryModel model;

        if (dayscursor.moveToFirst()) {
            Log.d(TAG, "onCreate: fetching tasks from database");

            do {
                model = new HistoryModel(
                        period,
                        dayscursor.getInt(dayscursor.getColumnIndexOrThrow("isdone")),
                        dayscursor.getInt(dayscursor.getColumnIndexOrThrow("changeday")),
                        dayscursor.getInt(dayscursor.getColumnIndexOrThrow("changeweek")),
                        dayscursor.getInt(dayscursor.getColumnIndexOrThrow("changemonth")),
                        dayscursor.getInt(dayscursor.getColumnIndexOrThrow("changeyear"))

                );

                switch (model.getPeriod()) {
                    case daily:
                        if (afterCreateDay(model.getChangeDay(), model.getChangeMonth(), model.getChangeYear(), tDay, tMonth, tYear)) {
                            tasksHistory.add(model);
                        }
                        break;

                    case weekly:
                        if (afterCreatedWeek(model.getChangeWeek(), model.getChangeYear(), tWeek, tYear)) {
                            tasksHistory.add(model);
                        }
                        break;

                    case monthly:
                        if (afterCreateMonth(model.getChangeMonth(), model.getChangeYear(), tMonth, tYear)) {
                            tasksHistory.add(model);
                        }
                        break;
                    default:
                        break;
                }


            } while (dayscursor.moveToNext());
            Log.d(TAG, "onCreate: tasks loaded from database");
        } else {
            Log.d(TAG, "onCreate: no tasks found in database");
        }


    }

    private boolean afterCreateDay(int day, int month, int year, int tDay, int tMonth, int tYear) {

        boolean b = false;
        if (tYear < year) {
            b = true;
        } else if ((tYear == year) && (tMonth < month)) {
            b = true;
        } else if ((tYear == year) && (tMonth == month) && (tDay <= day)){
            b = true;
        }

        if (jalaliDateTime.getYear() < year) {
            b = false;
        } else if ((jalaliDateTime.getYear() == year) && (jalaliDateTime.getMonth() < month)) {
            b = false;
        } else if ((jalaliDateTime.getYear() == year) && (jalaliDateTime.getMonth() == month) && (jalaliDateTime.getDay() < day)){
            b = false;
        }

        return b;
    }

    private boolean afterCreatedWeek(int week, int year, int tWeek, int tYear) {

        boolean b = false;

        if (tYear < year) {
            b = true;
        } else if( (tYear == year) && (tWeek <= week)){
            b = true;
        }

        if (jalaliDateTime.getYear() < year) {
            b = false;
        } else if ((jalaliDateTime.getYear() == year) && (calendar.get(Calendar.WEEK_OF_YEAR) < week)) {
            b = false;
        }

        return b;
    }

    private boolean afterCreateMonth(int month, int year, int tMonth, int tYear) {

        boolean b = false;
        if (tYear < year) {
            b = true;
        } else if ((tYear == year) && (tMonth <= month)) {
            b = true;
        }

        if (jalaliDateTime.getYear() < year) {
            b = false;
        } else if ((jalaliDateTime.getYear() == year) && (jalaliDateTime.getMonth() < month)) {
            b = false;
        }

        return b;

    }
}
