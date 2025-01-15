package com.example.demo.service;

import com.example.demo.Collections.Task;
import com.example.demo.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void AddTask(Task task) {
        taskRepository.save(task);
    }

    public List<Task> findByUserId(String userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task findById(String taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    public void updateTask(Task task) {
        taskRepository.save(task);
    }

    public void deleteTask(String taskId) {
        taskRepository.deleteById(taskId);
    }

    public void archiveTask(Task task) {
        task.setIsDone(true); // Mark as done
        task.setCategory("Archived"); // Set category to Archived (optional)
        taskRepository.save(task);
    }

    public List<Task> findArchivedTasksByUserId(String userId) {
        // Find tasks that are archived and belong to the user
        return taskRepository.findByUserIdAndIsDone(userId, true);
    }
}
