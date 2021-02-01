package com.example.securingweb.model;


import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Document(collection = "projectTasks")
public class ProjectTask {

    public ProjectTask() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    private String id;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String taskName;
    private String description;
    private String projectId;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;

    public Double getHourRate() {
        return hourRate;
    }

    public void setHourRate(Double hourRate) {
        this.hourRate = hourRate;
    }

    private Double hourRate;

    public ProjectTask(String taskName, String description, String projectId, LocalDateTime startDate, LocalDateTime endDate){ // @NotNull
        this.taskName = taskName;
        this.description = description;
        this.projectId = projectId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Boolean taskComplete(){
        return endDate != null;
    }

    public Long getTaskDuration(){
        return ChronoUnit.SECONDS.between(startDate, endDate);
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectId() {
        return projectId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
