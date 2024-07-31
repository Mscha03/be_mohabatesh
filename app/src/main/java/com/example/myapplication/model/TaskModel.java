package com.example.myapplication.model;

import android.widget.CheckBox;

import com.example.myapplication.Period;

public class TaskModel {
    private CheckBox checkBox;
    private String description;
    private int id = 0;
    private boolean isDone = false;
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

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
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

    public void setDone(boolean done) {
        isDone = done;
    }

    public int getDeadDay() {
        return deadDay;
    }

    public void setDeadDay(int deadDay) {
        this.deadDay = deadDay;
    }

    public int getDeadMonth() {
        return deadMonth;
    }

    public void setDeadMonth(int deadMonth) {
        this.deadMonth = deadMonth;
    }

    public int getDeadYear() {
        return deadYear;
    }

    public void setDeadYear(int deadYear) {
        this.deadYear = deadYear;
    }
}
