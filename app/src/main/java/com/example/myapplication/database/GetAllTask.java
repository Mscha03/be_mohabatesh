package com.example.myapplication.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.CheckBox;

import com.ali.uneversaldatetools.date.JalaliDateTime;
import com.example.myapplication.converter.BoolInt;
import com.example.myapplication.database.TaskDataBase.HabitDB;
import com.example.myapplication.database.TaskDataBase.SimpleTaskDB;
import com.example.myapplication.database.TaskDataBase.DeadLinedTaskDB;
import com.example.myapplication.model.Period;
import com.example.myapplication.model.tasks.DeadLinedTask;
import com.example.myapplication.model.tasks.Habit;
import com.example.myapplication.model.tasks.SimpleTask;
import com.example.myapplication.time.PeriodicCheckBoxReset;
import com.example.myapplication.time.WithWeekJalaliDateTime;

import java.util.ArrayList;
import java.util.Calendar;

public class GetAllTask implements AddInformationForHistory{

    private static final String TAG = "GetAllTask";

    // normal task
    static DeadLinedTaskDB deadLinedTaskDB;
    static ArrayList<DeadLinedTask> today;
    static ArrayList<DeadLinedTask> future;
    static ArrayList<DeadLinedTask> past;
    static DeadLinedTask[] todayModels;
    static DeadLinedTask[] futureModels;
    static DeadLinedTask[] pastModels;

    // periodic task
    static HabitDB habitDB;
    static ArrayList<Habit> dailyTasks;
    static ArrayList<Habit> weeklyTasks;
    static ArrayList<Habit> monthlyTasks;
    static Habit[] dailyModels;
    static Habit[] weeklyModels;
    static Habit[] monthlyModels;

    // simple task
    static SimpleTaskDB simpleTaskDB;
    static ArrayList<SimpleTask> simpleTasksList;
    static SimpleTask[] simpleTaskArray;

    public static DeadLinedTask[] todayTasks(Context context) {
        getNormalTasks(context);
        return todayModels;
    }

    public static DeadLinedTask[] futureTasks(Context context) {
        getNormalTasks(context);
        return futureModels;
    }

    public static DeadLinedTask[] pastTasks(Context context) {
        getNormalTasks(context);
        return pastModels;
    }


    public static Habit[] dailyTasks(Context context) {
        getPeriodicTasks(context);
        return dailyModels;
    }

    public static Habit[] weeklyTasks(Context context) {
        getPeriodicTasks(context);
        return weeklyModels;
    }

    public static Habit[] monthlyTasks(Context context) {
        getPeriodicTasks(context);
        return monthlyModels;
    }

    public static SimpleTask[] simpleTasks(Context context) {
        getSimpleTasks(context);
        return simpleTaskArray;
    }


    private static void getNormalTasks(Context context) {
        deadLinedTaskDB = new DeadLinedTaskDB(context);
        today = new ArrayList<>();
        future = new ArrayList<>();
        past = new ArrayList<>();

        Cursor cursor = deadLinedTaskDB.getAllRecords();
        if (cursor.moveToFirst()) {
            Log.d(TAG, "onCreate: fetching tasks from database");

            do {

                CheckBox checkBox = new CheckBox(context);
                checkBox.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                checkBox.setChecked(BoolInt.intToBool(cursor.getInt(cursor.getColumnIndexOrThrow("isdone"))));

                DeadLinedTask deadLinedTask = new DeadLinedTask(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("isdone")),
                        new JalaliDateTime(
                                cursor.getInt(cursor.getColumnIndexOrThrow("deadyear")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("deadmonth")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("deadday"))

                        )
                );

                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.SATURDAY);
                JalaliDateTime jalaliDateTime = JalaliDateTime.Now();

                int thisDay = jalaliDateTime.getDay();
                int thisMonth = jalaliDateTime.getMonth();
                int thisYear = jalaliDateTime.getYear();
                Log.d(TAG, "onClick: current date - day: " + today + ", month: " + thisMonth + ", year: " + thisYear);

                int deadDay = deadLinedTask.getDeadDate().getDay();
                int deadMonth = deadLinedTask.getDeadDate().getMonth();
                int deadYear = deadLinedTask.getDeadDate().getYear();
                Log.d(TAG, "onClick: deadline date - day: " + deadDay + ", month: " + deadMonth + ", year: " + deadYear);

                if ((deadDay == thisDay) && (deadMonth == thisMonth) && (deadYear == thisYear)) {
                    today.add(deadLinedTask);
                } else if ((deadYear > thisYear) || ((deadYear == thisYear) && (deadMonth > thisMonth)) || ((deadYear == thisYear) && (deadMonth == thisMonth) && (deadDay > thisDay))) {
                    future.add(deadLinedTask);
                } else {
                    past.add(deadLinedTask);
                }

            } while (cursor.moveToNext());
            Log.d(TAG, "noCreate: tasks loaded from database");

        } else {
            Log.d(TAG, "onCreate: no tasks found in database");
        }
        cursor.close();

        todayModels = new DeadLinedTask[today.size()];
        futureModels = new DeadLinedTask[future.size()];
        pastModels = new DeadLinedTask[past.size()];

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
        habitDB = new HabitDB(context);
        Log.d(TAG, "onCreate: database initialized");

        dailyTasks = new ArrayList<>();
        weeklyTasks = new ArrayList<>();
        monthlyTasks = new ArrayList<>();

        Cursor cursor = habitDB.getAllRecords(HabitDB.ROUTINE_TABLE_NAME);

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
                    AddInformationForHistory.addDays(dbPeriod, id, habitDB);
                }

                switch (dbPeriod) {
                    case "daily":
                        week = 0;
                        dailyTasks.add(
                                new Habit(
                                        id,
                                        title,
                                        description,
                                        PeriodicCheckBoxReset.checkDay(id, day, week, month,year, context),
                                        new WithWeekJalaliDateTime(year, month,week, day),
                                        Period.daily
                                        ));
                        break;

                    case "weekly":
                        day = 0;
                        month = 0;
                        dailyTasks.add(
                                new Habit(
                                        id,
                                        title,
                                        description,
                                        PeriodicCheckBoxReset.checkDay(id, day, week, month,year, context),
                                        new WithWeekJalaliDateTime(year, month,week, day),
                                        Period.weekly
                                ));      break;

                    case "monthly":
                        day = 0;
                        week = 0;
                        dailyTasks.add(
                                new Habit(
                                        id,
                                        title,
                                        description,
                                        PeriodicCheckBoxReset.checkDay(id, day, week, month,year, context),
                                        new WithWeekJalaliDateTime(year, month,week, day),
                                        Period.monthly
                                ));
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

        dailyModels = new Habit[dailyTasks.size()];
        weeklyModels = new Habit[weeklyTasks.size()];
        monthlyModels = new Habit[monthlyTasks.size()];

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
        simpleTaskDB = new SimpleTaskDB(context);
        Log.d(TAG, "onCreate: database initialized");
        simpleTasksList = new ArrayList<>();

        Cursor cursor = simpleTaskDB.getAllRecords();
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

