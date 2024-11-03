package com.example.myapplication.model.tasks.habits;

import androidx.annotation.NonNull;

import com.example.myapplication.time.WithWeekJalaliDateTime;

import java.util.ArrayList;

public class MonthlyHabit extends DailyHabit {
    ArrayList<Integer> daysOfMonth;

    public MonthlyHabit(int id, @NonNull String title, @NonNull String description, int isDone, WithWeekJalaliDateTime createDate, ArrayList<Integer> daysOfMonth) {
        super(id, title, description, isDone, createDate);
        this.daysOfMonth = daysOfMonth;
    }

    public MonthlyHabit(String title, String description, int isDone, WithWeekJalaliDateTime createDate, ArrayList<Integer> daysOfMonth) {
        super(title, description, isDone, createDate);
        this.daysOfMonth = daysOfMonth;
    }

    public MonthlyHabit(String title, String description, WithWeekJalaliDateTime createDate, ArrayList<Integer> daysOfMonth) {
        super(title, description, createDate);
        this.daysOfMonth = daysOfMonth;
    }

    public ArrayList<Integer> getDaysOfMonth() {
        return daysOfMonth;
    }

    public void setDaysOfMonth(ArrayList<Integer> daysOfMonth) {
        this.daysOfMonth = daysOfMonth;
    }
}
