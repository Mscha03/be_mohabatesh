package com.example.myapplication.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.CheckBox;

import com.ali.uneversaldatetools.date.JalaliDateTime;
import com.example.myapplication.Period;
import com.example.myapplication.changer.BoolInt;
import com.example.myapplication.model.PeriodicModel;
import com.example.myapplication.model.TaskModel;

import java.util.ArrayList;
import java.util.Calendar;

public class GetUndoneTask {

    private static String TAG = "GetAllTask";

    static TaskDB db;
    static ArrayList<TaskModel> today;
    static ArrayList<TaskModel> future;
    static ArrayList<TaskModel> past;

    static TaskModel[] todayModels;
    static TaskModel[] futureModels;
    static TaskModel[] pastModels;

    // periodic task
    static RoutineDB routineDB;
    static ArrayList<PeriodicModel> dailyTasks;
    static ArrayList<PeriodicModel> weeklyTasks;
    static ArrayList<PeriodicModel> monthlyTasks;
    static PeriodicModel[] dailyModels;
    static PeriodicModel[] weeklyModels;
    static PeriodicModel[] monthlyModels;
    static PeriodicModel[] allRoutineModels;

    public static TaskModel[] todayTasks(Context context) {
        getNormalTasks(context);
        return todayModels;
    }

    public static TaskModel[] futureTasks(Context context) {
        getNormalTasks(context);
        return futureModels;
    }

    public static TaskModel[] pastTasks(Context context) {
        getNormalTasks(context);
        return pastModels;
    }

    public static PeriodicModel[] dailyTasks(Context context) {
        getPeriodicTasks(context);
        return dailyModels;
    }

    public static PeriodicModel[] weeklyTasks(Context context) {
        getPeriodicTasks(context);
        return weeklyModels;
    }

    public static PeriodicModel[] monthlyTasks(Context context) {
        getPeriodicTasks(context);
        return monthlyModels;
    }

    public static PeriodicModel[] allRoutineTasks(Context context) {
        getPeriodicTasks(context);
        allRoutineModels = new PeriodicModel[dailyModels.length + weeklyModels.length + monthlyModels.length];

        for (int i = 0; i < dailyModels.length + weeklyModels.length + monthlyModels.length; i++) {
            if (i < dailyModels.length) {
                for (PeriodicModel dailyModel : dailyModels) {
                    allRoutineModels[i] = dailyModel;
                }
            } else if (i < dailyModels.length + weeklyModels.length) {
                for (PeriodicModel weeklyModel : weeklyModels) {
                    allRoutineModels[i] = weeklyModel;
                }
            } else {
                for (PeriodicModel monthlyModel : monthlyModels) {
                    allRoutineModels[i] = monthlyModel;
                }
            }
        }


        for (int i = 0; i < dailyTasks.size(); i++) {
            allRoutineModels[i] = dailyTasks.get(i);
        }
        for (int i = dailyTasks.size(); i < weeklyTasks.size(); i++) {
            allRoutineModels[i] = weeklyTasks.get(i);
        }
        for (int i = weeklyTasks.size(); i < monthlyTasks.size(); i++) {
            allRoutineModels[i] = monthlyTasks.get(i);
        }
        return allRoutineModels;
    }


