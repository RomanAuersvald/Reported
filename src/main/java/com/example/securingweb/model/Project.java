package com.example.securingweb.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;


@Document(collection = "projects")
public class Project {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    private String id;
    private final String name;
    private final String description;
    private final String ownerId;
    private Double estimatedHours;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime projectStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime projectEnd;

    public Double getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(Double estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public LocalDateTime getProjectStart() {
        return projectStart;
    }

    public LocalDateTime getProjectEnd() {
        return projectEnd;
    }

    public void setProjectEnd(LocalDateTime projectEnd) {
        this.projectEnd = projectEnd;
    }

    public Project(String name, String description, String ownerId, LocalDateTime projectStart){
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.projectStart = projectStart;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getOwnerId() {
        return ownerId;
    }



}