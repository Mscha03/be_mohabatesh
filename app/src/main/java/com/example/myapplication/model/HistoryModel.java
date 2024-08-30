package com.example.myapplication.model;

public class HistoryModel {
    private Period period;
    private int isDone;
    private int changeDay;
    private int changeWeek;
    private int changeMonth;
    private int changeYear;

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }

    public int getChangeDay() {
        return changeDay;
    }

    public void setChangeDay(int changeDay) {
        this.changeDay = changeDay;
    }

    public int getChangeWeek() {
        return changeWeek;
    }

    public void setChangeWeek(int changeWeek) {
        this.changeWeek = changeWeek;
    }

    public int getChangeMonth() {
        return changeMonth;
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

    public HistoryModel(Period period, int isDone, int changeDay, int changeWeek, int changeMonth, int changeYear) {
        this.period = period;
        this.isDone = isDone;
        this.changeDay = changeDay;
        this.changeWeek = changeWeek;
        this.changeMonth = changeMonth;
        this.changeYear = changeYear;


    }
}
