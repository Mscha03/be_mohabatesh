package com.example.myapplication.database;

import static com.example.myapplication.database.GetDates.jalaliDateTime;

import com.ali.uneversaldatetools.date.JalaliDateTime;
import com.example.myapplication.database.taskDataBase.habits.DailyHabitDB;
import com.example.myapplication.database.taskDataBase.habits.MonthlyHabitDB;
import com.example.myapplication.database.taskDataBase.habits.WeeklyHabitDB;
import com.example.myapplication.model.Period;
import com.example.myapplication.time.WithWeekJalaliDateTime;

import java.util.ArrayList;

import java.util.Date;
import java.util.GregorianCalendar;

import ir.huri.jcal.JalaliCalendar;

public interface AddInformationForHistory {

    static void addDays(String period, long id, DailyHabitDB db){

        JalaliDateTime jalaliDateTime = JalaliDateTime.Now();

        if (period.equals(Period.daily.toString())){
            for (int i = 1; i <= 6 ; i++) {
                for (int j = 1; j <= 31; j++) {
                    db.insertDays((int)id, 0, j, 0, i, jalaliDateTime.getYear());
                }
            }
            for (int i = 7; i <= 11 ; i++) {
                for (int j = 1; j <= 30; j++) {
                    db.insertDays((int)id, 0, j, 0, i, jalaliDateTime.getYear());
                }
            }
            if ((jalaliDateTime.getYear() % 4 ) == 3) {  //kabise => 366 days in year
                for (int j = 1; j <= 30; j++) {
                    db.insertDays((int) id, 0, j, 0, 12, jalaliDateTime.getYear());
                }
            }else { // normal => 365 day in year
                for (int j = 1; j <= 29; j++) {
                    db.insertDays((int) id, 0, j, 0, 12, jalaliDateTime.getYear());
                }
            }

        }
        else if (period.equals(Period.weekly.toString())){
            for (int i = 1; i <= 52; i++) {
                db.insertDays((int)id, 0, 0, i, 0, jalaliDateTime.getYear());
            }


        }
        else if (period.equals(Period.monthly.toString())){
            for (int i = 1; i <= 12; i++) {
                db.insertDays((int)id, 0, 0, 0, i, jalaliDateTime.getYear());
            }
        }

    }

    static void addForDaily(Integer id, DailyHabitDB db, WithWeekJalaliDateTime createDate){

        int day = createDate.getDay();
        int dayLimit = 31;

        int month = createDate.getMonth();
        int monthLimit = 12;

        int year = createDate.getYear();

        boolean thisMonth = true;

        for (; month <= monthLimit; month++) {

            if (month <= 6){
                dayLimit = 31;
            } else if (month <= 11){
                dayLimit = 30;
            } else if ((jalaliDateTime.getYear() % 4 ) == 3){
                dayLimit = 30;
            } else {
                dayLimit = 29;
            }

            if (thisMonth){
                for ( ; day<=dayLimit; day++ ) {
                    db.insertDays(id, 0, day, 0, month, year);
                }
                thisMonth = false;

            }else {
                for (day = 1; day <= dayLimit; day++) {
                    db.insertDays(id, 0, day, 0, month, year);
                }
            }
        }


    }

    static void addForWeekly(Integer id, WeeklyHabitDB db, WithWeekJalaliDateTime createDate, ArrayList<Integer> daysOfWeek){
        int week = createDate.getWeek();
        int weekLimit = 53;
        int year = createDate.getYear();

        for (; week <= weekLimit; week++) {
            for (int day = 1; day <= 7; day++){
                if (daysOfWeek.contains(day)){
                    db.insertWeeks(id, 0, day, week, 0, year );
                }
            }
        }
    }

    static void addForMonthly(Integer id, MonthlyHabitDB db, WithWeekJalaliDateTime createDate, ArrayList<Integer> daysOfMonth){
        int month = createDate.getMonth();
        int monthLimit = 12;
        int year = createDate.getYear();

        for (; month <= monthLimit; month++) {
            for (int day = 1; day <= 31; day++){
                if (daysOfMonth.contains(day)){
                    db.insertDays(id, 0, day, 0, month, year );
                }
            }
        }

    }

    public static int getPersianWeekOfYear(JalaliCalendar jalaliCalendar) {

        // تبدیل تاریخ به میلادی
        GregorianCalendar gregorianCalendar = jalaliCalendar.toGregorian();

        // اول فروردین به میلادی
        JalaliCalendar startOfYearJalali = new JalaliCalendar(jalaliCalendar.getYear(), 1, 1);
        GregorianCalendar startOfYearJalaliGregorian = startOfYearJalali.toGregorian();

        // محاسبه اختلاف روزها
        long t = gregorianCalendar.getTimeInMillis();
        long s = startOfYearJalaliGregorian.getTimeInMillis();
        long diffInMillis = gregorianCalendar.getTimeInMillis() - startOfYearJalaliGregorian.getTimeInMillis();


        // تعداد روزها از اول سال
        long daysSinceStartOfYear = (diffInMillis / (1000 * 60 * 60 * 24));

        // محاسبه شماره هفته
        return (int) ((daysSinceStartOfYear / 7) + 1);


    }

}
