package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Clear
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ali.uneversaldatetools.date.JalaliDateTime
import com.example.myapplication.converter.BoolInt
import com.example.myapplication.database.TaskDataBase.SimpleTaskDB
import com.example.myapplication.database.TaskDataBase.SpecialDayTaskDB
import com.example.myapplication.model.tasks.SimpleTask
import com.example.myapplication.model.tasks.SpecialDayTask
import com.example.myapplication.model.tasks.TaskType
import com.example.myapplication.model.tasks.habits.HabitType
import com.example.myapplication.time.ShamsiName
import com.gmail.hamedvakhide.compose_jalali_datepicker.JalaliDatePickerDialog

var simpleDB: SimpleTaskDB? = null
var specialDB: SpecialDayTaskDB? = null

class AddTask : AppCompatActivity() {

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

        val composeView = findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            AddTaskMain(this)
        }
    }
}

@Composable
fun AddTaskMain(context: Context) {

    var taskTitle = ""
    var taskDescription = ""
    var selectedTaskType = TaskType.SIMPLE
    var specialDay: JalaliDateTime? = null
    var deadlinedDay: JalaliDateTime? = null
    var subTasksOfDeadlinedTask = ArrayList<SimpleTask>()

    var expandedHabitType by remember { mutableStateOf(false) }
    var selectedHabitType: HabitType? = null
    val scrollState = rememberScrollState()


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
                            deadlinedDay = selectDeadLine()
                            subTasksOfDeadlinedTask = addSubTaskForDeadlinedTask()
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
                                WeeklySelector()
                            }

                            HabitType.MONTHLY -> {
                                MonthlySelector()
                            }

                            null -> {}
                        }

                    }

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

                            }

                            TaskType.HABIT -> TODO()
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
fun WeeklySelector() {
    // وضعیت روزهای انتخاب شده
    var selectedDays by remember { mutableStateOf(listOf<String>()) }

    Column {
        WeekDaySelector(selectedDays = selectedDays) { day ->
            // تغییر وضعیت انتخاب یک روز
            selectedDays = if (selectedDays.contains(day)) {
                selectedDays - day  // حذف روز
            } else {
                selectedDays + day  // اضافه کردن روز
            }
        }
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
        }
    }
}


@Composable
fun MonthlySelector() {
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
fun TitleScreenTextFiled() {
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
    var selectedTaskType by remember { mutableStateOf(TaskType.SIMPLE) }
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
    val subTaskTitles = remember { mutableStateListOf("sub task")}

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.box_background),
                shape = RoundedCornerShape(10.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(
            text = "sub Tasks",
            modifier = Modifier.padding(start = 25.dp, top = 15.dp))
        LazyColumn(
            modifier = Modifier
                .height((subTaskTitles.size*50).dp)
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

    subTaskTitles.forEach{ item -> subTasks.add(SimpleTask(item,""))}

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
    var selectedHabitType by remember { mutableStateOf(HabitType.DAILY) }
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
    val deadlinedTask = SpecialDayTask(title, description, deadLine)
    specialDB!!.insertRecord(deadlinedTask)
}

fun addDeadLinedTask(
    title: String,
    description: String,
    deadLine: JalaliDateTime,
    subTasks: ArrayList<SimpleTask>){


}