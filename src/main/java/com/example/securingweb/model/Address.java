package com.example.securingweb.model;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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

    @NotEmpty(message = "Enter street name")
    private String street;
    @NotEmpty(message = "Enter city name")
    private String city;
    @Min(value = 10000, message = "Set real post code, format: xxxxx")
    @Max(value = 99999)
    private String postCode;
    private String ownerId; // client or user

    public Address(){

    }
    public Address(ARESClient client){
        this.city = client.getCityName();;
        this.street = client.getStreetName();
        this.postCode = String.valueOf(client.getPSC());
    }

}
