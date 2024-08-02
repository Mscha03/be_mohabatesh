package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.database.RoutineDB;
import com.example.myapplication.recadapter.PeriodAdapter;
import com.example.myapplication.model.PeriodicModel;
import com.example.myapplication.time.PeriodicCheckBoxReset;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;






public class PeriodicTasksActivity extends AppCompatActivity {

    private static final String TAG = "PeriodicTaskActivity";

    public static RoutineDB db;
    ArrayList<PeriodicModel> dailyTasks;
    ArrayList<PeriodicModel> weeklyTasks;
    ArrayList<PeriodicModel> monthlyTasks;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    ImageButton drawerMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_periodic_tasks);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.periodic_task_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //navigation button
        drawerMenu = findViewById(R.id.periodic_task_nav_drawer_button);
        drawerMenu.setOnClickListener(v -> {
            drawerLayout.open();
        });

        //navigation drawer
        drawerLayout = findViewById(R.id.periodic_task_nav_drawer);
        navigationView = findViewById(R.id.periodic_task_nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.main_nav_open, R.string.main_nav_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();;
        navigationView.bringToFront();

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    if (isEnabled()) {
                        setEnabled(false);
                        handleOnBackPressed();
                    }                }
            }
        });


        navigationView.setNavigationItemSelectedListener(item -> {

            int itemId =  item.getItemId();

            if (itemId == R.id.main_nav_home){
                Intent intent = new Intent(PeriodicTasksActivity.this, MainActivity.class);
                startActivity(intent);

            } else if (itemId == R.id.main_nav_periodic_task) {
                drawerLayout.closeDrawer(GravityCompat.START);

            } else if (itemId == R.id.main_nav_normal_task) {
                Intent intent = new Intent(PeriodicTasksActivity.this, NormalTaskActivity.class);
                startActivity(intent);

            } else if (itemId == R.id.main_nav_history) {
                Intent intent = new Intent(PeriodicTasksActivity.this, HistoryActivity.class);
                startActivity(intent);

            }else if (itemId == R.id.main_nav_about_us) {
                Intent intent = new Intent(PeriodicTasksActivity.this, AboutUsActivity.class);
                startActivity(intent);

            } else if (itemId == R.id.main_contact_us) {
                Intent intent = new Intent(PeriodicTasksActivity.this, ContactUsActivity.class);
                startActivity(intent);

            }
            return false;
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
        PeriodAdapter dailyAdapter = new PeriodAdapter(dailyModels, db);
        dailyRecyclerView.setHasFixedSize(true);
        dailyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dailyRecyclerView.setAdapter(dailyAdapter);
        Log.d(TAG, "onCreate: daily tasks recycler view set up");

        RecyclerView weeklyRecyclerView = findViewById(R.id.this_week_recycler_view);
        PeriodAdapter weeklyAdapter = new PeriodAdapter(weeklyModels, db);
        weeklyRecyclerView.setHasFixedSize(true);
        weeklyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        weeklyRecyclerView.setAdapter(weeklyAdapter);
        Log.d(TAG, "onCreate: weekly tasks recycler view set up");

        RecyclerView monthlyRecyclerView = findViewById(R.id.this_month_recycler_view);
        PeriodAdapter monthlyAdapter = new PeriodAdapter(monthlyModels, db);
        monthlyRecyclerView.setHasFixedSize(true);
        monthlyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        monthlyRecyclerView.setAdapter(monthlyAdapter);
        Log.d(TAG, "onCreate: monthly tasks recycler view set up");




        //fab
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Log.d(TAG, "onClick: FAB clicked, navigating to AddPeriodTask");
            Intent intent = new Intent(this, AddPeriodTask.class);
            startActivity(intent);
            this.finish();
        });

    }

}
