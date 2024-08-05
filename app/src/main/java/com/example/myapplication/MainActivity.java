package com.example.myapplication;

import static com.example.myapplication.interfaces.drawerNavigation.drawerNavigationHandler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

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
import com.example.myapplication.database.GetUndoneTask;
import com.example.myapplication.database.RoutineDB;
import com.example.myapplication.database.TaskDB;
import com.example.myapplication.recadapter.PeriodAdapter;
import com.example.myapplication.recadapter.TaskAdapter;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    ImageButton drawerMenu;

    RecyclerView timedRecyclerView, periodicRecyclerView;
    TaskDB taskDB;
    RoutineDB routineDB;


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

        //navigation button
        drawerMenu = findViewById(R.id.main_nav_drawer_button);
        drawerMenu.setOnClickListener(v -> {
            drawerLayout.open();
        });

        //navigation drawer
        drawerLayout = findViewById(R.id.main_nav_drawer);
        navigationView = findViewById(R.id.main_nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.main_nav_open, R.string.main_nav_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();;
        navigationView.bringToFront();

        //
        taskDB = new TaskDB(this);
        timedRecyclerView = findViewById(R.id.timed_activities_recycler);
        TaskAdapter timedAdapter = new TaskAdapter(GetUndoneTask.todayTasks(this), taskDB);
        timedRecyclerView.setHasFixedSize(true);
        timedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        timedRecyclerView.setAdapter(timedAdapter);
        Log.d(TAG, "onViewCreated: timed tasks recycler view set up");

        routineDB = new RoutineDB(this);
        periodicRecyclerView = findViewById(R.id.periodic_activities_recycler);
        PeriodAdapter periodAdapter = new PeriodAdapter(GetUndoneTask.allRoutineTasks(this), routineDB);
        periodicRecyclerView.setHasFixedSize(true);
        periodicRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        periodicRecyclerView.setAdapter(periodAdapter);
        Log.d(TAG, "onViewCreated: timed tasks recycler view set up");






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

            drawerNavigationHandler(itemId, MainActivity.this, R.id.main_nav_home, drawerLayout);

            return false;
        });


    }

}
