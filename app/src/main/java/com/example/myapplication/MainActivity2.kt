package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myapplication.bottomsheet.EditSimpleTaskUI
import com.example.myapplication.converter.BoolInt
import com.example.myapplication.database.getinformation.getUnDoneDeadLinedTask
import com.example.myapplication.database.getinformation.getUnDoneSimpleTask
import com.example.myapplication.database.getinformation.getUnDoneSpecialTask
import com.example.myapplication.database.taskDataBase.DeadLinedTaskDB
import com.example.myapplication.database.taskDataBase.SimpleTaskDB
import com.example.myapplication.database.taskDataBase.SpecialDayTaskDB
import com.example.myapplication.database.taskDataBase.habits.DailyHabitDB
import com.example.myapplication.database.taskDataBase.habits.MonthlyHabitDB
import com.example.myapplication.database.taskDataBase.habits.WeeklyHabitDB
import com.example.myapplication.interfaces.drawerNavigation
import com.example.myapplication.model.tasks.DeadLinedTask
import com.example.myapplication.model.tasks.SimpleTask
import com.example.myapplication.model.tasks.SpecialDayTask
import com.example.myapplication.model.tasks.habits.DailyHabit
import com.example.myapplication.model.tasks.habits.MonthlyHabit
import com.example.myapplication.model.tasks.habits.WeeklyHabit
import com.google.android.material.navigation.NavigationView

private var simpleTaskDB: SimpleTaskDB? = null
private var specialDayTaskDB: SpecialDayTaskDB? = null
private var deadLinedTaskDB: DeadLinedTaskDB? = null

private var dailyHabitDB: DailyHabitDB? = null
private var weeklyHabitDB: WeeklyHabitDB? = null
private var monthlyHabitDB: MonthlyHabitDB? = null


class MainActivity2 : AppCompatActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        simpleTaskDB = SimpleTaskDB(this)
        specialDayTaskDB = SpecialDayTaskDB(this)
        deadLinedTaskDB = DeadLinedTaskDB(this)

        dailyHabitDB = DailyHabitDB(this)
        weeklyHabitDB = WeeklyHabitDB(this)
        monthlyHabitDB = MonthlyHabitDB(this)

        val composeView = findViewById<ComposeView>(R.id.main_composeView)

        val appName = findViewById<TextView>(R.id.app_name_text_view)
        val drawerLayout = findViewById<DrawerLayout>(R.id.setting_nav_drawer)
        val navigationView = findViewById<NavigationView>(R.id.setting_nav_view)
        val drawerMenu = findViewById<ImageButton>(R.id.setting_nav_drawer_button)


        //set app name
        appName.text = getText(R.string.application_title)
        //navigation button
        drawerMenu.setOnClickListener { drawerLayout.open() }
        val drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.main_nav_open,
            R.string.main_nav_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        navigationView.bringToFront()

        composeView.setContent {
            MainPage(this)
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
}


@Composable
private fun MainPage(context: Context) {

    Box(modifier = Modifier) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = colorResource(id = R.color.screen_background),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(15.dp)
        ) {

            TodayBox()
            Spacer(modifier = Modifier.size(10.dp))
            TaskBox()

        }

        FloatingActionButton(
            onClick = {
                val intent = Intent(context, AddTask::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(90.dp)
                .padding(16.dp) // فاصله از لبه‌های صفحه
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
        }

    }
}

@SuppressLint("ResourceType")
@Composable
private fun TodayBox() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .size(height = 60.dp, width = 15.dp)
            .background(
                color = colorResource(id = R.color.box_background),
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Monday 7 Aban 1403", fontSize = 24.sp)
    }
}

