package com.example.myapplication.database.taskDataBase.habits

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DB_NAME = "monthly_habit"
private const val DB_VERSION: Int = 1

private const val ROUTINE_TABLE_NAME: String = "routine"
private const val ROUTINE_ID_COL: String = "id"
private const val ROUTINE_NAME_COL: String = "name"
private const val ROUTINE_DESCRIPTION_COL: String = "description"
private const val ROUTINE_DAY: String = "day"
private const val ROUTINE_WEEK: String = "week"
private const val ROUTINE_MONTH: String = "month"
private const val ROUTINE_YEAR: String = "year"

private const val DAYS_TABLE_NAME: String = "habits"
private const val DAYS_TABLE_ID: String = "id"
private const val DAYS_ROUTINE_TABLE_ID: String = "routine_id"
private const val DAYS_DONE_COL: String = "isdone"
private const val DAYS_DAY: String = "changeday"
private const val DAYS_WEEK: String = "changeweek"
private const val DAYS_MONTH: String = "changemonth"
private const val DAYS_YEAR: String = "changeyear"

private const val REMINDER_TABLE_NAME: String = "reminder"
private const val REMINDER_ID_COL: String = "id"
private const val REMINDER_ROUTINE_ID_COL: String = "routine_id"
private const val REMINDER_DAYS: String = "day"
private const val REMINDER_HOUR: String = "hour"
private const val REMINDER_MINUTES: String = "minutes"


