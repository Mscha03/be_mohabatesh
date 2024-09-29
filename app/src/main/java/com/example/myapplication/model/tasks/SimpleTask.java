package com.example.myapplication.model.tasks;

public class SimpleTask {
    private int id = 0;
    private String title;
    private String description = "";
    private int isDone = 0;

    public SimpleTask(int id, String title, String description, int isDone) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isDone = isDone;
    }

    public SimpleTask(String title, String description, int isDone) {
        this.title = title;
        this.description = description;
        this.isDone = isDone;
    }

    public SimpleTask(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }
}
