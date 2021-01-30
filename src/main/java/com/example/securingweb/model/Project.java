package com.example.securingweb.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collection;


@Entity
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

    public long getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    @Id
    @GeneratedValue // vyzkouset, jestli bude generovat ředu
    private int Id;

    @OneToMany
    public Collection<ProjectTask> tasks;

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
