package com.example.myapplication.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.CheckBox;

import com.ali.uneversaldatetools.date.JalaliDateTime;
import com.example.myapplication.changer.BoolInt;
import com.example.myapplication.customwidget.MultiStateCheckBox;
import com.example.myapplication.model.Period;
import com.example.myapplication.model.PeriodicModel;
import com.example.myapplication.model.TaskModel;
import com.example.myapplication.model.tasks.SimpleTask;
import com.example.myapplication.time.PeriodicCheckBoxReset;

import java.util.ArrayList;
import java.util.Calendar;

public class GetAllTask implements AddInformationForHistory{

    private static final String TAG = "GetAllTask";

    // normal task
    static TaskDB taskDB;
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

    // simple task
    static SimpleDB simpleDB;
    static ArrayList<SimpleTask> simpleTasksList;
    static SimpleTask[] simpleTaskArray;

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

    public static SimpleTask[] simpleTasks(Context context) {
        getSimpleTasks(context);
        return simpleTaskArray;
    }


    private static void getNormalTasks(Context context) {
        taskDB = new TaskDB(context);
        today = new ArrayList<>();
        future = new ArrayList<>();
        past = new ArrayList<>();

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

        Cursor cursor = routineDB.getAllRecords(RoutineDB.ROUTINE_TABLE_NAME);

        if (cursor.moveToFirst()) {
            Log.d(TAG, "onCreate: fetching tasks from database");

            do {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String dbPeriod = cursor.getString(cursor.getColumnIndexOrThrow("period"));
                int dbYear = cursor.getInt(cursor.getColumnIndexOrThrow("year"));

                JalaliDateTime jalaliDateTime = JalaliDateTime.Now();
                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.SATURDAY);

                int day = jalaliDateTime.getDay();
                int week = calendar.get(Calendar.WEEK_OF_YEAR);
                int month = jalaliDateTime.getMonth();
                int year = jalaliDateTime.getYear();

                if (dbYear != year){
                    AddInformationForHistory.addDays(dbPeriod, id, routineDB);
                }


                MultiStateCheckBox checkBox = new MultiStateCheckBox(context);
                checkBox.setText(title);

                switch (dbPeriod) {
                    case "daily":
                        week = 0;
                        checkBox.setState(PeriodicCheckBoxReset.checkDay(id, day, week, month,year, context));
                        dailyTasks.add(new PeriodicModel(checkBox, description, Period.daily, id, day, week, month, year));
                        break;

                    case "weekly":
                        day = 0;
                        month = 0;
                        checkBox.setState(PeriodicCheckBoxReset.checkDay(id, day, week, month,year, context));
                        weeklyTasks.add(new PeriodicModel(checkBox, description, Period.weekly, id, day, week, month, year));
                        break;

                    case "monthly":
                        day = 0;
                        week = 0;
                        checkBox.setState(PeriodicCheckBoxReset.checkDay(id, day, week, month,year, context));
                        monthlyTasks.add(new PeriodicModel(checkBox, description, Period.monthly, id, day, week, month, year));
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
        simpleTasksList = new ArrayList<>();

        Cursor cursor = simpleDB.getAllRecords();
        if (cursor.moveToFirst()) {
            Log.d(TAG, "onCreate: fetching tasks from database");

            do {

                SimpleTask simpleTask = new SimpleTask(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        (cursor.getInt(cursor.getColumnIndexOrThrow("isdone")))
                );

                simpleTasksList.add(simpleTask);

            } while (cursor.moveToNext());
            Log.d(TAG, "onCreate: tasks loaded from database");
        } else {
            Log.d(TAG, "onCreate: no tasks found in database");
        }
        cursor.close();

        simpleTaskArray = new SimpleTask[simpleTasksList.size()];

        for (int i = 0; i < simpleTasksList.size(); i++) {
            simpleTaskArray[i] = simpleTasksList.get(i);
        }

    }

}

