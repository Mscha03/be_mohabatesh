package com.example.myapplication.recyclerview;

import android.widget.CheckBox;

import com.example.myapplication.Period;

public class PeriodicModel {
    private CheckBox checkBox;
    private String description;
    private Period period;
    private int id = 0;
    private boolean isDone = false;
    private int changeDay;
    private int changeWeek;
    private int changeMonth;

    public PeriodicModel(CheckBox checkBox, String description, Period period, int id, int changeDay, int changeWeek, int changeMonth) {
        this.checkBox = checkBox;
        this.description = description;
        this.period = period;
        this.id = id;
        isDone = checkBox.isChecked();
        this.changeDay = changeDay;
        this.changeWeek = changeWeek;
        this.changeMonth = changeMonth;
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

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
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

    public int getChangeDay() {
        return changeDay;
    }

    public void setChangeDay(int changeDay) {
        this.changeDay = changeDay;
    }

    public int getChangeMonth() {
        return changeMonth;
    }

    public int getChangeWeek() {
        return changeWeek;
    }

    public void setChangeWeek(int changeWeek) {
        this.changeWeek = changeWeek;
    }

    public void setChangeMonth(int changeMonth) {
        this.changeMonth = changeMonth;
    }

}
