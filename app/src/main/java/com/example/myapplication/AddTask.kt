package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ali.uneversaldatetools.date.JalaliDateTime
import com.example.myapplication.database.AddInformationForHistory
import com.example.myapplication.database.AddInformationForHistory.getPersianWeekOfYear
import com.example.myapplication.database.taskDataBase.DeadLinedTaskDB
import com.example.myapplication.database.taskDataBase.SimpleTaskDB
import com.example.myapplication.database.taskDataBase.SpecialDayTaskDB
import com.example.myapplication.database.taskDataBase.habits.DailyHabitDB
import com.example.myapplication.database.taskDataBase.habits.MonthlyHabitDB
import com.example.myapplication.database.taskDataBase.habits.WeeklyHabitDB
import com.example.myapplication.model.tasks.DeadLinedTask
import com.example.myapplication.model.tasks.SimpleTask
import com.example.myapplication.model.tasks.SpecialDayTask
import com.example.myapplication.model.tasks.TaskType
import com.example.myapplication.model.tasks.habits.HabitType
import com.example.myapplication.time.ShamsiName
import com.example.myapplication.time.WithWeekJalaliDateTime
import com.gmail.hamedvakhide.compose_jalali_datepicker.JalaliDatePickerDialog
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import ir.huri.jcal.JalaliCalendar
import java.time.LocalTime
import java.util.Calendar

var simpleDB: SimpleTaskDB? = null
var specialDB: SpecialDayTaskDB? = null
var deadLinedDB: DeadLinedTaskDB? = null

var dailyHabitDB: DailyHabitDB? = null
var weeklyHabitDB: WeeklyHabitDB? = null
var monthlyHabitDB: MonthlyHabitDB? = null

