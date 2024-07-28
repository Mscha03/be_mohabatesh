package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.database.RoutineDB;
import com.example.myapplication.recadapter.DailyAdapter;
import com.example.myapplication.recyclerview.PeriodicModel;
import com.example.myapplication.time.CheckBoxReset;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static RoutineDB db;
    ArrayList<PeriodicModel> dailyTasks;
    ArrayList<PeriodicModel> weeklyTasks;
    ArrayList<PeriodicModel> monthlyTasks;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: started");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //database
        db = new RoutineDB(this);
        Log.d(TAG, "onCreate: database initialized");
        dailyTasks = new ArrayList<>();
        weeklyTasks = new ArrayList<>();
        monthlyTasks = new ArrayList<>();

        Cursor cursor = db.getAllRecords();
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

                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(title);
                checkBox.setChecked(CheckBoxReset.checkDay(isDone, dbChangeDay, dbChangeWeek, dbChangeMonth, dbPeriod));

                switch (dbPeriod){
                    case "daily":
                    case "روزانه":
                        dailyTasks.add(new PeriodicModel(checkBox, description, Period.daily, id, dbChangeDay, dbChangeWeek, dbChangeMonth));
                        break;

                    case "weekly":
                    case "هفتگی":
                        weeklyTasks.add(new PeriodicModel(checkBox, description, Period.weekly, id, dbChangeDay, dbChangeWeek, dbChangeMonth));
                        break;
                    case "monthly":
                    case "ماهانه":
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

        PeriodicModel[] dailyModels = new PeriodicModel[dailyTasks.size()];
        PeriodicModel[] weeklyModels = new PeriodicModel[weeklyTasks.size()];
        PeriodicModel[] monthlyModels = new PeriodicModel[monthlyTasks.size()];

        for (int i = 0; i < dailyTasks.size(); i++) {
            dailyModels[i] = dailyTasks.get(i);
        }
        for (int i = 0; i < weeklyTasks.size(); i++) {
            weeklyModels[i] = weeklyTasks.get(i);
        }
        for (int i = 0; i < monthlyTasks.size(); i++) {
            monthlyModels[i] = monthlyTasks.get(i);
        }

        RecyclerView dailyRecyclerView = findViewById(R.id.today_recycler_view);
        DailyAdapter dailyAdapter = new DailyAdapter(dailyModels, db);
        dailyRecyclerView.setHasFixedSize(true);
        dailyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dailyRecyclerView.setAdapter(dailyAdapter);
        Log.d(TAG, "onCreate: daily tasks recycler view set up");

        RecyclerView weeklyRecyclerView = findViewById(R.id.this_week_recycler_view);
        DailyAdapter weeklyAdapter = new DailyAdapter(weeklyModels, db);
        weeklyRecyclerView.setHasFixedSize(true);
        weeklyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        weeklyRecyclerView.setAdapter(weeklyAdapter);
        Log.d(TAG, "onCreate: weekly tasks recycler view set up");

        RecyclerView monthlyRecyclerView = findViewById(R.id.this_month_recycler_view);
        DailyAdapter monthlyAdapter = new DailyAdapter(monthlyModels, db);
        monthlyRecyclerView.setHasFixedSize(true);
        monthlyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        monthlyRecyclerView.setAdapter(monthlyAdapter);
        Log.d(TAG, "onCreate: monthly tasks recycler view set up");

        //fab
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Log.d(TAG, "onClick: FAB clicked, navigating to AddTask");
            Intent intent = new Intent(this, AddTask.class);
            startActivity(intent);
            this.finish();
        });
    }

}