    private static void getNormalTasks(Context context) {
        db = new TaskDB(context);
        today = new ArrayList<>();
        future = new ArrayList<>();
        past = new ArrayList<>();

        Cursor cursor = db.getAllRecords();
        if (cursor.moveToFirst()) {
            Log.d(TAG, "onCreate: fetching tasks from database");

            do {

                CheckBox checkBox = new CheckBox(context);
                checkBox.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                checkBox.setChecked(BoolInt.intToBool(cursor.getInt(cursor.getColumnIndexOrThrow("isdone"))));

                TaskModel taskModel = new TaskModel(
                        checkBox,
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("deadday")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("deadmonth")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("deadyear"))
                );

                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.SATURDAY);
                JalaliDateTime jalaliDateTime = JalaliDateTime.Now();

                int thisDay = jalaliDateTime.getDay();
                int thisMonth = jalaliDateTime.getMonth();
                int thisYear = jalaliDateTime.getYear();
                Log.d(TAG, "onClick: current date - day: " + today + ", month: " + thisMonth + ", year: " + thisYear);

                int deadDay = taskModel.getDeadDay();
                int deadMonth = taskModel.getDeadMonth();
                int deadYear = taskModel.getDeadYear();
                Log.d(TAG, "onClick: deadline date - day: " + deadDay + ", month: " + deadMonth + ", year: " + deadYear);

                if (!taskModel.getCheckBox().isChecked()) {
                    if ((deadDay == thisDay) && (deadMonth == thisMonth) && (deadYear == thisYear)) {
                        today.add(taskModel);
                    } else if ((deadYear > thisYear) || ((deadYear == thisYear) && (deadMonth > thisMonth)) || ((deadYear == thisYear) && (deadMonth == thisMonth) && (deadDay > thisDay))) {
                        future.add(taskModel);
                    } else {
                        past.add(taskModel);
                    }
                }

            } while (cursor.moveToNext());
            Log.d(TAG, "noCreate: tasks loaded from database");

        } else {
            Log.d(TAG, "onCreate: no tasks found in database");
        }
        cursor.close();

        todayModels = new TaskModel[today.size()];
        futureModels = new TaskModel[future.size()];
        pastModels = new TaskModel[past.size()];

        for (int i = 0; i < today.size(); i++) {
            todayModels[i] = today.get(i);
        }
        for (int i = 0; i < future.size(); i++) {
            futureModels[i] = future.get(i);
        }
        for (int i = 0; i < past.size(); i++) {
            pastModels[i] = past.get(i);
        }

    }

    private static void getPeriodicTasks(Context context) {
        routineDB = new RoutineDB(context);
        Log.d(TAG, "onCreate: database initialized");
        dailyTasks = new ArrayList<>();
        weeklyTasks = new ArrayList<>();
        monthlyTasks = new ArrayList<>();

        Cursor cursor = routineDB.getAllRecords();
        if (cursor.moveToFirst()) {
            Log.d(TAG, "onCreate: fetching tasks from database");

            do {

                CheckBox checkBox = new CheckBox(context);
                checkBox.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                checkBox.setChecked(BoolInt.intToBool(cursor.getInt(cursor.getColumnIndexOrThrow("isdone"))));

                String dbPeriod = cursor.getString(cursor.getColumnIndexOrThrow("period"));
                Period period = null;
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
                        break;
                }

                PeriodicModel periodicModel = new PeriodicModel(
                        checkBox,
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        period,
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("changeday")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("changeweek")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("changemonth"))
                );

                Log.d(TAG, "onCreate: periodicModel created:" +
                        " title: " + periodicModel.getCheckBox().getText() +
                        " isDone: " + periodicModel.getCheckBox().isChecked() +
                        " descreption: " + periodicModel.getDescription() +
                        " period: " + periodicModel.getPeriod().toString() +
                        " id: " + periodicModel.getId() +
                        " day: " + periodicModel.getChangeDay() +
                        " month: " + periodicModel.getChangeMonth() +
                        " week: " + periodicModel.getChangeWeek());

                if (!periodicModel.getCheckBox().isChecked()) {
                    switch (dbPeriod) {
                        case "daily":
                            dailyTasks.add(periodicModel);
                            break;

                        case "weekly":
                            weeklyTasks.add(periodicModel);
                            break;
                        case "monthly":
                            monthlyTasks.add(periodicModel);
                            break;
                        default:
                            break;
                    }
                }

            } while (cursor.moveToNext());
            Log.d(TAG, "onCreate: tasks loaded from database");
        } else {
            Log.d(TAG, "onCreate: no tasks found in database");
        }
        cursor.close();

        dailyModels = new PeriodicModel[dailyTasks.size()];
        weeklyModels = new PeriodicModel[weeklyTasks.size()];
        monthlyModels = new PeriodicModel[monthlyTasks.size()];

        for (int i = 0; i < dailyTasks.size(); i++) {
            dailyModels[i] = dailyTasks.get(i);
        }
        for (int i = 0; i < weeklyTasks.size(); i++) {
            weeklyModels[i] = weeklyTasks.get(i);

        }
        for (int i = 0; i < monthlyTasks.size(); i++) {

            monthlyModels[i] = monthlyTasks.get(i);

        }
    }

}

