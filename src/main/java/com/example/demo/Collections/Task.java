package com.example.demo.Collections;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "Tasks")
public class Task {

    @Id
    private String id;
    private String title;
    private String description;
    private Date dueDate;
    private String userId; // Foreign key referencing the user's _id from Accounts
    private String category;

    public Task() { }

    public Task(String title, String description, Date dueDate, String userId, String Category) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.userId = userId;
        this.category = Category;
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