class AddTask : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        simpleDB = SimpleTaskDB(this)
        specialDB = SpecialDayTaskDB(this)
        deadLinedDB = DeadLinedTaskDB(this)

        dailyHabitDB = DailyHabitDB(this)
        weeklyHabitDB = WeeklyHabitDB(this)
        monthlyHabitDB = MonthlyHabitDB(this)

        val composeView = findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            AddTaskMain(this)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddTaskMain(context: Context) {

    var taskTitle = ""
    var taskDescription = ""
    var selectedTaskType = TaskType.HABIT
    var specialDay: JalaliDateTime? = null
    var deadlinedDay: JalaliDateTime? = null
    var subTasksOfDeadlinedTask = ArrayList<SimpleTask>()

    var selectedWeeklyHabitDays = ArrayList<Int>()
    var selectedMonthlyHabitDays = ArrayList<Int>()

    var expandedHabitType by remember { mutableStateOf(false) }
    var selectedHabitType: HabitType? = null
    val scrollState = rememberScrollState()

    var haveAlarm by remember { mutableStateOf(false) }
    val currentTime = Calendar.getInstance()
    var time = LocalTime.now()

    // UI
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TitleScreenTextFiled()

        // Background
        Box(
            modifier = Modifier
                .padding(7.dp)
                .fillMaxSize()
                .background(
                    color = colorResource(id = R.color.box_background),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                taskTitle = taskTitleTextFiled()

                taskDescription = taskDescription()

                selectedTaskType = selectTaskType()


                // Show field depends on Task Type
                when (selectedTaskType) {

                    TaskType.SIMPLE -> {
                        // No field
                    }

                    TaskType.SPECIAL_DAY -> {
                        specialDay = selectDeadLine()
                    }

                    TaskType.DEADLINED -> {
                        Column(modifier = Modifier.padding()) {
                            val haveSubTask = remember { mutableStateOf(false) }

                            deadlinedDay = selectDeadLine()

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = haveSubTask.value,
                                    onCheckedChange = { haveSubTask.value = it })
                                Text(text = "Have Sub Tasks")
                            }
                            if (haveSubTask.value) {
                                subTasksOfDeadlinedTask = addSubTaskForDeadlinedTask()
                            }
                        }
                    }

                    TaskType.HABIT -> {

                        Row(
                            modifier = Modifier
                                .padding()
                        ) {

                            selectedHabitType = selectHabitType()

                        }
                        when (selectedHabitType) {

                            HabitType.DAILY -> {
                                // nothing
                            }

                            HabitType.WEEKLY -> {
                                selectedWeeklyHabitDays = weeklySelector()
                            }

                            HabitType.MONTHLY -> {
                                selectedMonthlyHabitDays = monthlySelector()
                            }

                            null -> {}
                        }

                    }

                }

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Checkbox(checked = haveAlarm, onCheckedChange = { haveAlarm = it })
                    Text(text = "Add Alarm")
                }

                if (haveAlarm) {
                    time = selectTime()
                }
                // Add Button
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp)
                    .size(55.dp),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        when (selectedTaskType) {

                            TaskType.SIMPLE -> {
                                addSimpleTask(
                                    taskTitle,
                                    taskDescription
                                )
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                                Toast.makeText(context, "Task Added", Toast.LENGTH_SHORT).show()
                            }

                            TaskType.SPECIAL_DAY -> {
                                addSpecialTask(
                                    taskTitle,
                                    taskDescription,
                                    specialDay!!
                                )
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                                Toast.makeText(context, "Task Added", Toast.LENGTH_SHORT).show()
                            }

                            TaskType.DEADLINED -> {
                                addDeadLinedTask(
                                    taskTitle,
                                    taskDescription,
                                    deadlinedDay!!,
                                    subTasksOfDeadlinedTask
                                )
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                                Toast.makeText(context, "Task Added", Toast.LENGTH_SHORT).show()
                            }

                            TaskType.HABIT -> {
                                val date = JalaliDateTime.Now()
                                when (selectedHabitType) {
                                    HabitType.DAILY -> {
                                        addDailyHabit(
                                            taskTitle,
                                            taskDescription,
                                            WithWeekJalaliDateTime(
                                                date.year, date.month, getPersianWeekOfYear(JalaliCalendar()), date.day
                                            )
                                        )
                                        val intent = Intent(context, MainActivity::class.java)
                                        context.startActivity(intent)
                                        Toast.makeText(context, "Task Added", Toast.LENGTH_SHORT).show()
                                    }

                                    HabitType.WEEKLY -> {
                                        addWeeklyHabit(
                                            taskTitle,
                                            taskDescription,
                                            WithWeekJalaliDateTime(
                                                date.year, date.month, getPersianWeekOfYear(JalaliCalendar()), date.day
                                            ),
                                            selectedWeeklyHabitDays
                                        )
                                        val intent = Intent(context, MainActivity::class.java)
                                        context.startActivity(intent)
                                        Toast.makeText(context, "Task Added", Toast.LENGTH_SHORT).show()
                                    }

                                    HabitType.MONTHLY -> {
                                        addMonthlyHabit(
                                            taskTitle,
                                            taskDescription,
                                            WithWeekJalaliDateTime(
                                                date.year, date.month, getPersianWeekOfYear(JalaliCalendar()), date.day
                                            ),
                                            selectedMonthlyHabitDays
                                        )
                                        val intent = Intent(context, MainActivity::class.java)
                                        context.startActivity(intent)
                                        Toast.makeText(context, "Task Added", Toast.LENGTH_SHORT).show()
                                    }

                                    null -> {}
                                }

                            }
                        }
                    }
                ) { Text(text = "Add Task") }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun ShowItem() {
    selectHabitType()
}


@Composable
fun weeklySelector(): ArrayList<Int> {
    // وضعیت روزهای انتخاب شده
    var selectedDays by remember { mutableStateOf(listOf<String>()) }
    var selectedDaysInt by remember { mutableStateOf(listOf<Int>()) }

    Column {
        WeekDaySelector(selectedDays = selectedDays) { day ->
            // تغییر وضعیت انتخاب یک روز
            if (selectedDays.contains(day)) {
                selectedDays = selectedDays - day // حذف روز
                selectedDaysInt = selectedDaysInt - dayStrToInt(day)
            } else {
                selectedDays = selectedDays + day  // اضافه کردن روز
                selectedDaysInt = selectedDaysInt + dayStrToInt(day)

            }
        }
    }


    return ArrayList(selectedDaysInt)
}

