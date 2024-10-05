package com.example.myapplication.time;


import android.content.Context;

import com.example.myapplication.R;

public class ShamsiName {

    public static String getMonthName(int monthNumber, Context context) {

        String[] monthName = context.getResources().getStringArray(R.array.shamsiMonth);

        if (monthNumber > 0 && monthNumber < 13) {
            return monthName[monthNumber - 1];
        } else {
            return " ";
        }
    }

    public static String getDayName(int dayNumber, Context context) {
        String[] dayName = context.getResources().getStringArray(R.array.shamsiWeekDay);
        if (dayNumber >= 0 && dayNumber < 8) {
            return dayName[dayNumber];
        } else {
            return " ";
    }
}
}
