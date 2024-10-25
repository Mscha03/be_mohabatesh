package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ali.uneversaldatetools.date.JalaliDateTime
import com.example.myapplication.converter.BoolInt
import com.example.myapplication.model.tasks.DeadLinedTask
import com.example.myapplication.model.tasks.SimpleTask

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

        val composeView = findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            DeadLinedTaskDetail()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeadLinedTaskDetailPreview() {
    DeadLinedTaskDetail()
}

@Composable
fun DeadLinedTaskDetail() {
    // UI
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
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


            var a = DeadLinedTask("task 1", "des of 1", JalaliDateTime(1403, 7, 1))
            a.subTask.add(SimpleTask("1.sub task 1", "des of sub task 1"))
            a.subTask.add(SimpleTask("1.sub task 2", "des of sub task 2"))
            var b = DeadLinedTask("task 2", "des of 2", JalaliDateTime(1403, 7, 2))

            val c = ArrayList<DeadLinedTask>();
            c.add(a)
            c.add(b)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height((c.size * 500).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                c.forEach { task ->
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
    Box (modifier = Modifier
        .clip(RoundedCornerShape(16.dp))
        .background(color = colorResource(id = R.color.screen_background))
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding( top = 2.dp, bottom = 2.dp, start = 10.dp, end = 22.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = colorResource(id = R.color.screen_background)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var isChecked by remember { mutableStateOf(BoolInt.intToBool(task.isDone)) }

            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                    task.isDone = BoolInt.boolToInt(it)
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

    var isExpanded by remember { mutableStateOf(true) }
    var imageVector by remember { mutableStateOf(Icons.Default.KeyboardArrowDown) }

    Column (modifier = Modifier
        .clip(RoundedCornerShape(16.dp))
        .background(color = colorResource(id = R.color.screen_background))
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 10.dp, end = 22.dp, bottom = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = colorResource(id = R.color.screen_background))
                .clickable(
                    onClick = {
                        isExpanded = !isExpanded
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(checked = false, onCheckedChange = { })
            Text(text = task.title, fontSize = 20.sp)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = imageVector,
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
            )
        }

        if (isExpanded) {
            imageVector = Icons.Default.KeyboardArrowUp
            SubTaskCard(task = task)
        } else {
            imageVector = Icons.Default.KeyboardArrowDown
        }


    }
}

@Composable
private fun SubTaskCard(task: DeadLinedTask) {
    LazyColumn(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, bottom = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color = colorResource(id = R.color.box_background))
    ) {
        items(task.subTask) { subTask ->
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
                    }
                )
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(text = subTask.title)
                }
            }
        }
    }
}