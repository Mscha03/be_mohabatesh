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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ali.uneversaldatetools.date.JalaliDateTime;
import com.example.myapplication.changer.BoolInt;
import com.example.myapplication.database.TaskDB;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.model.TaskModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;

public class NormalTaskActivity extends AppCompatActivity {

    private static final String TAG = "NormalTaskActivity";

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    ImageButton drawerMenu;

    BottomNavigationView bottomNavigationView;
    ActivityMainBinding binding;

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

            drawerNavigationHandler(
                    item.getItemId(), NormalTaskActivity.this, R.id.main_nav_normal_task, drawerLayout );

            return false;
        });



        // fragment
        bottomNavigationView = findViewById(R.id.normal_task_navigation);
        // Set the initial fragment
        if (savedInstanceState == null) {
            loadFragment(new NormalTaskTodayFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
                    Fragment selectedFragment = null;
                    int itemId = item.getItemId();
                    if (itemId == R.id.normal_bottom_today) {
                        selectedFragment = new NormalTaskTodayFragment();
                        Bundle bundle = new Bundle();
                    } else if (itemId == R.id.normal_bottom_future) {
                        selectedFragment = new NormalTaskFutureFragment();
                    } else if (itemId == R.id.normal_bottom_past) {
                        selectedFragment = new NormalTaskPastFragment();
                    }
                    return loadFragment(selectedFragment);
                }
        );


        //fab
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Log.d(TAG, "onClick: FAB clicked, navigating to AddPeriodTask");
            Intent intent = new Intent(this, AddNormalTask.class);
            startActivity(intent);
            this.finish();
        });

    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.normal_frame_layout, fragment);
            transaction.commit();
            return true;
        }
        return false;
    }
}