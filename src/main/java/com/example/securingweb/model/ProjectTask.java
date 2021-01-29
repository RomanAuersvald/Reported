package com.example.securingweb.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ProjectTask {
    @Id
    @GeneratedValue
    private int Id;

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
