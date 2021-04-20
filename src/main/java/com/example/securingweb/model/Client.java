package com.example.securingweb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Document(collection = "clients")
public class Client {


    @Id
    public String id;
    @NotEmpty(message = "Enter customers first name")
    private String firstName;
    @NotEmpty(message = "Enter customers last name")
    private String lastName;
    @NotEmpty(message = "Enter customer company name")
    private String companyName;
    @NotEmpty(message = "Enter valid ICO")
    private String ico;
    @NotEmpty(message = "Enter valid DIC")
    private String dic;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getDic() {
        return dic;
    }

    public void setDic(String dic) {
        this.dic = dic;
    }


    public Client(ARESClient client) {
        setFirstName(" ");
        setLastName(" ");
        setDic("CZ" + client.getICO()); // opravdu to tak funguje??????
        setIco(client.getICO());
        setCompanyName(client.getCompanyName());
    }

    public Client() {

    }
}