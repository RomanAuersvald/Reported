package com.example.securingweb.model;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Validated
@Document(collection = "users")
public class ReportedUser implements UserDetails {


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

    public String getId() {
        return id;
    }

    @Id
    private String id;


    @Pattern(regexp="^[a-zA-Z]{2,20}",message="Invalid name pattern")
    private String firstName;
    @Pattern(regexp="^[a-zA-Z]{2,20}",message="Invalid surname pattern")
    private String lastName;
    @Pattern(regexp="^[a-zA-Z]{3,20}",message="Invalid username pattern")
    private String username;
    @Size(min = 5, message = "Password must be at least 5 characters long")
    private String password;
    private String role;

    public ReportedUser() {}

    public ReportedUser(String username, String password, String role, String name, String surname){
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstName = name;
        this.lastName = surname;
    }

    @Override
    public String toString() {
        return String.format(
                "User[firstName='%s', lastName='%s', role='%s', username='%s']",
                firstName, lastName, role, username);
    }

    public String getNiceNameAndLastname(){
        return String.format(
                "%s %s",
                firstName, lastName);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        getRoleList().forEach(p -> {GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + p);
            authorities.add(authority);
        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username){
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public List<String> getRoleList(){
        if(this.role.length() > 0) {
            return Arrays.asList(this.role.split(","));
        }
        return new ArrayList<>();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
