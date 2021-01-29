package com.example.securingweb.model;

import java.util.Collection;

public class Project {

    public Project(){

    }

    public Project(String name, String description, ReportedUser owner) {
        this.name = name;
        this.description = description;
        this.owner = owner;
    }

    public Collection<ProjectTask> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<ProjectTask> tasks) {
        this.tasks = tasks;
    }

    public void addTask(ProjectTask task){
        Collection<ProjectTask> tasks = getTasks();
        tasks.add(task);
        setTasks(tasks);
    }

    private Collection<ProjectTask> tasks;

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

    private String name;
    private String description;

    public ReportedUser getOwner() {
        return owner;
    }

    public void setOwner(ReportedUser owner) {
        this.owner = owner;
    }

    private ReportedUser owner;


}
