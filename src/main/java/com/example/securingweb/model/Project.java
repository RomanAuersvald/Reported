package com.example.securingweb.model;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collection;


@Document(collection = "projects")
public class Project {

    @Id
    private int id;
    private final String name;
    private final String description;
    private final String owner;

    public Project(int id, String name, String description, String owner){
        this.name = name;
        this.description = description;
        this.owner = owner;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getOwner() {
        return owner;
    }



}