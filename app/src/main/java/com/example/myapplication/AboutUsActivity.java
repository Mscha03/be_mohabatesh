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

import com.google.android.material.navigation.NavigationView;

public class AboutUsActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    ImageButton drawerMenu;

    TextView t1, t2, t3, t4, t5, t6, t7, t8, t9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about_us);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //navigation button
        drawerMenu = findViewById(R.id.contact_nav_drawer_button);
        drawerMenu.setOnClickListener(v -> drawerLayout.open());

        //navigation drawer
        drawerLayout = findViewById(R.id.main_nav_drawer);
        navigationView = findViewById(R.id.main_nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.main_nav_open, R.string.main_nav_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(item -> {

            int itemId = item.getItemId();

            drawerNavigationHandler(itemId, AboutUsActivity.this, R.id.main_nav_home, drawerLayout);

            return false;
        });

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


        t1 = findViewById(R.id.us_text1);
        t2 = findViewById(R.id.us_text2);
        t3 = findViewById(R.id.us_text3);
        t4 = findViewById(R.id.us_text4);
        t5 = findViewById(R.id.us_text5);
        t6 = findViewById(R.id.us_text6);
        t7 = findViewById(R.id.us_text7);
        t8 = findViewById(R.id.us_text8);
        t9 = findViewById(R.id.us_text9);

        t1.setText(getString(R.string.us_text_1));
        t2.setText(getString(R.string.us_text_2));
        t3.setText(getString(R.string.us_text_3));
        t4.setText(getString(R.string.us_text_4));
        t5.setText(getString(R.string.us_text_5));
        t6.setText(getString(R.string.us_text_6));
        t7.setText(getString(R.string.us_text_7));
        t8.setText(getString(R.string.us_text_8));
        t9.setText(getString(R.string.us_text_9));

    }
}