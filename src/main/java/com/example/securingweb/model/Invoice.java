package com.example.securingweb.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Collection;

@Document(collection = "invoices")
public class Invoice {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ReportedUser getUser() {
        return user;
    }

    public void setUser(ReportedUser user) {
        this.user = user;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Collection<ProjectTask> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<ProjectTask> tasks) {
        this.tasks = tasks;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public LocalDateTime getDueBy() {
        return dueBy;
    }

    public void setDueBy(LocalDateTime dueBy) {
        this.dueBy = dueBy;
    }

    private ReportedUser user;
    private Client client; // vybírá se
    private Collection<ProjectTask> tasks; // vybírá se
    private Project project;
    private LocalDateTime created;
    private String userId;
    private String projectId;
    private String clientId;

    public void addTaskObjects(ProjectTask task){
        this.tasks.add(task);
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Collection<String> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(Collection<String> taskIds) {
        this.taskIds = taskIds;
    }

    private Collection<String> taskIds;

    // datum splatnosti
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dueBy; // vybírá se

}
