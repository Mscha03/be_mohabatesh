package com.example.myapplication.model.tasks.habits;

import androidx.annotation.NonNull;

import com.example.myapplication.model.Period;
import com.example.myapplication.model.tasks.SimpleTask;
import com.example.myapplication.time.WithWeekJalaliDateTime;

public class Habit extends SimpleTask {

    private WithWeekJalaliDateTime createDate;
    private Period period;


    public Habit(int id, @NonNull String title, @NonNull String description, int isDone, WithWeekJalaliDateTime createDate, Period period) {
        super(id, title, description, isDone);
        this.createDate = createDate;
        this.period = period;
    }

    public Habit(String title, String description, int isDone, WithWeekJalaliDateTime createDate, Period period) {
        super(title, description, isDone);
        this.createDate = createDate;
        this.period = period;
    }
    public Habit(String title, String description, WithWeekJalaliDateTime createDate, Period period) {
        super(title, description);
        this.createDate = createDate;
        this.period = period;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public WithWeekJalaliDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(WithWeekJalaliDateTime createDate) {
        this.createDate = createDate;
    }

}
