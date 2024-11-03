package com.example.myapplication.database.getinformation

import com.example.myapplication.database.taskDataBase.DeadLinedTaskDB
import com.example.myapplication.database.taskDataBase.SimpleTaskDB
import com.example.myapplication.database.taskDataBase.SpecialDayTaskDB
import com.example.myapplication.database.taskDataBase.habits.DailyHabitDB
import com.example.myapplication.database.taskDataBase.habits.MonthlyHabitDB
import com.example.myapplication.database.taskDataBase.habits.WeeklyHabitDB
import com.example.myapplication.model.tasks.DeadLinedTask
import com.example.myapplication.model.tasks.SimpleTask
import com.example.myapplication.model.tasks.SpecialDayTask
import com.example.myapplication.model.tasks.habits.DailyHabit
import com.example.myapplication.model.tasks.habits.MonthlyHabit
import com.example.myapplication.model.tasks.habits.WeeklyHabit

fun getUnDoneSimpleTask(db: SimpleTaskDB): ArrayList<SimpleTask> {
    val tasks = getAllSimpleTask(db)
    val undoneTask = ArrayList<SimpleTask>()

    tasks.forEach { task ->
        if (task.isDone == 0) {
            undoneTask.add(task)
        }
    }
    return undoneTask
}

fun getUnDoneSpecialTask(db: SpecialDayTaskDB): ArrayList<SpecialDayTask> {
    val tasks = ArrayList<SpecialDayTask>()
    return tasks
}

fun getUnDoneDeadLinedTask(db: DeadLinedTaskDB): ArrayList<DeadLinedTask> {
    val tasks = ArrayList<DeadLinedTask>()
    return tasks
}

//fun getUnDoneDailyHabitsTask(db: DailyHabitDB): ArrayList<DailyHabit> {
//    val tasks = ArrayList<DailyHabit>()
//    return tasks
//}
//
//fun getUnDoneWeeklyHabitsTask(db: WeeklyHabitDB): ArrayList<WeeklyHabit> {
//    val tasks = ArrayList<WeeklyHabit>()
//    return tasks
//}
//
//fun getUnDoneMonthlyHabitsTask(db: MonthlyHabitDB): ArrayList<MonthlyHabit> {
//    val tasks = ArrayList<MonthlyHabit>()
//    return tasks
//}
//