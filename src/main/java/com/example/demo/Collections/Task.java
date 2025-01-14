package com.example.demo.Collections;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;

/**
 * Represents a task stored in the "Tasks" MongoDB collection.
 */
@Getter
@Setter
@Document(collection = "Tasks")
public class Task {

    /**
     * Unique identifier for the task.
     */
    @Id
    private String id;

    /**
     * Title of the task.
     */
    private String title;

    /**
     * Detailed description of the task.
     */
    private String description;

    /**
     * Due date for the task.
     */
    private LocalDate dueDate;

    /**
     * Completion date of the task.
     */
    private LocalDate completionDate;

    /**
     * ID of the user who owns the task.
     */
    private String userId;

    /**
     * Category of the task.
     */
    private String category;

    /**
     * Priority level of the task.
     */
    private Priority priority;

    /**
     * Status indicating whether the task is completed.
     */
    private Boolean isDone;

    /**
     * Enum representing task priority levels.
     */
    public enum Priority {
        HIGH,
        NORMAL,
        LOW
    }

    /**
     * Default constructor.
     */
    public Task() { }

    /**
     * Constructor to create a Task object.
     *
     * @param title           the title of the task.
     * @param description     the description of the task.
     * @param dueDate         the due date of the task.
     * @param completionDate  the completion date of the task.
     * @param userId          the user ID associated with the task.
     * @param category        the category of the task.
     * @param priority        the priority of the task.
     * @param isDone          the completion status of the task.
     */
    public Task(String title, String description, LocalDate dueDate, LocalDate completionDate,
                String userId, String category, Priority priority, Boolean isDone) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completionDate = completionDate;
        this.userId = userId;
        this.category = category;
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
