package com.example.myapplication.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.CheckBox;

import com.ali.uneversaldatetools.date.JalaliDateTime;
import com.example.myapplication.Period;
import com.example.myapplication.changer.BoolInt;
import com.example.myapplication.model.PeriodicModel;
import com.example.myapplication.model.SimpleModel;
import com.example.myapplication.model.TaskModel;
import com.example.myapplication.time.PeriodicCheckBoxReset;

import java.util.ArrayList;
import java.util.Calendar;

public class GetAllTask {

    private static String TAG = "GetAllTask";

    // normal task
    static TaskDB taskDB;
    static ArrayList<TaskModel> today;
    static ArrayList<TaskModel> future;
    static ArrayList<TaskModel> past;
    static TaskModel[] todayModels;
    static TaskModel[] futureModels;
    static TaskModel[] pastModels;

    // periodic task
    static RoutineDB routineDB ;
    static ArrayList<PeriodicModel> dailyTasks;
    static ArrayList<PeriodicModel> weeklyTasks;
    static ArrayList<PeriodicModel> monthlyTasks;
    static PeriodicModel[] dailyModels;
    static PeriodicModel[] weeklyModels;
    static PeriodicModel[] monthlyModels;

    // simple task
    static SimpleDB simpleDB ;
    static ArrayList<SimpleModel> simpleTasks;
    static SimpleModel[] simpleModels;

    public static TaskModel[] todayTasks(Context context){
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


    public static PeriodicModel[] dailyTasks(Context context){
        getPeriodicTasks(context);
        return dailyModels;
    }
    public static PeriodicModel[] weeklyTasks(Context context){
        getPeriodicTasks(context);
        return weeklyModels;
    }
    public static PeriodicModel[] monthlyTasks(Context context){
        getPeriodicTasks(context);
        return monthlyModels;
    }

    public static SimpleModel[] simpleTasks(Context context){
        getSimpleTasks(context);
        return simpleModels;
    }


    private static void getNormalTasks(Context context) {
        taskDB = new TaskDB(context);
        today = new ArrayList<>();
        future = new ArrayList<>();
        past= new ArrayList<>();

        Cursor cursor = taskDB.getAllRecords();
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

                if ((deadDay == thisDay) && (deadMonth == thisMonth) && (deadYear == thisYear)) {
                    today.add(taskModel);
                } else if ((deadYear > thisYear) || ((deadYear == thisYear) && (deadMonth > thisMonth)) || ((deadYear == thisYear) && (deadMonth == thisMonth) && (deadDay > thisDay))) {
                    future.add(taskModel);
                } else {
                    past.add(taskModel);
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
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));

                String title = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int isDone = cursor.getInt(cursor.getColumnIndexOrThrow("isdone"));

                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                String dbPeriod =  cursor.getString(cursor.getColumnIndexOrThrow("period"));

                int dbChangeDay = cursor.getInt(cursor.getColumnIndexOrThrow("changeday"));
                int dbChangeWeek = cursor.getInt(cursor.getColumnIndexOrThrow("changeweek"));
                int dbChangeMonth = cursor.getInt(cursor.getColumnIndexOrThrow("changemonth"));

                CheckBox checkBox = new CheckBox(context);
                checkBox.setText(title);
                checkBox.setChecked(PeriodicCheckBoxReset.checkDay(isDone, dbChangeDay, dbChangeWeek, dbChangeMonth, dbPeriod));

                switch (dbPeriod){
                    case "daily":
                        dailyTasks.add(new PeriodicModel(checkBox, description, Period.daily, id, dbChangeDay, dbChangeWeek, dbChangeMonth));
                        break;

                    case "weekly":
                        weeklyTasks.add(new PeriodicModel(checkBox, description, Period.weekly, id, dbChangeDay, dbChangeWeek, dbChangeMonth));
                        break;
                    case "monthly":
                        monthlyTasks.add(new PeriodicModel(checkBox, description, Period.monthly, id, dbChangeDay, dbChangeWeek, dbChangeMonth));
                        break;
                    default:
                        break;
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

    private static void getSimpleTasks(Context context) {
        simpleDB = new SimpleDB(context);
        Log.d(TAG, "onCreate: database initialized");
        simpleTasks = new ArrayList<>();

        Cursor cursor = simpleDB.getAllRecords();
        if (cursor.moveToFirst()) {
            Log.d(TAG, "onCreate: fetching tasks from database");

            do {
                CheckBox checkBox = new CheckBox(context);
                checkBox.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                checkBox.setChecked(BoolInt.intToBool(cursor.getInt(cursor.getColumnIndexOrThrow("isdone"))));

                SimpleModel simpleModel = new SimpleModel(
                        checkBox,
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                );

                simpleTasks.add(simpleModel);

            } while (cursor.moveToNext());
            Log.d(TAG, "onCreate: tasks loaded from database");
        } else {
            Log.d(TAG, "onCreate: no tasks found in database");
        }
        cursor.close();

        simpleModels = new SimpleModel[simpleTasks.size()];

        for (int i = 0; i < simpleTasks.size(); i++) {
            simpleModels[i] = simpleTasks.get(i);
        }

    }

}

