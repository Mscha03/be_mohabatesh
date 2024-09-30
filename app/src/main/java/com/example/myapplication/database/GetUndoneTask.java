package com.example.myapplication.database;

import static com.example.myapplication.changer.BoolInt.intToBool;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.CheckBox;

import com.ali.uneversaldatetools.date.JalaliDateTime;
import com.example.myapplication.model.Period;
import com.example.myapplication.model.tasks.DeadLinedTask;
import com.example.myapplication.model.tasks.Habit;
import com.example.myapplication.model.tasks.SimpleTask;
import com.example.myapplication.time.PeriodicCheckBoxReset;
import com.example.myapplication.time.WithWeekJalaliDateTime;

import java.util.ArrayList;
import java.util.Calendar;

public class GetUndoneTask {

    private static final String TAG = "GetAllTask";

    static TaskDB db;
    static ArrayList<DeadLinedTask> today;
    static ArrayList<DeadLinedTask> future;
    static ArrayList<DeadLinedTask> past;
    static DeadLinedTask[] todayModels;
    static DeadLinedTask[] futureModels;
    static DeadLinedTask[] pastModels;

    // periodic task
    static RoutineDB routineDB;
    static ArrayList<Habit> dailyTasks;
    static ArrayList<Habit> weeklyTasks;
    static ArrayList<Habit> monthlyTasks;
    static Habit[] dailyModels;
    static Habit[] weeklyModels;
    static Habit[] monthlyModels;
    static Habit[] allRoutineModels;

    //Simple task
    static SimpleDB simpleDB;
    static ArrayList<SimpleTask> simpleTasksList;
    static SimpleTask[] simpleTasksArray;

    // public method
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

    public static Habit[] allRoutineTasks(Context context) {
        getPeriodicTasks(context);
        allRoutineModels = new Habit[dailyModels.length + weeklyModels.length + monthlyModels.length];

        for (int i = 0; i < dailyModels.length + weeklyModels.length + monthlyModels.length; i++) {
            if (i < dailyModels.length) {
                for (Habit dailyModel : dailyModels) {
                    allRoutineModels[i] = dailyModel;
                }
            } else if (i < dailyModels.length + weeklyModels.length) {
                for (Habit weeklyModel : weeklyModels) {
                    allRoutineModels[i] = weeklyModel;
                }
            } else {
                for (Habit monthlyModel : monthlyModels) {
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

    public static SimpleTask[] simpleTasks(Context context) {
        getSimpleTasks(context);
        return simpleTasksArray;
    }


    // private method
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
                checkBox.setChecked(intToBool(cursor.getInt(cursor.getColumnIndexOrThrow("isdone"))));

                DeadLinedTask taskModel = new DeadLinedTask(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("isdone")),
                        new JalaliDateTime(
                                cursor.getInt(cursor.getColumnIndexOrThrow("deadyear")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("deadmonth")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("deadday")))
                );

                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.SATURDAY);
                JalaliDateTime jalaliDateTime = JalaliDateTime.Now();

                int thisDay = jalaliDateTime.getDay();
                int thisMonth = jalaliDateTime.getMonth();
                int thisYear = jalaliDateTime.getYear();
                Log.d(TAG, "onClick: current date - day: " + today + ", month: " + thisMonth + ", year: " + thisYear);

                int deadDay = taskModel.getDeadDate().getDay();
                int deadMonth = taskModel.getDeadDate().getMonth();
                int deadYear = taskModel.getDeadDate().getYear();
                Log.d(TAG, "onClick: deadline date - day: " + deadDay + ", month: " + deadMonth + ", year: " + deadYear);

                if (!intToBool(taskModel.getIsDone())) {
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
        routineDB = new RoutineDB(context);
        Log.d(TAG, "onCreate: database initialized");
        dailyTasks = new ArrayList<>();
        weeklyTasks = new ArrayList<>();
        monthlyTasks = new ArrayList<>();

        Cursor cursor = routineDB.getAllRecords(RoutineDB.ROUTINE_TABLE_NAME);
        if (cursor.moveToFirst()) {
            Log.d(TAG, "onCreate: fetching tasks from database");

            do {

                JalaliDateTime jalaliDateTime = JalaliDateTime.Now();
                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.SATURDAY);

                int day = jalaliDateTime.getDay();
                int week = calendar.get(Calendar.WEEK_OF_YEAR);
                int month = jalaliDateTime.getMonth();
                int year = jalaliDateTime.getYear();



                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String dbPeriod = cursor.getString(cursor.getColumnIndexOrThrow("period"));

                Period period = null;
                int isDone = 0;
                switch (dbPeriod) {
                    case "daily":
                        week = 0;
                        isDone = PeriodicCheckBoxReset.checkDay(id, day, week, month,year, context);
                        period = Period.daily;
                        break;
                    case "weekly":
                        day = 0;
                        month = 0;
                        isDone = PeriodicCheckBoxReset.checkDay(id, day, week, month,year, context);
                        period = Period.weekly;
                        break;
                    case "monthly":
                        day = 0;
                        week = 0;
                        isDone = PeriodicCheckBoxReset.checkDay(id, day, week, month,year, context);
                        period = Period.monthly;
                        break;
                    default:
                        break;
                }

                Habit periodicModel = new Habit(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        isDone,
                        new WithWeekJalaliDateTime(
                                cursor.getInt(cursor.getColumnIndexOrThrow("year")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("month")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("week")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("day"))),
                        period);

                Log.d(TAG, "onCreate: periodicModel created:" +
                        " title: " + periodicModel.getTitle() +
                        " descreption: " + periodicModel.getDescription() +
                        " period: " + periodicModel.getPeriod().toString() +
                        " id: " + periodicModel.getId() +
                        " day: " + periodicModel.getCreateDate().getDay() +
                        " month: " + periodicModel.getCreateDate().getMonth() +
                        " week: " + periodicModel.getCreateDate().getWeek() +
                        " year" + periodicModel.getCreateDate().getYear());

                if (periodicModel.getIsDone() == 0) {
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
        simpleDB = new SimpleDB(context);
        Log.d(TAG, "onCreate: database initialized");
        simpleTasksList = new ArrayList<>();


        Cursor cursor = simpleDB.getAllRecords();
        if (cursor.moveToFirst()) {
            Log.d(TAG, "onCreate: fetching tasks from database");

            do {

                SimpleTask simpleModel = new SimpleTask(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("isdone"))
                );

                Log.d(TAG, "onCreate: periodicModel created:" +
                        " title: " + simpleModel.getTitle() +
                        " isDone: " + simpleModel.getIsDone() +
                        " descreption: " + simpleModel.getDescription() +
                        " id: " + simpleModel.getId());

                if (simpleModel.getIsDone() == 0) {
                    simpleTasksList.add(simpleModel);
                }

            } while (cursor.moveToNext());
            Log.d(TAG, "onCreate: tasks loaded from database");
        } else {
            Log.d(TAG, "onCreate: no tasks found in database");
        }
        cursor.close();

        // List to simpleArray
        simpleTasksArray = new SimpleTask[simpleTasksList.size()];
        for (int i = 0; i < simpleTasksList.size(); i++) {
            simpleTasksArray[i] = simpleTasksList.get(i);
        }

    }

}

