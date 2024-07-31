package com.example.myapplication.time;

import com.ali.uneversaldatetools.date.JalaliDateTime;

import java.util.Calendar;

public class PeriodicCheckBoxReset {
    static Calendar calendar = Calendar.getInstance();
    static JalaliDateTime jalaliDateTime = JalaliDateTime.Now();
    static int day = jalaliDateTime.getDay();
    static int month = jalaliDateTime.getMonth();
    static int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

    public static boolean checkDay(int isDone, int changeDay,int changeWeek, int changeMonth, String  period) {

        calendar.setFirstDayOfWeek(7);


        switch (period){
            case "daily":
                if (day == changeDay){
                    return (isDone == 1);
                }else {
                    return false;
                }

            case "weekly":
                if (weekOfYear == changeWeek){
                    return (isDone == 1);
                }else {
                    return false;
                }


            case "monthly":

                if (month == changeMonth){
                    return (isDone == 1);
                }else {
                    return false;
                }

        }
        return false;
    }
}
