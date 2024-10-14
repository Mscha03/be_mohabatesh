package com.example.myapplication.model.tasks.habits;

import androidx.annotation.NonNull;

import com.example.myapplication.model.Period;
import com.example.myapplication.time.WithWeekJalaliDateTime;

public class MonthlyWeek extends Habit{
    int dayOfMonth;

    public MonthlyWeek(int id, @NonNull String title, @NonNull String description, int isDone, WithWeekJalaliDateTime createDate, int dayOfMonth) {
        super(id, title, description, isDone, createDate, Period.monthly);
        this.dayOfMonth = dayOfMonth;
    }

    public MonthlyWeek(String title, String description, int isDone, WithWeekJalaliDateTime createDate, int dayOfMonth) {
        super(title, description, isDone, createDate, Period.monthly);
        this.dayOfMonth = dayOfMonth;
    }

    public MonthlyWeek(String title, String description, WithWeekJalaliDateTime createDate, int dayOfMonth) {
        super(title, description, createDate, Period.monthly);
        this.dayOfMonth = dayOfMonth;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
}
