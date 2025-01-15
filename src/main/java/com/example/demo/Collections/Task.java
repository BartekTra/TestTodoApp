package com.example.demo.Collections;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Document(collection = "Tasks")
public class Task {

    @Id
    private String id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private LocalDate completionDate;
    private String userId; // Foreign key referencing the user's _id from Accounts
    private String category;
    private Priority priority;
    private Boolean isDone;
    private Boolean isArchived; // Field to track if the task is archived

    public enum Priority {
        HIGH,
        NORMAL,
        LOW
    }

    // Default constructor
    public Task() {
        this.isDone = false; // Default value for new tasks
        this.isArchived = false; // Default value for new tasks
    }

    // Constructor with parameters
    public Task(String title, String description, LocalDate dueDate, LocalDate completionDate, String userId, String category, Priority priority, Boolean isDone, Boolean isArchived) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completionDate = completionDate;
        this.userId = userId;
        this.category = category;
        this.priority = priority;
        this.isDone = isDone;
        this.isArchived = isArchived;
    }

    // Getter for isArchived
    public Boolean isArchived() {
        return isArchived;
    }

    // Setter for isArchived
    public void setArchived(Boolean archived) {
        this.isArchived = archived;
    }

    // Getter for isDone
    public Boolean isDone() {
        return isDone;
    }

    // Setter for isDone
    public void setDone(Boolean done) {
        this.isDone = done;
    }

    // toString method
    @Override
    public String toString() {
        return "Task {" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", completionDate=" + completionDate +
                ", userId='" + userId + '\'' +
                ", category='" + category + '\'' +
                ", priority=" + priority +
                ", isDone=" + isDone +
                ", isArchived=" + isArchived +
                '}';
    }
}
