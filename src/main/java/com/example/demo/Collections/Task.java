package com.example.demo.Collections;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;

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

    public enum Priority {
        HIGH,
        NORMAL,
        LOW
    }


    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public Task() { }

    public Task(String title, String description, LocalDate dueDate, LocalDate completionDate, String userId, String Category, Priority priority, Boolean isDone) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completionDate = completionDate;
        this.userId = userId;
        this.category = Category;
        this.priority = priority;
        this.isDone = isDone;

    }

    @Override
    public String toString() {
        return "Task { " +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dueDate=" + dueDate + '\'' +
                ", userId='" + userId + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
