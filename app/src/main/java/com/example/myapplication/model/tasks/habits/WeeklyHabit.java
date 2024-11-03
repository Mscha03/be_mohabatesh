package com.example.myapplication.model.tasks.habits;

import androidx.annotation.NonNull;

import com.example.myapplication.time.WithWeekJalaliDateTime;

import java.util.ArrayList;

public class WeeklyHabit extends DailyHabit {

    ArrayList<Integer> daysOfWeek;

    public WeeklyHabit(int id, @NonNull String title, @NonNull String description, int isDone, WithWeekJalaliDateTime createDate, ArrayList<Integer> daysOfWeek) {
        super(id, title, description, isDone, createDate);
        this.daysOfWeek = daysOfWeek;
    }

    public WeeklyHabit(String title, String description, int isDone, WithWeekJalaliDateTime createDate, ArrayList<Integer> daysOfWeek) {
        super(title, description, isDone, createDate);
        this.daysOfWeek = daysOfWeek;
    }

    public WeeklyHabit(String title, String description, WithWeekJalaliDateTime createDate, ArrayList<Integer> daysOfWeek) {
        super(title, description, createDate);
        this.daysOfWeek = daysOfWeek;
    }

    public ArrayList<Integer> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(ArrayList<Integer> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }
}
