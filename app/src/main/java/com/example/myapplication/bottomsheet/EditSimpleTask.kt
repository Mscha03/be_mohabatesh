package com.example.myapplication.bottomsheet

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.addSimpleTask
import com.example.myapplication.database.taskDataBase.SimpleTaskDB
import com.example.myapplication.model.tasks.SimpleTask
import com.example.myapplication.taskDescription
import com.example.myapplication.taskTitleTextFiled

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSimpleTaskUI(isSheetOpen: MutableState<Boolean>, simpleTaskDB: SimpleTaskDB, simpleTask: SimpleTask){

    Column (
        modifier = Modifier
            .padding(22.dp)

    ){
        simpleTask.title = taskTitleTextFiled(simpleTask.title)
        simpleTask.description = taskDescription(simpleTask.description)

        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp)
            .size(55.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                editSimpleTask(simpleTask, simpleTaskDB)
                isSheetOpen.value = false
            })
        {
            Text(text = "Edit")
        }
    }

}

fun editSimpleTask(simpleTask: SimpleTask, simpleTaskDB: SimpleTaskDB) {
    simpleTaskDB.updateRecord(simpleTask)
}