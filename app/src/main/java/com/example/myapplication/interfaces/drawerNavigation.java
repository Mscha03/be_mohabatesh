package com.example.myapplication.interfaces;

import android.content.Context;
import android.content.Intent;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.myapplication.AboutUsActivity;
import com.example.myapplication.ContactUsActivity;
import com.example.myapplication.HistoryActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.NormalTaskActivity;
import com.example.myapplication.PeriodicTasksActivity;
import com.example.myapplication.R;
import com.example.myapplication.SettingActivity;
import com.example.myapplication.SimpleTaskActivity;

public interface drawerNavigation {
    static void drawerNavigationHandler(int itemId, Context packageContext, int viewId, DrawerLayout drawerLayout) {
        if (itemId == viewId) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else if (itemId == R.id.main_nav_home) {
            Intent intent = new Intent(packageContext, MainActivity.class);
            packageContext.startActivity(intent);

        } else if (itemId == R.id.main_nav_periodic_task) {
            Intent intent = new Intent(packageContext, PeriodicTasksActivity.class);
            packageContext.startActivity(intent);

        } else if (itemId == R.id.main_nav_normal_task) {
            Intent intent = new Intent(packageContext, NormalTaskActivity.class);
            packageContext.startActivity(intent);

        } else if (itemId == R.id.main_nav_simple_task) {
            Intent intent = new Intent(packageContext, SimpleTaskActivity.class);
            packageContext.startActivity(intent);
        } else if (itemId == R.id.main_nav_history) {
            Intent intent = new Intent(packageContext, HistoryActivity.class);
            packageContext.startActivity(intent);

        } else if (itemId == R.id.main_nav_about_us) {
            Intent intent = new Intent(packageContext, AboutUsActivity.class);
            packageContext.startActivity(intent);

        } else if (itemId == R.id.main_contact_us) {
            Intent intent = new Intent(packageContext, ContactUsActivity.class);
            packageContext.startActivity(intent);

        } else if (itemId == R.id.main_nav_settings) {
            Intent intent = new Intent(packageContext, SettingActivity.class);
            packageContext.startActivity(intent);
        }
    }
}
