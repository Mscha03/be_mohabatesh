package com.example.myapplication.model;

import android.widget.CheckBox;

import com.example.myapplication.Period;

public class TaskModel {
    private CheckBox checkBox;
    private String description;
    private int id = 0;
    private boolean isDone = false;
    private int deadDay;
    private int deadWeek;
    private int deadMonth;

    public TaskModel(CheckBox checkBox, String description, Period period, int id, int deadDay, int deadWeek, int deadMonth) {
        this.checkBox = checkBox;
        this.description = description;
        this.id = id;
        isDone = checkBox.isChecked();
        this.deadDay = deadDay;
        this.deadWeek = deadWeek;
        this.deadMonth = deadMonth;
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

    public int getDeadWeek() {
        return deadWeek;
    }

    public void setDeadWeek(int deadWeek) {
        this.deadWeek = deadWeek;
    }

    public void setDeadMonth(int deadMonth) {
        this.deadMonth = deadMonth;
    }

}
