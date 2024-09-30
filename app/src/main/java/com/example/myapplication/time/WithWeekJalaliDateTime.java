package com.example.myapplication.time;

import com.ali.uneversaldatetools.date.JalaliDateTime;

public class WithWeekJalaliDateTime extends JalaliDateTime {

    int week;

    public WithWeekJalaliDateTime(int year, int month, int week, int day) {
        super(year, month, day);
        this.week = week;
    }

    public WithWeekJalaliDateTime(int year, int month, int day, int hour, int min, int sec, java.util.TimeZone timeZone) {
        super(year, month, day, hour, min, sec, timeZone);
    }

    public WithWeekJalaliDateTime(int days) {
        super(days);
    }

    public WithWeekJalaliDateTime(int days, int hour, int min, int sec, java.util.TimeZone timeZone) {
        super(days, hour, min, sec, timeZone);
    }

    public WithWeekJalaliDateTime(int unixTimeSeconds, java.util.TimeZone timeZone) {
        super(unixTimeSeconds, timeZone);
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }
}
