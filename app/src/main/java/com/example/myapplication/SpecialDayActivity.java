package com.example.myapplication;

import static com.example.myapplication.interfaces.drawerNavigation.drawerNavigationHandler;

import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class SpecialDayActivity extends AppCompatActivity {

    private static final String TAG = "SpecialDayActivity";

    TextView appName;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    ImageButton drawerMenu;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_special_day_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.special_day_task_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        appName = findViewById(R.id.app_name_text_view);
        appName.setText(getString(R.string.application_title));

        //navigation button
        drawerMenu = findViewById(R.id.normal_task_nav_drawer_button);
        drawerMenu.setOnClickListener(v -> drawerLayout.open());

        //navigation drawer
        drawerLayout = findViewById(R.id.normal_task_nav_drawer);
        navigationView = findViewById(R.id.normal_task_nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.main_nav_open, R.string.main_nav_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
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
                    item.getItemId(), SpecialDayActivity.this, R.id.main_nav_special_day_task, drawerLayout );

            return false;
        });



        // fragment
        bottomNavigationView = findViewById(R.id.normal_task_navigation);
        bottomNavigationView.setSelectedItemId(R.id.normal_bottom_today);
        // Set the initial fragment
        if (savedInstanceState == null) {
            loadFragment(new SpecialDayTodayFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
                    Fragment selectedFragment = null;
                    int itemId = item.getItemId();
                    if (itemId == R.id.normal_bottom_today) {
                        selectedFragment = new SpecialDayTodayFragment();
                    } else if (itemId == R.id.normal_bottom_future) {
                        selectedFragment = new SpecialDayFutureFragment();
                    } else if (itemId == R.id.normal_bottom_past) {
                        selectedFragment = new SpecialDayPastFragment();
                    }
                    return loadFragment(selectedFragment);
                }
        );


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