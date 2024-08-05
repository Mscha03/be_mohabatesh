package com.example.myapplication;

import static com.example.myapplication.interfaces.drawerNavigation.drawerNavigationHandler;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class ContactUsActivity extends AppCompatActivity {

    Button phone, telegram, eitaa, email;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    ImageButton drawerMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_us);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        phone = findViewById(R.id.phone_btn);
        telegram = findViewById(R.id.telegram_btn);
        eitaa = findViewById(R.id.eitaa_btn);
        email = findViewById(R.id.email_btn);

        phone.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+989157476168"));
            startActivity(intent);
        });

        telegram.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/mscha03"));
            startActivity(intent);
        });

        eitaa.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://eitaa.com/mscha03"));
            startActivity(intent);
        });

        email.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"m.saleh.sedeghi@gmail.com"});
            startActivity(emailIntent);
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

            drawerNavigationHandler(itemId, ContactUsActivity.this, R.id.main_contact_us, drawerLayout);

            return false;
        });

    }
}