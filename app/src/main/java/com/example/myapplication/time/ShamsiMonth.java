package com.example.myapplication.time;



import android.content.Context;

import com.example.myapplication.R;

public class ShamsiMonth {

    public static String getMonthName(int monthNumber, Context context){

        String[] monthName = context.getResources().getStringArray(R.array.shamsiMonth);

        if (monthNumber > 0 && monthNumber < 13) {
            return monthName[monthNumber - 1];
        } else {
            return " ";
        }
    }
}
