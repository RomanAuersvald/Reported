package com.example.securingweb.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collection;


@Document(collection = "projects")
public class Project {

    public String getId() {
        return id;
    }

    @Id
    private String id;
    private final String name;
    private final String description;
    private final String owner;

    public Project(String name, String description, String owner){
        this.name = name;
        this.description = description;
        this.owner = owner;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getOwner() {
        return owner;
    }

    // pokaždé vytváříš nové ObjId proto se ti to pořád mění
    public String getObjectId() { return ObjectId.get().toHexString();
    }


}