package com.example.myapplication;

import static com.example.myapplication.interfaces.drawerNavigation.drawerNavigationHandler;

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

import com.example.myapplication.database.GetAllTask;
import com.example.myapplication.database.RoutineDB;
import com.example.myapplication.database.TaskDB;
import com.example.myapplication.recadapter.PeriodAdapter;
import com.example.myapplication.model.PeriodicModel;
import com.example.myapplication.recadapter.TaskAdapter;
import com.example.myapplication.time.PeriodicCheckBoxReset;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;



public class PeriodicTasksActivity extends AppCompatActivity {

    private static final String TAG = "PeriodicTaskActivity";

    public static RoutineDB db;

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

            drawerNavigationHandler(
                    item.getItemId(), PeriodicTasksActivity.this, R.id.main_nav_periodic_task, drawerLayout );

            return false;
        });



        RecyclerView dailyRecyclerView = findViewById(R.id.today_recycler_view);
        PeriodAdapter dailyAdapter = new PeriodAdapter(GetAllTask.dailyTasks(this), db);
        dailyRecyclerView.setHasFixedSize(true);
        dailyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dailyRecyclerView.setAdapter(dailyAdapter);
        Log.d(TAG, "onCreate: daily tasks recycler view set up");

        RecyclerView weeklyRecyclerView = findViewById(R.id.this_week_recycler_view);
        PeriodAdapter weeklyAdapter = new PeriodAdapter(GetAllTask.weeklyTasks(this), db);
        weeklyRecyclerView.setHasFixedSize(true);
        weeklyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        weeklyRecyclerView.setAdapter(weeklyAdapter);
        Log.d(TAG, "onCreate: weekly tasks recycler view set up");

        RecyclerView monthlyRecyclerView = findViewById(R.id.this_month_recycler_view);
        PeriodAdapter monthlyAdapter = new PeriodAdapter(GetAllTask.monthlyTasks(this), db);
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
