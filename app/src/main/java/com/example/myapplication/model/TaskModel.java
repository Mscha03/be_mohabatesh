package com.example.myapplication.model;

import android.widget.CheckBox;

public class TaskModel {
    private CheckBox checkBox;
    private String description;
    private int id;
    private boolean isDone;
    private int deadDay;
    private int deadMonth;
    private int deadYear;

    public TaskModel(CheckBox checkBox, String description, int id, int deadDay, int deadMonth, int deadYear) {
        this.checkBox = checkBox;
        this.description = description;
        this.id = id;
        isDone = checkBox.isChecked();
        this.deadDay = deadDay;
        this.deadMonth = deadMonth;
        this.deadYear = deadYear;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDone() {
        return isDone;
    }

    public int getDeadDay() {
        return deadDay;
    }

    public int getDeadMonth() {
        return deadMonth;
    }

    public int getDeadYear() {
        return deadYear;
    }
}
