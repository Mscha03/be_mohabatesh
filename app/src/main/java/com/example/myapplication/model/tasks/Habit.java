package com.example.myapplication.model.tasks;

import androidx.annotation.NonNull;

import com.ali.uneversaldatetools.date.JalaliDateTime;

public class Habit extends SimpleTask{

    private JalaliDateTime createDate;

    public Habit(int id, @NonNull String title, @NonNull String description, int isDone, JalaliDateTime createDate) {
        super(id, title, description, isDone);
        this.createDate = createDate;
    }

    public Habit(String title, String description, int isDone, JalaliDateTime createDate) {
        super(title, description, isDone);
        this.createDate = createDate;
    }
    public Habit(String title, String description, JalaliDateTime createDate) {
        super(title, description);
        this.createDate = createDate;
    }

    public JalaliDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(JalaliDateTime createDate) {
        this.createDate = createDate;
    }
}
