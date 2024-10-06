package com.example.myapplication;

import static com.example.myapplication.interfaces.drawerNavigation.drawerNavigationHandler;
import static com.example.myapplication.settings.LanguageKt.getLanguageFromPreferences;
import static com.example.myapplication.settings.LanguageKt.setLocale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

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
import com.example.myapplication.bottomsheet.MainBottomSheet;
import com.example.myapplication.database.GetUndoneTask;
import com.example.myapplication.database.TaskDataBase.HabitDB;
import com.example.myapplication.database.TaskDataBase.SimpleTaskDB;
import com.example.myapplication.database.TaskDataBase.DeadLinedTaskDB;
import com.example.myapplication.recadapter.PeriodAdapter;
import com.example.myapplication.recadapter.SimpleAdapter;
import com.example.myapplication.recadapter.TaskAdapter;
import com.example.myapplication.time.ShamsiName;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    TextView today;
    TextView appName;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    ImageButton drawerMenu;

    RecyclerView timedRecyclerView, periodicRecyclerView, simpleRecyclerView;
    DeadLinedTaskDB deadLinedTaskDB;
    HabitDB habitDB;
    SimpleTaskDB simpleTaskDB;


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

        //set app name
        appName = findViewById(R.id.app_name_text_view);
        appName.setText(getString(R.string.application_title));

        //date
        today = findViewById(R.id.main_date_text_view);
        JalaliDateTime jalaliDateTime = JalaliDateTime.Now();
        String date =
                    ShamsiName.getDayName((jalaliDateTime.getDays() + 5) % 7, this) + " " +
                        jalaliDateTime.getDay() + " " +
                        ShamsiName.getMonthName(jalaliDateTime.getMonth(),this) + " " +
                        jalaliDateTime.getYear();
        today.setText(date);

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
        drawerToggle.syncState();
        navigationView.bringToFront();

        // load undone work from data base
        deadLinedTaskDB = new DeadLinedTaskDB(this);
        timedRecyclerView = findViewById(R.id.timed_activities_recycler);
        TaskAdapter timedAdapter = new TaskAdapter(GetUndoneTask.todayTasks(this), deadLinedTaskDB);
        timedRecyclerView.setHasFixedSize(true);
        timedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        timedRecyclerView.setAdapter(timedAdapter);
        Log.d(TAG, "onViewCreated: timed tasks recycler view set up");

        habitDB = new HabitDB(this);
        periodicRecyclerView = findViewById(R.id.periodic_activities_recycler);
        PeriodAdapter periodAdapter = new PeriodAdapter(GetUndoneTask.allRoutineTasks(this), habitDB);
        periodicRecyclerView.setHasFixedSize(true);
        periodicRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        periodicRecyclerView.setAdapter(periodAdapter);
        Log.d(TAG, "onViewCreated: timed tasks recycler view set up");

        simpleTaskDB = new SimpleTaskDB(this);
        simpleRecyclerView = findViewById(R.id.simple_activities_recycler);
        SimpleAdapter simpleAdapter = new SimpleAdapter(GetUndoneTask.simpleTasks(this), simpleTaskDB);
        simpleRecyclerView.setHasFixedSize(true);
        simpleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        simpleRecyclerView.setAdapter(simpleAdapter);
        Log.d(TAG, "onViewCreated: timed tasks recycler view set up");


        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    if (isEnabled()) {
                        setEnabled(false);
                        handleOnBackPressed();
                    }
                }
            }
        });


        navigationView.setNavigationItemSelectedListener(item -> {

            int itemId = item.getItemId();

            drawerNavigationHandler(itemId, MainActivity.this, R.id.main_nav_home, drawerLayout);

            return false;
        });

        //fab
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            MainBottomSheet mainBottomSheet = new MainBottomSheet();
            mainBottomSheet.show(getSupportFragmentManager(), "mainBottomSheet");
            Log.d(TAG, "onClick: showing TaskListBottomSheet C");
        });


    }

}
