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

import com.ali.uneversaldatetools.date.JalaliDateTime;
import com.example.myapplication.changer.BoolInt;
import com.example.myapplication.database.RoutineDB;
import com.example.myapplication.database.TaskDB;
import com.example.myapplication.model.PeriodicModel;
import com.example.myapplication.model.TaskModel;
import com.example.myapplication.recadapter.PeriodAdapter;
import com.example.myapplication.recadapter.TaskAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;

public class NormalTaskActivity extends AppCompatActivity {

    private static final String TAG = "NormalTaskActivity";

    public static TaskDB db;
    ArrayList<TaskModel> todayTasks;
    ArrayList<TaskModel> futureTasks;
    ArrayList<TaskModel> pastTasks;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    ImageButton drawerMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_normal_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.normal_task_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //navigation button
        drawerMenu = findViewById(R.id.normal_task_nav_drawer_button);
        drawerMenu.setOnClickListener(v -> {
            drawerLayout.open();
        });

        //navigation drawer
        drawerLayout = findViewById(R.id.normal_task_nav_drawer);
        navigationView = findViewById(R.id.normal_task_nav_view);
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
                Intent intent = new Intent(NormalTaskActivity.this, MainActivity.class);
                startActivity(intent);

            } else if (itemId == R.id.main_nav_periodic_task) {
                Intent intent = new Intent(NormalTaskActivity.this, PeriodicTasksActivity.class);
                startActivity(intent);

            } else if (itemId == R.id.main_nav_normal_task) {
                drawerLayout.closeDrawer(GravityCompat.START);

            } else if (itemId == R.id.main_nav_history) {
                Intent intent = new Intent(NormalTaskActivity.this, HistoryActivity.class);
                startActivity(intent);

            }else if (itemId == R.id.main_nav_about_us) {
                Intent intent = new Intent(NormalTaskActivity.this, AboutUsActivity.class);
                startActivity(intent);

            } else if (itemId == R.id.main_contact_us) {
                Intent intent = new Intent(NormalTaskActivity.this, ContactUsActivity.class);
                startActivity(intent);

            }
            return false;
        });








        //database
        db = new TaskDB(this);
        Log.d(TAG, "onCreate: database initialized");
        todayTasks = new ArrayList<>();
        futureTasks = new ArrayList<>();
        pastTasks = new ArrayList<>();

        Cursor cursor = db.getAllRecords();
        if (cursor.moveToFirst()) {
            Log.d(TAG, "onCreate: fetching tasks from database");

            do {



                CheckBox checkBox = new CheckBox(this);
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

                int today = jalaliDateTime.getDay();
                int thisMonth = jalaliDateTime.getMonth();
                int thisYear = jalaliDateTime.getYear();
                Log.d(TAG, "onClick: current date - day: " + today +  ", month: " + thisMonth);

                int deadDay = taskModel.getDeadDay();
                int deadMonth = taskModel.getDeadMonth();
                int deadYear = taskModel.getDeadYear();
                Log.d(TAG, "onClick: deadline date - day: " + deadDay +  ", month: " + deadMonth);



                if ((deadDay == today) && (deadMonth == thisMonth) && (deadYear == thisYear)){
                    todayTasks.add(taskModel);
                } else if ( (deadYear > thisYear) || ((deadYear == thisYear) && (deadMonth > thisMonth)) || ((deadYear == thisYear) && (deadMonth == thisMonth) && (deadDay > today))) {
                    futureTasks.add(taskModel);
                } else {
                    pastTasks.add(taskModel);
                }

            }while (cursor.moveToNext());
            Log.d(TAG, "noCreate: tasks loaded from database");

        } else {
            Log.d(TAG, "onCreate: no tasks found in database");
        }
        cursor.close();

        TaskModel[] todayModels = new TaskModel[todayTasks.size()];
        TaskModel[] futureModels = new TaskModel[futureTasks.size()];
        TaskModel[] pastModels = new TaskModel[pastTasks.size()];

        for (int i = 0; i < todayTasks.size(); i++) {
            todayModels[i] = todayTasks.get(i);
        }
        for (int i = 0; i < futureTasks.size(); i++) {
            futureModels[i] = futureTasks.get(i);
        }
        for (int i = 0; i < pastTasks.size(); i++) {
            pastModels[i] = pastTasks.get(i);
        }

        RecyclerView todayRecyclerView = findViewById(R.id.today_recycler_view);
        TaskAdapter todayAdapter = new TaskAdapter(todayModels, db);
        todayRecyclerView.setHasFixedSize(true);
        todayRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todayRecyclerView.setAdapter(todayAdapter);
        Log.d(TAG, "onCreate: today tasks recycler view set up");

        RecyclerView futureRecyclerView = findViewById(R.id.today_recycler_view);
        TaskAdapter futureAdapter = new TaskAdapter(todayModels, db);
        futureRecyclerView.setHasFixedSize(true);
        futureRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        futureRecyclerView.setAdapter(futureAdapter);
        Log.d(TAG, "onCreate: future tasks recycler view set up");

        RecyclerView pastRecyclerView = findViewById(R.id.today_recycler_view);
        TaskAdapter pastAdapter = new TaskAdapter(todayModels, db);
        pastRecyclerView.setHasFixedSize(true);
        pastRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pastRecyclerView.setAdapter(pastAdapter);
        Log.d(TAG, "onCreate: past tasks recycler view set up");


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