fun dayStrToInt(day: String): Int {
    return when (day) {
        "Sat" -> 1
        "Sun" -> 2
        "Mon" -> 3
        "Tue" -> 4
        "Wen" -> 5
        "Thu" -> 6
        "Fri" -> 7
        else -> -1
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WeekDaySelector(
    selectedDays: List<String>,  // روزهای انتخاب شده
    onDaySelected: (String) -> Unit,  // تابعی برای زمانی که کاربر یک روز را انتخاب یا حذف کند
) {
    // آرایه‌ای از روزهای هفته
    val daysOfWeek = listOf("Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri")

    FlowRow(
        modifier = Modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        daysOfWeek.forEach { day ->
            val isSelected = selectedDays.contains(day)

            Box(
                modifier = Modifier
                    .size(50.dp)  // اندازه دایره
                    .clip(CircleShape)
                    .background(
                        if (isSelected) {
                            ButtonDefaults.buttonColors().containerColor
                        } else {
                            colorResource(
                                id = R.color.screen_background
                            )
                        }
                    )  // توپر بنفش برای انتخاب شده و شفاف برای انتخاب نشده
                    .border(
                        width = 2.dp,
                        color = if (isSelected) ButtonDefaults.buttonColors().disabledContainerColor else Color.Gray,
                        shape = CircleShape
                    )
                    .clickable { onDaySelected(day) },  // کلیک کردن برای انتخاب/حذف روز
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day,
                    color = if (isSelected) Color.White else Color.Black
                )  // رنگ متن برای حالت انتخاب شده و نشده
            }
            Spacer(modifier = Modifier.size(2.dp))
        }
    }
}


@Composable
fun monthlySelector(): ArrayList<Int> {
    // وضعیت روزهای انتخاب شده
    var selectedDays by remember { mutableStateOf(listOf<Int>()) }

    Column {
        MonthDaySelector(selectedDays = selectedDays) { day ->
            // تغییر وضعیت انتخاب یک روز
            selectedDays = if (selectedDays.contains(day)) {
                selectedDays - day  // حذف روز
            } else {
                selectedDays + day  // اضافه کردن روز
            }
        }

    }

    return ArrayList(selectedDays)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MonthDaySelector(
    selectedDays: List<Int>,  // روزهای انتخاب شده
    onDaySelected: (Int) -> Unit,  // تابعی برای زمانی که کاربر یک روز را انتخاب یا حذف کند
) {
    // آرایه‌ای از روزهای هفته
    val daysOfWeek = listOf(
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        11,
        12,
        13,
        14,
        15,
        16,
        17,
        18,
        19,
        20,
        21,
        22,
        23,
        24,
        25,
        26,
        27,
        28,
        29,
        30,
        31
    )

    FlowRow(
        modifier = Modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        daysOfWeek.forEach { day ->
            val isSelected = selectedDays.contains(day)

            Box(
                modifier = Modifier
                    .size(50.dp)  // اندازه دایره
                    .clip(CircleShape)
                    .background(
                        if (isSelected) {
                            ButtonDefaults.buttonColors().containerColor
                        } else {
                            colorResource(
                                id = R.color.screen_background
                            )
                        }
                    )  // توپر بنفش برای انتخاب شده و شفاف برای انتخاب نشده
                    .border(
                        width = 2.dp,
                        color = if (isSelected) ButtonDefaults.buttonColors().disabledContainerColor else Color.Gray,
                        shape = CircleShape
                    )
                    .clickable { onDaySelected(day) },  // کلیک کردن برای انتخاب/حذف روز
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.toString(),
                    color = if (isSelected) Color.White else Color.Black
                )  // رنگ متن برای حالت انتخاب شده و نشده
            }
        }
    }
}

@Composable
private fun TitleScreenTextFiled() {
    // Title
    Text(
        modifier = Modifier.padding(10.dp),
        text = "Add Task",
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun taskTitleTextFiled(): String {
    var taskTitle by remember { mutableStateOf("") }

    // Task Title
    OutlinedTextField(
        value = taskTitle,
        onValueChange = { taskTitle = it },
        label = { Text("Task Title") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    )

    return taskTitle
}

@Composable
fun taskDescription(): String {
    // Task Description
    var taskDescription by remember { mutableStateOf("") }

    OutlinedTextField(
        value = taskDescription,
        onValueChange = { taskDescription = it },
        label = { Text("Task Description") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    )

    return taskDescription
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun selectTaskType(): TaskType {
    var selectedTaskType by remember { mutableStateOf(TaskType.HABIT) }
    var expandedTaskType by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxWidth(),
            expanded = expandedTaskType,
            onExpandedChange = { expandedTaskType = !expandedTaskType }
        ) {
            OutlinedTextField(
                value = selectedTaskType.name,
                onValueChange = {},
                readOnly = true,
                label = { Text(text = "Task Type") },
                modifier = Modifier
                    .menuAnchor()
                    .padding(vertical = 5.dp)
                    .fillMaxWidth(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTaskType) }
            )

            ExposedDropdownMenu(
                expanded = expandedTaskType,
                onDismissRequest = { expandedTaskType = false }
            ) {
                TaskType.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.name) },
                        onClick = {
                            expandedTaskType = false
                            selectedTaskType = type
                        })
                }
            }
        }
    }
    return selectedTaskType
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun addSubTaskForDeadlinedTask(): ArrayList<SimpleTask> {
    val subTaskTitles = remember { mutableStateListOf("sub task") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.box_background),
                shape = RoundedCornerShape(10.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "sub Tasks",
            modifier = Modifier.padding(start = 25.dp, top = 15.dp)
        )
        LazyColumn(
            modifier = Modifier
                .height((subTaskTitles.size * 50).dp)
        ) {
            items(subTaskTitles.size) { index ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = false,
                        onCheckedChange = {},
                    )
                    Column(modifier = Modifier.padding(10.dp)) {
                        BasicTextField(value = subTaskTitles[index],
                            onValueChange = {
                                subTaskTitles[index] = it
                            })
                    }

                    Spacer(modifier = Modifier.weight(1f)) // فضای خالی برای چسباندن آیکون به راست‌ترین قسمت


                    // دکمه با آیکون منفی
                    IconButton(
                        onClick = {
                            subTaskTitles.removeAt(index)
                        },
                        modifier = Modifier.padding(end = 10.dp) // فاصله از عنوان
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close, // استفاده از آیکون منفی
                            contentDescription = "حذف", // توصیف تصویر
                            modifier = Modifier.size(24.dp) // اندازه تصویر
                        )
                    }

                }
            }
        }

        Button(
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .padding(5.dp),
            onClick = {
                subTaskTitles.add("sub task")
            }
        ) {
            Text("Add sub task")
        }
    }

    val subTasks = ArrayList<SimpleTask>()

    subTaskTitles.forEach { item -> subTasks.add(SimpleTask(item, "")) }

    return subTasks
}

