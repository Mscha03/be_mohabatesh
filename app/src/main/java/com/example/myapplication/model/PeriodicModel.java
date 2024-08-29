package com.example.myapplication.model;
import com.example.myapplication.Period;
import com.example.myapplication.customwidget.MultiStateCheckBox;

public class PeriodicModel {
    private MultiStateCheckBox checkBox;
    private String description;
    private Period period;
    private int id;
    private int isDone;
    private int changeDay;
    private int changeWeek;
    private int changeMonth;
    private int changeYear;


    public PeriodicModel(MultiStateCheckBox checkBox, String description, Period period, int id, int changeDay, int changeWeek, int changeMonth, int changeYear) {
        this.checkBox = checkBox;
        this.description = description;
        this.period = period;
        this.id = id;
        isDone = checkBox.getState();
        this.changeDay = changeDay;
        this.changeWeek = changeWeek;
        this.changeMonth = changeMonth;
        this.changeYear = changeYear;
    }

    public MultiStateCheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(MultiStateCheckBox checkBox) {
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

    public int isDone() {
        return isDone;
    }

    public void setDone(int done) {
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

    public int getChangeYear() {
        return changeYear;
    }

    public void setChangeYear(int changeYear) {
        this.changeYear = changeYear;
    }
}
