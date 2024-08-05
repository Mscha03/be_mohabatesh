package com.example.myapplication.model;

import android.widget.CheckBox;

public class SimpleModel {
    private CheckBox checkBox;
    private String description;
    private int id = 0;
    private boolean isDone = false;

    public SimpleModel(CheckBox checkBox, String description, int id) {
        this.checkBox = checkBox;
        this.description = description;
        this.id = id;
        isDone = checkBox.isChecked();
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

}