class MonthlyHabitDB(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {


    override fun onCreate(db: SQLiteDatabase?) {
        val createTaskTable = "CREATE TABLE $ROUTINE_TABLE_NAME (" +
                "$ROUTINE_ID_COL INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$ROUTINE_NAME_COL TEXT, " +
                "$ROUTINE_DESCRIPTION_COL TEXT, " +
                "$ROUTINE_DAY INTEGER, " +
                "$ROUTINE_WEEK INTEGER, " +
                "$ROUTINE_MONTH INTEGER, " +
                "$ROUTINE_YEAR INTEGER)"

        val createDaysTable = "CREATE TABLE $DAYS_TABLE_NAME (" +
                "$DAYS_TABLE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$DAYS_ROUTINE_TABLE_ID INTEGER, " +
                "$DAYS_DONE_COL INTEGER," +
                "$DAYS_DAY INTEGER," +
                "$DAYS_WEEK INTEGER," +
                "$DAYS_MONTH INTEGER," +
                "$DAYS_YEAR INTEGER," +
                "FOREIGN KEY($DAYS_ROUTINE_TABLE_ID) REFERENCES $ROUTINE_TABLE_NAME($ROUTINE_ID_COL))"

        val createReminderTable = "CREATE TABLE $REMINDER_TABLE_NAME (" +
                "$REMINDER_ID_COL INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$REMINDER_ROUTINE_ID_COL INTEGER, " +
                "$REMINDER_DAYS INTEGER, " +
                "$REMINDER_HOUR INTEGER, " +
                "$REMINDER_MINUTES INTEGER, "+
                "FOREIGN KEY($REMINDER_ROUTINE_ID_COL) REFERENCES $ROUTINE_TABLE_NAME($ROUTINE_ID_COL))"


        db!!.execSQL(createTaskTable)
        db.execSQL(createDaysTable)
        db.execSQL(createReminderTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $ROUTINE_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $DAYS_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $REMINDER_TABLE_NAME")
        onCreate(db)
    }


    // Create
    fun insertRecord(
        name: String, description: String, day: Int, week: Int, month: Int, year: Int
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ROUTINE_NAME_COL, name)
        values.put(ROUTINE_DESCRIPTION_COL, description)
        values.put(ROUTINE_DAY,day)
        values.put(ROUTINE_WEEK,week)
        values.put(ROUTINE_MONTH,month)
        values.put(ROUTINE_YEAR,year)
        return db.insert(ROUTINE_TABLE_NAME, null, values)
    }

    fun insertDays(
        routineId: Int, done: Int, day: Int, week: Int, month: Int, year: Int
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(DAYS_ROUTINE_TABLE_ID, routineId)
        values.put(DAYS_DONE_COL, done)
        values.put(DAYS_DAY, day)
        values.put(DAYS_WEEK, week)
        values.put(DAYS_MONTH, month)
        values.put(DAYS_YEAR, year)

        return db.insert(DAYS_TABLE_NAME, null, values)
    }

    fun insertReminder(
        routineId: Int, day: Int, hour: Int, minutes: Int
    ): Long{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(REMINDER_ROUTINE_ID_COL, routineId)
        values.put(REMINDER_DAYS, day)
        values.put(REMINDER_HOUR, hour)
        values.put(REMINDER_MINUTES, minutes)

        return db.insert(REMINDER_TABLE_NAME, null, values)
    }

    // Read
    fun getAllHabit(): Cursor {
        val db = this.readableDatabase
        return db.query(ROUTINE_TABLE_NAME, null, null, null, null, null, null)
    }

    fun getAllMonth(routineId: Int): Cursor? {
        val db = this.readableDatabase
        val cursor = db.query(
            DAYS_TABLE_NAME,
            null,
            "$DAYS_ROUTINE_TABLE_ID = ?", arrayOf(routineId.toString()),
            null, null, null
        )
        cursor?.moveToFirst()
        return cursor
    }

    fun getAllReminder(routineId: Int): Cursor? {
        val db = this.readableDatabase
        val cursor = db.query(
            REMINDER_TABLE_NAME,
            null,
            "$REMINDER_ROUTINE_ID_COL = ?", arrayOf(routineId.toString()),
            null, null, null
        )
        cursor?.moveToFirst()
        return cursor
    }


    fun getRecord(id: Int): Cursor {
        val db = this.readableDatabase
        return db.query(ROUTINE_TABLE_NAME, null, "$ROUTINE_ID_COL = ?", arrayOf(id.toString()), null, null, null)
    }

    fun getWeek(routineId: Int, week: Int, year: Int): Cursor {
        val db = this.readableDatabase
        return db.query(DAYS_TABLE_NAME, null,
            "$DAYS_ROUTINE_TABLE_ID = ? AND $DAYS_WEEK = ? AND $DAYS_YEAR " , arrayOf(routineId.toString(), week.toString(),year.toString()), null, null, null
        )
    }

    fun getReminder(routineId: Int, id: Int): Cursor {
        val db = this.readableDatabase
        return db.query(REMINDER_TABLE_NAME, null, "$REMINDER_ID_COL = ? AND $REMINDER_ROUTINE_ID_COL = ?", arrayOf(id.toString(), routineId.toString()), null, null, null
        )
    }

    fun updateRecord(
        id: Int, name: String, description: String, day: Int, week: Int, month: Int, year: Int
    ){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ROUTINE_NAME_COL, name)
        values.put(ROUTINE_DESCRIPTION_COL, description)
        values.put(ROUTINE_DAY,day)
        values.put(ROUTINE_WEEK,week)
        values.put(ROUTINE_MONTH,month)
        values.put(ROUTINE_YEAR,year)
        db.update(ROUTINE_TABLE_NAME, values, "$ROUTINE_ID_COL = ?", arrayOf(id.toString()))
    }

    fun updateDays(
        id: Int, routineId: Int, done: Int, day: Int, week: Int, month: Int, year: Int
    ){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(DAYS_ROUTINE_TABLE_ID, routineId)
        values.put(DAYS_DONE_COL, done)
        values.put(DAYS_DAY, day)
        values.put(DAYS_WEEK, week)
        values.put(DAYS_MONTH, month)
        values.put(DAYS_YEAR, year)
        db.update(DAYS_TABLE_NAME, values, "$DAYS_TABLE_ID = ?", arrayOf(id.toString()))

    }

    fun updateReminder(
        id: Int, routineId: Int, day: Int, hour: Int, minutes: Int
    ){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(REMINDER_ROUTINE_ID_COL, routineId)
        values.put(REMINDER_DAYS, day)
        values.put(REMINDER_HOUR, hour)
        values.put(REMINDER_MINUTES, minutes)
        db.update(REMINDER_TABLE_NAME, values, "$REMINDER_ID_COL = ?", arrayOf(id.toString()))
    }

    fun deleteRecord(id: Int) {
        val db = this.writableDatabase
        db.delete(ROUTINE_TABLE_NAME, "$ROUTINE_ID_COL = ?", arrayOf(id.toString())
        )
    }

    fun deleteDays(id: Int) {
        val db = this.writableDatabase
        db.delete(DAYS_TABLE_NAME, "$DAYS_ROUTINE_TABLE_ID = ?", arrayOf(id.toString())
        )
    }

    fun deleteReminder(id: Int) {
        val db = this.writableDatabase
        db.delete(REMINDER_TABLE_NAME, "$REMINDER_ROUTINE_ID_COL = ?", arrayOf(id.toString())
        )
    }
}