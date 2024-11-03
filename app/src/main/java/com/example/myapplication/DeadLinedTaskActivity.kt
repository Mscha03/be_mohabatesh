package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
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
import com.example.myapplication.database.taskDataBase.DeadLinedTaskDB
import com.example.myapplication.model.tasks.DeadLinedTask
import com.example.myapplication.model.tasks.SimpleTask

private var deadLinedTaskDB: DeadLinedTaskDB? = null

class DeadLinedTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_dead_lined_task_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        deadLinedTaskDB = DeadLinedTaskDB(this)

        val composeView = findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            DeadLinedTaskDetail()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskCard() {
}

@Composable
fun DeadLinedTaskDetail() {

    val scrollState = rememberScrollState()

    // UI
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
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

            val taskArray = getTaskFromDB()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height((taskArray.size * 500).dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                taskArray.forEach { task ->
                    SelectTaskCardType(task = task)
                }
            }
        }

    }
}

@Composable
private fun TitleScreenTextFiled() {
    // Title
    Text(
        modifier = Modifier.padding(10.dp),
        text = "Dead Lined Task",
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center,
    )
}


@Composable
private fun SelectTaskCardType(task: DeadLinedTask) {
    if (task.subTask.isEmpty()) {
        TaskCard(task = task)
    } else {
        TaskCardWithSubTask(task = task)
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
private fun TaskCard(task: DeadLinedTask) {
    // UI
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = colorResource(id = R.color.screen_background)),

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(top = 2.dp, bottom = 2.dp, start = 10.dp, end = 22.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = colorResource(id = R.color.screen_background))
                .clickable(
                    onClick = {
                        TODO()
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var isChecked by remember { mutableStateOf(BoolInt.intToBool(task.isDone)) }

            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                    task.isDone = BoolInt.boolToInt(it)
                    deadLinedTaskDB!!.updateRecord(task)
                }
            )
            Text(text = task.title, fontSize = 20.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.padding(20.dp),
                text = "${task.deadDate.year} / ${task.deadDate.month} / ${task.deadDate.day}"
            )
        }
    }
}

@Composable
private fun TaskCardWithSubTask(task: DeadLinedTask) {

    var isExpanded by remember { mutableStateOf(false) }
    var imageVector by remember { mutableIntStateOf(R.drawable.baseline_keyboard_arrow_down_24) }
    var progress by remember { mutableFloatStateOf(calculateDoneSubTask(task)) }
    var isChecked by remember { mutableStateOf(BoolInt.intToBool(task.isDone)) }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = colorResource(id = R.color.screen_background))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 10.dp, end = 22.dp, bottom = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = colorResource(id = R.color.screen_background))
                .clickable(
                    onClick = {
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = isChecked,
                onCheckedChange = {})

            Text(text = task.title, fontSize = 20.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.padding(20.dp),
                text = "${task.deadDate.year} / ${task.deadDate.month} / ${task.deadDate.day}"
            )
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = { isExpanded = !isExpanded }
            ) {
                Icon(painter = painterResource(imageVector), contentDescription = "")
            }


        }
        Text(
            modifier = Modifier.padding(bottom = 10.dp, start = 20.dp),
            text = task.description
        )

        val progressPerCent = (progress / task.subTask.size)
        ProgressBar(progress = progressPerCent)

        isChecked = (progressPerCent == 1f)

        if (isExpanded) {

            imageVector = R.drawable.baseline_keyboard_arrow_up_24

            if (progress / task.subTask.size == 1f) {
                task.isDone = 1
                deadLinedTaskDB?.updateRecord(task)
            } else {
                task.isDone = 0
                deadLinedTaskDB?.updateRecord(task)
            }

            Column(
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp, bottom = 10.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = colorResource(id = R.color.box_background))
            ) {
                task.subTask.forEach { subTask ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var isChecked by remember { mutableStateOf(BoolInt.intToBool(subTask.isDone)) }
                        Checkbox(
                            modifier = Modifier.padding(start = 10.dp),
                            checked = isChecked,
                            onCheckedChange = {
                                isChecked = it
                                subTask.isDone = BoolInt.boolToInt(it)
                                deadLinedTaskDB!!.updateSubTask(subTask)
                                progress = changeToProgress(progress, it)
                            }
                        )
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = subTask.title)
                        }
                    }
                }
            }


        } else {
            imageVector = R.drawable.baseline_keyboard_arrow_down_24
        }


    }
}

private fun getTaskFromDB(): ArrayList<DeadLinedTask> {
    val taskArray = ArrayList<DeadLinedTask>()
    val cursor = deadLinedTaskDB!!.getAllRecords()


    if (cursor.moveToFirst()) {
        do {
            val task = DeadLinedTask(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getString(cursor.getColumnIndexOrThrow("description")),
                cursor.getInt(cursor.getColumnIndexOrThrow("isdone")),
                JalaliDateTime(
                    cursor.getInt(cursor.getColumnIndexOrThrow("year")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("month")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("day")),
                )
            )

            val subCursor = deadLinedTaskDB!!.getAllSubTask(
                cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            )

            if (subCursor.moveToFirst()) {
                do {
                    val subTask = SimpleTask(
                        subCursor.getInt(subCursor.getColumnIndexOrThrow("id")),
                        subCursor.getString(subCursor.getColumnIndexOrThrow("name")),
                        "",
                        subCursor.getInt(subCursor.getColumnIndexOrThrow("isdone"))
                    )
                    task.subTask.add(subTask)
                } while (subCursor.moveToNext())
            }

            taskArray.add(task)

        } while (cursor.moveToNext())

    }
    return taskArray
}

@Composable
fun ProgressBar(progress: Float) {

    // تعیین رنگ بر اساس مقدار پیشرفت
    val progressColor = when {
        progress <= 0.2f -> Color.Red
        progress <= 0.4f -> Color(0xFFFFA500) // نارنجی
        progress <= 0.6f -> Color.Yellow
        progress <= 0.8f -> Color.Green
        progress < 1.0f -> Color.Blue
        else -> Color(0xFF7140C7)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .height(8.dp),
            color = progressColor,
            trackColor = Color.Gray,
        )

    }
}

fun changeToProgress(progress: Float, done: Boolean): Float {
    return if (done) {
        (progress + 1)
    } else {
        (progress - 1)
    }
}

fun calculateDoneSubTask(task: DeadLinedTask): Float {
    var done = 0f
    task.subTask.forEach { t ->
        if (t.isDone == 1) done++
    }
    return done
}