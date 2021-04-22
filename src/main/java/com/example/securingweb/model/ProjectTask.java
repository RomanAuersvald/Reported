package com.example.securingweb.model;


import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Document(collection = "projectTasks")
public class ProjectTask {

    public Boolean hasOngoingEntry(){
        if (getLastEntry() != null){
            return (getLastEntry().getEndDate() == null ? true : false);
        }else{
            return false;
        }
    }

    public ProjectTaskEntry getLastEntry(){
        if (getTaskEntries() != null){
            return getTaskEntries().get(getTaskEntries().size() -1);
        }else{
            return null;
        }
    }

    public void endLastTaskEntry(){
        getTaskEntries().get(getTaskEntries().size() -1).endEntry();
    }

    public void newTaskEntry(){
        if (taskEntries == null){
            taskEntries = new ArrayList<ProjectTaskEntry>();
        }
        taskEntries.add(new ProjectTaskEntry());
    }

    public List<ProjectTaskEntry> getTaskEntries(){
        if (taskEntries != null){
            Collections.sort(taskEntries);
        }
        return taskEntries;
    }


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

    @NotEmpty(message = "Just fill that name, it will be easier to recognize this task.")
    private String taskName;
    private String description;
    private String shortDescription;
    private String projectId;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;

    private List<ProjectTaskEntry> taskEntries;

    private Integer taskDuration = 0;
    private Double hourRate;

    public ProjectTask(String taskName, String description, String projectId, LocalDateTime startDate, LocalDateTime endDate){ // @NotNull
        this.taskName = taskName;
        this.description = description;
        this.projectId = projectId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.taskEntries = new ArrayList<>();
    }

    public Boolean taskComplete(){
        return endDate != null;
    }

    public Long returnTaskDuration(){
        Long elapsed = 0L;
        for (ProjectTaskEntry entry : taskEntries){
            if (entry.returnTaskEntryDuration() != null)
                elapsed += entry.returnTaskEntryDuration();
        }
        return elapsed;
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
    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
    public Double getHourRate() {
        return hourRate;
    }

    public void setHourRate(Double hourRate) {
        this.hourRate = hourRate;
    }
}
