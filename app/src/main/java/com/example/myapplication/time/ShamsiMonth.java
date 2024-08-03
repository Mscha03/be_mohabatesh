package com.example.myapplication.time;



import android.content.Context;

import com.example.myapplication.R;

public class ShamsiMonth {

    public static String getMonthName(int monthNumber, Context context){

        String[] monthName = context.getResources().getStringArray(R.array.shamsiMonth);

        return monthName[monthNumber - 1];
    }
}
