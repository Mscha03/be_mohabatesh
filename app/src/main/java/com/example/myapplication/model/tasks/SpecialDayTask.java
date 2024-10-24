package com.example.myapplication.model.tasks;

import com.ali.uneversaldatetools.date.JalaliDateTime;

public class SpecialDayTask extends SimpleTask{

    private JalaliDateTime deadDate;

    public SpecialDayTask(int id, String title, String description, int isDone, JalaliDateTime deadDate) {
        super(id, title, description, isDone);
        this.deadDate = deadDate;
    }

    public SpecialDayTask(String title, String description, int isDone, JalaliDateTime deadDate) {
        super(title, description, isDone);
        this.deadDate = deadDate;
    }

    public SpecialDayTask(String title, String description, JalaliDateTime deadDate) {
        super(title, description);
        this.deadDate = deadDate;
    }

    public JalaliDateTime getDeadDate() {
        return deadDate;
    }

    public void setDeadDate(JalaliDateTime deadDate) {
        this.deadDate = deadDate;
    }
}
