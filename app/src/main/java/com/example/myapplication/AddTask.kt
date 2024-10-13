package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ali.uneversaldatetools.date.JalaliDateTime
import com.example.myapplication.model.tasks.TaskType
import com.gmail.hamedvakhide.compose_jalali_datepicker.JalaliDatePickerDialog
import ir.huri.jcal.JalaliCalendar
import java.util.Date

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

        val composeView = findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
//             Compose code here
            AddTaskMain()
        }
    }
}

@Composable
fun AddTaskMain() {

    var taskTitle by remember { mutableStateOf("")}
    var taskDescription by remember { mutableStateOf("")}
    var selectedTaskType by remember { mutableStateOf(TaskType.SIMPLE) }
    var specialDay by remember { mutableStateOf<JalaliCalendar?>(null) }
    var dueDate by remember { mutableStateOf<JalaliDateTime?>(null) }
    var repeatDays by remember { mutableStateOf(listOf<Int>()) }
    var endDate by remember { mutableStateOf<JalaliDateTime?>(null) }



    // رابط کاربری
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {


        Text(
            text = "Add Task",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center)


        Box(modifier = Modifier
            .padding()
            .background(Color.Red, shape = RoundedCornerShape(10.dp))){
            Text(text = "Hi",
                Modifier.fillMaxSize())
        }


        // فیلد عنوان تسک
        OutlinedTextField(
            value = taskTitle,
            onValueChange = { taskTitle = it },
            label = { Text("Task Title") },
            modifier = Modifier.fillMaxWidth()
        )

        // فیلد توضیحات تسک
        OutlinedTextField(
            value = taskDescription,
            onValueChange = { taskDescription = it },
            label = { Text("Task Description") },
            modifier = Modifier.fillMaxWidth()
        )

        // انتخاب نوع تسک
        TaskTypeSelector(selectedTaskType) { newType ->
            selectedTaskType = newType
        }

        // نمایش فیلدهای وابسته به نوع تسک
        when (selectedTaskType) {
            TaskType.SIMPLE -> {
                // برای تسک ساده نیازی به فیلد اضافی نیست
            }
            TaskType.SPECIAL_DAY -> {
                // انتخاب تاریخ خاص برای تسک
                val openDialog = remember { mutableStateOf(false) }
                Button(onClick = { openDialog.value = true }) {
                    Text(text = "Open JalaliDatePicker")
                }
                JalaliDatePickerDialog(
                    openDialog = openDialog,
                    onSelectDay = { //it:JalaliCalendar
                        specialDay = it
                        Log.d("Date", "onSelect: ${it.day} ${it.monthString} ${it.year}")

                    },
                    onConfirm = {
                        Log.d("Date", "onConfirm: ${it.day} ${it.monthString} ${it.year}")
                    }
                )

            }
//            TaskType.DEADLINED -> {
//                // انتخاب تاریخ نهایی برای تسک
//                DatePicker(onDateSelected = { selectedDueDate ->
//                    dueDate = selectedDueDate
//                })
//            }
//            TaskType.HABIT -> {
//                // انتخاب نوع تکرار و تاریخ پایان (در صورت وجود)
//                FrequencySelector(repeatFrequency) { newFrequency ->
//                    repeatFrequency = newFrequency
//                }
//                DatePicker(onDateSelected = { selectedEndDate ->
//                    endDate = selectedEndDate
//                }, label = "End Date (Optional)")
//            }
//
//            TaskType.DEADLINED -> TODO()
//            TaskType.SPECIAL_DAY -> TODO()
            TaskType.HABIT -> TODO()
            TaskType.DEADLINED -> TODO()
        }

    }
}

@Composable
fun TaskTypeSelector(selectedTaskType: TaskType, onTaskTypeSelected: (TaskType) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        TextButton(onClick = { expanded = true }) {
            Text("Task Type: ${selectedTaskType.name}")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            TaskType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.name) },
                    onClick = {
                    onTaskTypeSelected(type)
                    expanded = false
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowItem() {
    AddTaskMain()
}