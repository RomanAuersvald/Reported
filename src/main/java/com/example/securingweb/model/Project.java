package com.example.securingweb.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
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

    public Project(String name, String description, String ownerId){
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
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