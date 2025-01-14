package com.example.demo.service;

import com.example.demo.Collections.Task;
import com.example.demo.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TaskService provides business logic and operations for Task entities.
 */
@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * Retrieves all tasks from the database.
     *
     * @return a list of all tasks.
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Adds a new task to the database.
     *
     * @param task the task to add.
     */
    public void AddTask(Task task) {
        taskRepository.save(task);
    }

    /**
     * Finds tasks associated with a specific user ID.
     *
     * @param userId the ID of the user.
     * @return a list of tasks belonging to the specified user.
     */
    public List<Task> findByUserId(String userId) {
        return taskRepository.findByUserId(userId);
    }

    /**
     * Finds a task by its ID.
     *
     * @param taskId the ID of the task.
     * @return the Task object if found, otherwise null.
     */
    public Task findById(String taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    /**
     * Updates an existing task in the database.
     *
     * @param task the task to update.
     */
    public void updateTask(Task task) {
        taskRepository.save(task);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param taskId the ID of the task to delete.
     */
    public void deleteTask(String taskId) {
        taskRepository.deleteById(taskId);
    }
}

