package com.example.securingweb.model;

public class ProjectTask {

    private String name;

    public ProjectTask(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public ProjectTask() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;
}
