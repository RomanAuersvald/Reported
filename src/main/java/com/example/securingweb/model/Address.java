package com.example.securingweb.model;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "addresses")
public class Address {
    public void setId(String id) {
        this.id = id;
    }

    @Id
    public String id;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String clientId) {
        this.ownerId = clientId;
    }

    private String street;
    private String city;
    private String postCode;
    private String ownerId; // client or user

}
