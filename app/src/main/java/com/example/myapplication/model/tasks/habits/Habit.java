package com.example.myapplication.model.tasks.habits;

import androidx.annotation.NonNull;

import com.example.myapplication.model.Period;
import com.example.myapplication.model.tasks.SimpleTask;
import com.example.myapplication.time.WithWeekJalaliDateTime;

public class Habit extends SimpleTask {

    private WithWeekJalaliDateTime createDate;



    public Habit(int id, @NonNull String title, @NonNull String description, int isDone, WithWeekJalaliDateTime createDate) {
        super(id, title, description, isDone);
        this.createDate = createDate;
    }

    public Habit(String title, String description, int isDone, WithWeekJalaliDateTime createDate) {
        super(title, description, isDone);
        this.createDate = createDate;
    }
    public Habit(String title, String description, WithWeekJalaliDateTime createDate) {
        super(title, description);
        this.createDate = createDate;
    }


    public WithWeekJalaliDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(WithWeekJalaliDateTime createDate) {
        this.createDate = createDate;
    }

}
