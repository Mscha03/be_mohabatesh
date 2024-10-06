package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myapplication.interfaces.drawerNavigation
import com.example.myapplication.settings.getLanguageCode
import com.example.myapplication.settings.saveLanguageToPreferences
import com.example.myapplication.settings.setLocale
import com.google.android.material.navigation.NavigationView

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_setting)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val appName = findViewById<TextView>(R.id.app_name_text_view)
        val drawerLayout = findViewById<DrawerLayout>(R.id.setting_nav_drawer)
        val navigationView = findViewById<NavigationView>(R.id.setting_nav_view)
        val drawerMenu = findViewById<ImageButton>(R.id.setting_nav_drawer_button)


        //set app name
        appName.text = getText(R.string.application_title)

        //navigation button
        drawerMenu.setOnClickListener {
            drawerLayout.open()
        }

        val drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.main_nav_open,
            R.string.main_nav_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        navigationView.bringToFront()
        val composeView = findViewById<ComposeView>(R.id.composeView)

        composeView.setContent {
            // Compose code here
            Row(modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x223F51B5))){

                LanguageItem()

            }

        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    if (isEnabled) {
                        isEnabled = false
                        handleOnBackPressed()
                    }
                }
            }
        })


        navigationView.setNavigationItemSelectedListener { item: MenuItem ->
            val itemId = item.itemId
            drawerNavigation.drawerNavigationHandler(
                itemId,
                this,
                R.id.main_nav_settings,
                drawerLayout
            )
            false
        }

    }

    @Preview(showBackground = true)
    @Composable
    fun ShowItem() {
        LanguageItem()
    }

    @Composable
    fun LanguageItem() {
        val context = LocalContext.current
        var expanded by remember { mutableStateOf(false) }
        var selectedText by remember { mutableStateOf("Select an item") }
        val items = listOf("English", "فارسی")

        Row(
            Modifier
                .clickable { expanded = true }
                .fillMaxWidth()
                .padding(5.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x223F51B5))

            ){
            Text(
                text = "${getString(R.string.setting_item_language)}: " +
                        getString(R.string.app_language),
                modifier = Modifier
                    .padding(20.dp),
                fontSize = 20.sp)

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp)),
                        text = { Text(text = item) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            setLocale(context = context, language = getLanguageCode(item))
                            saveLanguageToPreferences(this@SettingActivity, getLanguageCode(item))
                            startActivity(Intent(this@SettingActivity, SettingActivity::class.java))
                        }
                    )
                }
            }

        }
    }




}




