package com.example.myapplication.model.tasks.Habits;

import androidx.annotation.NonNull;

import com.example.myapplication.model.Period;
import com.example.myapplication.time.WithWeekJalaliDateTime;

public class WeeklyHabit extends Habit {

    Integer dayOfWeek;

    public WeeklyHabit(int id, @NonNull String title, @NonNull String description, int isDone, WithWeekJalaliDateTime createDate, Integer dayOfWeek ) {
        super(id, title, description, isDone, createDate, Period.weekly);
        this.dayOfWeek = dayOfWeek;
    }

    public WeeklyHabit(String title, String description, int isDone, WithWeekJalaliDateTime createDate, Integer dayOfWeek) {
        super(title, description, isDone, createDate, Period.weekly);
        this.dayOfWeek = dayOfWeek;
    }

    public WeeklyHabit(String title, String description, WithWeekJalaliDateTime createDate, Integer dayOfWeek) {
        super(title, description, createDate, Period.weekly);
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
