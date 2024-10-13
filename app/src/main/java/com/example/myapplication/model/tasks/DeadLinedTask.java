package com.example.myapplication.model.tasks;

import com.ali.uneversaldatetools.date.JalaliDateTime;

import java.util.ArrayList;
import java.util.List;

public class DeadLinedTask extends SpecialDay{

    private List<SimpleTask> subTask = new ArrayList<>();

    public DeadLinedTask(int id, String title, String description, int isDone, JalaliDateTime deadDate) {
        super(id, title, description, isDone, deadDate);
    }

    public DeadLinedTask(String title, String description, int isDone, JalaliDateTime deadDate) {
        super(title, description, isDone, deadDate);
    }

    public DeadLinedTask(String title, String description, JalaliDateTime deadDate) {
        super(title, description, deadDate);
    }

    public List<SimpleTask> getSubTask() {
        return subTask;
    }

    public void setSubTask(List<SimpleTask> subTask) {
        this.subTask = subTask;
    }
}
