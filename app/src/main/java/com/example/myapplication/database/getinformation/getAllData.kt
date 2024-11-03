package com.example.myapplication.database.getinformation

import com.ali.uneversaldatetools.date.JalaliDateTime
import com.example.myapplication.database.AddInformationForHistory
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
import com.example.myapplication.time.WithWeekJalaliDateTime
import ir.huri.jcal.JalaliCalendar

fun getAllSimpleTask(db: SimpleTaskDB): ArrayList<SimpleTask>{
    val tasks = ArrayList<SimpleTask>()
    val cursor = db.getAllRecords()

    if (cursor.moveToFirst()) {
        do {
            val task = SimpleTask(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getString(cursor.getColumnIndexOrThrow("description")),
                cursor.getInt(cursor.getColumnIndexOrThrow("isdone"))

            )
            tasks.add(task)
        } while (cursor.moveToNext())
    }
    return tasks
}
fun getAllSpecialTask(db: SpecialDayTaskDB): ArrayList<SpecialDayTask>{
    val tasks = ArrayList<SpecialDayTask>()
    val cursor = db.getAllRecords()

    if (cursor.moveToFirst()) {
        do {
            val task = SpecialDayTask(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getString(cursor.getColumnIndexOrThrow("description")),
                cursor.getInt(cursor.getColumnIndexOrThrow("isdone")),
                JalaliDateTime(
                    cursor.getInt(cursor.getColumnIndexOrThrow("deadyear")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("deadmonth")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("deadday"))
                )

            )
            tasks.add(task)
        } while (cursor.moveToNext())
    }
    return tasks
}
fun getAllDeadLinedTask(db: DeadLinedTaskDB): ArrayList<DeadLinedTask> {
    val tasks = ArrayList<DeadLinedTask>()
    val cursor = db.getAllRecords()

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
                    cursor.getInt(cursor.getColumnIndexOrThrow("day"))
                )

            )
            tasks.add(task)
        } while (cursor.moveToNext())
    }

    tasks.forEach { task ->
        val subTaskCursor = db.getAllSubTask(task.id)
        if (subTaskCursor.moveToFirst()) {
            do {
                val subTask = SimpleTask(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")), "",
                    cursor.getInt(cursor.getColumnIndexOrThrow("isdone"))
                )
                task.subTask.add(subTask)
            } while (cursor.moveToNext())
        }
    }
    return tasks
}

//fun getAllDailyHabitsTask(db: DailyHabitDB): ArrayList<DailyHabit>{
//    val tasks = ArrayList<DailyHabit>()
//    val cursor = db.getAllHabit()
//
//
//    if (cursor.moveToFirst()) {
//        do {
//            val task = DailyHabit(
//                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
//                cursor.getString(cursor.getColumnIndexOrThrow("name")),
//                cursor.getString(cursor.getColumnIndexOrThrow("description")),
//                0,
//                WithWeekJalaliDateTime(
//                    cursor.getInt(cursor.getColumnIndexOrThrow("year")),
//                    cursor.getInt(cursor.getColumnIndexOrThrow("month")),
//                    cursor.getInt(cursor.getColumnIndexOrThrow("week")),
//                    cursor.getInt(cursor.getColumnIndexOrThrow("day"))
//                )
//
//            )
//            val today = JalaliCalendar()
//            val dayCursor = db.getDays(
//                task.id,
//                today.day,
//                today.month,
//                today.year
//            )
//
//            if (cursor.moveToFirst()) {
//                do {
//                    task.isDone = dayCursor.getInt(cursor.getColumnIndexOrThrow("isdone"))
//                }while (cursor.moveToNext())
//
//            tasks.add(task)
//            }
//        } while (cursor.moveToNext())
//    }
//
//    return tasks
//}
//fun getAllWeeklyHabitsTask(db: WeeklyHabitDB): ArrayList<WeeklyHabit>{
//    val tasks = ArrayList<WeeklyHabit>()
//    val cursor = db.getAllHabit()
//
//
////    if (cursor.moveToFirst()) {
////        do {
////            val task = WeeklyHabit(
////                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
////                cursor.getString(cursor.getColumnIndexOrThrow("name")),
////                cursor.getString(cursor.getColumnIndexOrThrow("description")),
////                0,
////                WithWeekJalaliDateTime(
////                    cursor.getInt(cursor.getColumnIndexOrThrow("year")),
////                    cursor.getInt(cursor.getColumnIndexOrThrow("month")),
////                    cursor.getInt(cursor.getColumnIndexOrThrow("week")),
////                    cursor.getInt(cursor.getColumnIndexOrThrow("day"))
////                )
////
////            )
////            val today = JalaliCalendar()
////            val weekCursor = db.getWeek(
////                task.id,
////                AddInformationForHistory.getPersianWeekOfYear(today),
////                today.year
////            )
////
////            if (cursor.moveToFirst()) {
////                do {
////                    task.isDone = dayCursor.getInt(cursor.getColumnIndexOrThrow("isdone"))
////                }while (cursor.moveToNext())
////
////                tasks.add(task)
////            }
////        } while (cursor.moveToNext())
////    }
//
//    return tasks
//}
//fun getAllMonthlyHabitsTask(db: MonthlyHabitDB): ArrayList<MonthlyHabit>{
//    val tasks = ArrayList<MonthlyHabit>()
//    return tasks
//}