@Composable
fun selectDeadLine(): JalaliDateTime {
    val openDialog = remember { mutableStateOf(false) }
    var specialDay by remember { mutableStateOf(JalaliDateTime.Now()) }


    Row(
        modifier = Modifier
            .padding()
    ) {

        // Text field show date
        OutlinedTextField(
            modifier = Modifier
                .width(200.dp)
                .padding(vertical = 6.dp),
            value = "تاریخ: ${specialDay.day} ${
                ShamsiName.getMonthName(
                    specialDay.month,
                    LocalContext.current
                )
            } ${specialDay.year}",
            onValueChange = {},
            readOnly = true,
        )

        // button select date
        Button(
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .padding(5.dp)
                .width(200.dp),
            onClick = { openDialog.value = true }
        ) {
            Text("Choose Date")
        }

    }

    // date selector
    JalaliDatePickerDialog(
        openDialog = openDialog,
        onSelectDay = {
            // nothing
        },
        onConfirm = {
            specialDay = JalaliDateTime(it.year, it.month, it.day)
            Log.d(
                "Date",
                "onConfirm: ${it.day} ${it.monthString} ${it.year}"
            )
        }
    )
    return specialDay
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun selectHabitType(): HabitType {
    var selectedHabitType by remember { mutableStateOf(HabitType.MONTHLY) }
    var expandedTaskType by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxWidth(),
            expanded = expandedTaskType,
            onExpandedChange = { expandedTaskType = !expandedTaskType }
        ) {
            OutlinedTextField(
                value = selectedHabitType.name,
                onValueChange = {},
                readOnly = true,
                label = { Text(text = "Task Type") },
                modifier = Modifier
                    .menuAnchor()
                    .padding(vertical = 5.dp)
                    .fillMaxWidth(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTaskType) }
            )

            ExposedDropdownMenu(
                expanded = expandedTaskType,
                onDismissRequest = { expandedTaskType = false }
            ) {
                HabitType.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.name) },
                        onClick = {
                            expandedTaskType = false
                            selectedHabitType = type
                        })
                }
            }
        }
    }
    return selectedHabitType
}


