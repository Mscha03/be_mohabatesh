package com.example.myapplication.database.TaskDataBase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapplication.model.tasks.DeadLinedTask
import com.example.myapplication.model.tasks.SimpleTask

const val DATABASE_NAME = "deadlined_db"
const val DATABASE_VERSION = 1

const val TABLE_NAME = "deadlined"
const val ID_COL = "id"
const val NAME_COL = "name"
const val DESCRIPTION_COL = "description"
const val DAY = "day"
const val MONTH = "month"
const val YEAR = "year"

const val SUB_TASK_TABLE_NAME = "sub_dead"
const val SUB_TASK_ID = "id"
const val SUB_TASK_DEADLINED_ID = "routine_id"
const val SUB_TASK_NAME = "name"
const val SUB_TASK_ISDONE = "isdone"


class DeadLinedTaskDB(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase?) {
        val createTaskTable = "CREATE TABLE $TABLE_NAME (" +
                "$ID_COL INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$NAME_COL TEXT, " +
                "$DESCRIPTION_COL TEXT, " +
                "$DAY INTEGER, " +
                "$MONTH INTEGER, " +
                "$YEAR INTEGER)"

        val createSubTaskTable = "CREATE TABLE $SUB_TASK_TABLE_NAME (" +
                "$SUB_TASK_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$SUB_TASK_DEADLINED_ID INTEGER, " +
                "$SUB_TASK_NAME TEXT, " +
                "$SUB_TASK_ISDONE INTEGER," +
                "FOREIGN KEY($SUB_TASK_DEADLINED_ID) REFERENCES $TABLE_NAME($ID_COL))"

        db?.execSQL(createTaskTable)
        db?.execSQL(createSubTaskTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $SUB_TASK_TABLE_NAME")
        onCreate(db)
    }

    fun insertRecord(
        task: DeadLinedTask,
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME_COL, task.title)
        values.put(DESCRIPTION_COL, task.description)
        values.put(DAY, task.deadDate.day)
        values.put(MONTH, task.deadDate.month)
        values.put(YEAR, task.deadDate.year)
        return db.insert(TABLE_NAME, null, values)
    }

    fun insertSubTask(
        subTask: SimpleTask,
        taskId: Int
    ) {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(SUB_TASK_NAME, subTask.title)
        values.put(SUB_TASK_ISDONE, subTask.isDone)
        values.put(SUB_TASK_DEADLINED_ID, taskId)
        db.insert(SUB_TASK_TABLE_NAME, null, values)
    }

    fun getAllRecords(): Cursor {
        val db = this.readableDatabase
        return db.query(TABLE_NAME, null, null, null, null, null, null)
    }

    fun getRecord(id: Int): Cursor {
        val db = this.readableDatabase
        return db.query(TABLE_NAME, null, "$ID_COL = ?", arrayOf(id.toString()), null, null, null)
    }

    fun getSubTask(id: Int): Cursor {
        val db = this.readableDatabase
        return db.query(
            SUB_TASK_NAME,
            null,
            "$SUB_TASK_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
    }

    fun updateRecord(task: DeadLinedTask){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME_COL, task.title)
        values.put(DESCRIPTION_COL, task.description)
        values.put(DAY, task.deadDate.day)
        values.put(MONTH, task.deadDate.month)
        values.put(YEAR, task.deadDate.year)
        db.update(TABLE_NAME, values, "$ID_COL = ?", arrayOf(task.id.toString()))
    }

    fun updateSubTask(subTask: SimpleTask){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(SUB_TASK_NAME, subTask.title)
        values.put(SUB_TASK_ISDONE, subTask.isDone)
        db.update(SUB_TASK_TABLE_NAME, values, "$SUB_TASK_ID = ?", arrayOf(subTask.id.toString()))
    }

    // Delete
    fun deleteRecord(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$ID_COL = ?", arrayOf(id.toString()))
        db.delete(SUB_TASK_TABLE_NAME, "$SUB_TASK_DEADLINED_ID = ?", arrayOf(id.toString()))
    }

}