@Composable
private fun TaskBox() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.box_background),
                shape = RoundedCornerShape(16.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val specialArray = specialDayTaskDB?.let { getUnDoneSpecialTask(it) }
        val deadLinedArray = deadLinedTaskDB?.let { getUnDoneDeadLinedTask(it) }
        val simpleArray = simpleTaskDB?.let { getUnDoneSimpleTask(it) }

//        val dailyHabitArray = dailyHabitDB?.let { getUnDoneDailyHabitsTask(it) }
//        val weeklyHabitArray = weeklyHabitDB?.let { getUnDoneWeeklyHabitsTask(it) }
//        val monthlyHabitArray = monthlyHabitDB?.let { getUnDoneMonthlyHabitsTask(it) }


        if (
            specialArray?.isEmpty() == true &&
            deadLinedArray?.isEmpty() == true &&
            simpleArray?.isEmpty() == true
//            dailyHabitArray?.isEmpty() == true &&
//            weeklyHabitArray?.isEmpty() == true &&
//            monthlyHabitArray?.isEmpty() == true
        ) {
            Text(
                text = "You Have not any undone task!!",
                fontSize = 20.sp,
                modifier = Modifier.padding(20.dp)
            )
        } else {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Unfinished Tasks",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(10.dp)
                )
                if (specialArray!!.isNotEmpty()) {
                    ShowSpecialTaskBox(specialArray)
                }
                if (deadLinedArray!!.isNotEmpty()) {
                    ShowDeadLinedTaskBox(deadLinedArray)
                }
                if (simpleArray!!.isNotEmpty()) {
                    ShowSimpleTaskBox(simpleArray)
                }
//                if (dailyHabitArray!!.isNotEmpty() && weeklyHabitArray!!.isNotEmpty() && monthlyHabitArray!!.isNotEmpty()) {
//                    ShowHabitBox(dailyHabitArray, weeklyHabitArray, monthlyHabitArray)
//                }

            }
        }
    }
}

@Composable
private fun ShowSpecialTaskBox(specialArrayList: ArrayList<SpecialDayTask>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.box_background),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Special Day", fontSize = 24.sp)
    }
}

@Composable
private fun ShowDeadLinedTaskBox(deadLinedArrayList: ArrayList<DeadLinedTask>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.box_background),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "DeadLinedTask", fontSize = 24.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShowSimpleTaskBox( simpleArrayList: ArrayList<SimpleTask>) {

    val sheetState = rememberModalBottomSheetState()
    val isSheetOpen = remember { mutableStateOf(false) }
    var simpleTask by remember { mutableStateOf(SimpleTask("","")) }

    val showDeleteDialog = remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.box_background),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(text = "Simple Task", fontSize = 24.sp, modifier = Modifier.padding(4.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            items(simpleArrayList.size) { index ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    simpleTask = simpleArrayList[index]
                                    isSheetOpen.value = true
                                },
                                onLongPress = {
                                    simpleTask = simpleArrayList[index]
                                    showDeleteDialog.value = true
                                }
                            )
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    var isChecked by remember { mutableIntStateOf(simpleArrayList[index].isDone) }

                    Checkbox(
                        checked = BoolInt.intToBool(isChecked),
                        onCheckedChange = {
                            isChecked = BoolInt.boolToInt(it)
                            simpleArrayList[index].isDone = isChecked
                            simpleTaskDB!!.updateRecord(
                                simpleArrayList[index]
                            )
                        }
                    )
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = simpleArrayList[index].title,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.size(5.dp))
                        Text(text = simpleArrayList[index].description)
                    }

                }
            }

        }
    }

    if (isSheetOpen.value) {
        EditSimpleTask(state = sheetState, isSheetOpen = isSheetOpen, simpleTask = simpleTask)
    }

    if (showDeleteDialog.value) {
        DeleteTaskDialog(showDeleteDialog = showDeleteDialog, id = simpleTask.id)
    }


}

@Composable
private fun ShowHabitBox(
    dailyHabitArrayList: ArrayList<DailyHabit>,
    weeklyHabitArrayList: ArrayList<WeeklyHabit>,
    monthlyHabitArrayList: ArrayList<MonthlyHabit>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.box_background),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Habits", fontSize = 24.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSimpleTask(state: SheetState, isSheetOpen: MutableState<Boolean>, simpleTask: SimpleTask) {

    ModalBottomSheet(
        onDismissRequest = { isSheetOpen.value = false },
        sheetState = state,
    ) {
        simpleTaskDB?.let {
            EditSimpleTaskUI(
                isSheetOpen,
                simpleTaskDB = it,
                simpleTask = simpleTask
            )
        }
    }

}

@Composable
fun DeleteTaskDialog( showDeleteDialog: MutableState<Boolean>, id:Int) {
    val context = LocalContext.current;
    if (showDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog.value = false },
            title = { Text(text = "Will you delete task?") },
            text = { Text("Deleting task.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog.value = false
                        simpleTaskDB!!.deleteRecord(id)
                        context.startActivity(Intent(context, MainActivity2::class.java))
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog.value = false }
                ) {
                    Text("No")
                }
            }
        )
    }
}