fun addSimpleTask(title: String, description: String) {
    val simpleTask = SimpleTask(title, description)
    simpleDB!!.insertRecord(simpleTask)
}


fun addSpecialTask(title: String, description: String, deadLine: JalaliDateTime) {
    val specialDayTask = SpecialDayTask(title, description, deadLine)
    specialDB!!.insertRecord(specialDayTask)
}

fun addDeadLinedTask(
    title: String,
    description: String,
    deadLine: JalaliDateTime,
    subTasks: ArrayList<SimpleTask>
) {

    val deadLinedTask = DeadLinedTask(title, description, deadLine)
    deadLinedTask.subTask = subTasks

    val taskId = deadLinedDB!!.insertRecord(deadLinedTask)

    subTasks.forEach {
        deadLinedDB!!.insertSubTask(it, taskId.toInt())
    }
}

fun addDailyHabit(
    title: String,
    description: String,
    createDate: WithWeekJalaliDateTime,
) {
    val id = dailyHabitDB!!.insertRecord(
        title, description, createDate.day, createDate.week, createDate.month, createDate.year
    )

    AddInformationForHistory.addForDaily(id.toInt(), dailyHabitDB, createDate)
}

fun addWeeklyHabit(
    title: String,
    description: String,
    createDate: WithWeekJalaliDateTime,
    daysOfWeek: ArrayList<Int>
) {
    val id = weeklyHabitDB!!.insertRecord(
        title, description, createDate.day, createDate.week, createDate.month, createDate.year
    )
    AddInformationForHistory.addForWeekly(id.toInt(), weeklyHabitDB, createDate, daysOfWeek)
}

fun addMonthlyHabit(
    title: String,
    description: String,
    createDate: WithWeekJalaliDateTime,
    daysOfMonth: ArrayList<Int>
) {
    val id = monthlyHabitDB!!.insertRecord(
        title, description, createDate.day, createDate.week, createDate.month, createDate.year
    )
    AddInformationForHistory.addForMonthly(id.toInt(), monthlyHabitDB, createDate, daysOfMonth)
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun selectTime(): LocalTime? {
    var time by remember { mutableStateOf(LocalTime.now()) }
    val timeDialogState = rememberMaterialDialogState()

    Row(
        modifier = Modifier
            .padding()
    ) {

        // Text field show date
        OutlinedTextField(
            modifier = Modifier
                .width(200.dp)
                .padding(vertical = 6.dp),
            value = "${time.hour}:${time.minute}",
            onValueChange = {},
            readOnly = true,
        )

        // button select date
        Button(
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .padding(5.dp)
                .width(200.dp),
            onClick = {
                timeDialogState.show()
            }
        ) {
            Text("Choose Time")
        }

    }


    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton(text = "Ok") {

            }
            negativeButton(text = "Cancel")
        },
        shape = RoundedCornerShape(30.dp),
        backgroundColor = Color.White,
    ) {
        timepicker(
            title = "Pick a time",
        ) {
            time = it
        }
    }


    return time
}


