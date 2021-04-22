package com.example.securingweb.model;

public class ComplexResult extends Object{
    Project project;
    ProjectTask task;

    public ComplexResult(Project project, ProjectTask task) {
        this.project = project;
        this.task = task;
    }

    public Project getProject() {
        return project;
    }

    public ProjectTask getTask() {
        return task;
    }
}
