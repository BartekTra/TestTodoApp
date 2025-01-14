package com.example.demo.controllers;

import com.example.demo.Collections.Task;
import com.example.demo.Repository.TaskRepository;
import com.example.demo.service.TaskService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class for handling authenticated task-related API queries.
 * Provides endpoints for retrieving, adding, updating, and deleting tasks.
 */
@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class AuthenticatedQuery {

    @Autowired
    private TaskService taskService;

    /**
     * Retrieves all tasks associated with the authenticated user.
     *
     * @param token the Bearer token from the Authorization header.
     * @return a {@link ResponseEntity} containing a list of {@link Task} objects.
     */
    @GetMapping("/GetTasks")
    public ResponseEntity<List<Task>> getTasks(@RequestHeader("Authorization") String token) {
        String userId = getUserIdFromToken(token.replace("Bearer ", ""));
        List<Task> tasks = taskService.findByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Extracts the user ID from a JWT token.
     *
     * @param token the JWT token.
     * @return the user ID.
     */
    private String getUserIdFromToken(String token) {
        return Jwts.parser()
                .setSigningKey("secret") // Replace with your secret key
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Adds a new task for the authenticated user.
     *
     * @param authorizationHeader the Bearer token from the Authorization header.
     * @param task the {@link Task} object to add.
     * @return a {@link ResponseEntity} containing a success message or error details.
     */
    @PostMapping("/AddTask")
    public ResponseEntity<Map<String, Object>> AddTask(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody Task task) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Token is missing or invalid"));
        }
        String token = authorizationHeader.substring(7);
        String userId;
        try {
            userId = getUserIdFromToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }
        task.setUserId(userId);
        if (task.getPriority() == null) {
            task.setPriority(Task.Priority.NORMAL);
        }
        task.setIsDone(false);
        taskService.AddTask(task);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Task added successfully!");
        return ResponseEntity.ok(response);
    }

    /**
     * Marks a task as done for the authenticated user.
     *
     * @param authorizationHeader the Bearer token from the Authorization header.
     * @param taskId the ID of the task to mark as done.
     * @return a {@link ResponseEntity} containing a success message or error details.
     */
    @PostMapping("/MarkTaskAsDone/{taskId}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Map<String, Object>> markTaskAsDone(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable String taskId) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Token is missing or invalid"));
        }
        String token = authorizationHeader.substring(7);
        String userId;
        try {
            userId = getUserIdFromToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }
        Task task = taskService.findById(taskId);
        if (task == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Task not found"));
        }
        if (!task.getUserId().equals(userId)) {
            return ResponseEntity.status(403).body(Map.of("error", "Unauthorized to modify this task"));
        }
        task.setIsDone(true);
        task.setCompletionDate(LocalDate.now());
        taskService.updateTask(task);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Task marked as done successfully!");
        return ResponseEntity.ok(response);
    }

    /**
     * Generates a task report for the authenticated user.
     *
     * @param token the Bearer token from the Authorization header.
     * @return a {@link ResponseEntity} containing task statistics.
     */
    @GetMapping("/GetTaskReport")
    public ResponseEntity<Map<String, Object>> getTaskReport(@RequestHeader("Authorization") String token) {
        String userId;
        try {
            userId = getUserIdFromToken(token.replace("Bearer ", ""));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }
        List<Task> tasks = taskService.findByUserId(userId);
        int inProgress = 0;
        int overdue = 0;
        int completedOnTime = 0;
        int allTasks = 0;
        for (Task task : tasks) {
            LocalDate dueDate = task.getDueDate();
            LocalDate completionDate = task.getCompletionDate();
            if (task.getIsDone()) {
                if (dueDate != null && completionDate != null && dueDate.isAfter(completionDate)) {
                    completedOnTime++;
                }
            } else {
                if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
                    overdue++;
                } else {
                    inProgress++;
                }
            }
            allTasks++;
        }
        Map<String, Object> report = new HashMap<>();
        report.put("inProgress", inProgress);
        report.put("overdue", overdue);
        report.put("completedOnTime", completedOnTime);
        report.put("allTasks", allTasks);
        return ResponseEntity.ok(report);
    }

    /**
     * Deletes a task for the authenticated user.
     *
     * @param authorizationHeader the Bearer token from the Authorization header.
     * @param taskId the ID of the task to delete.
     * @return a {@link ResponseEntity} containing a success message or error details.
     */
    @DeleteMapping("/DeleteTask/{taskId}")
    public ResponseEntity<Map<String, Object>> deleteTask(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable String taskId) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Token is missing or invalid"));
        }
        String token = authorizationHeader.substring(7);
        String userId;
        try {
            userId = getUserIdFromToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }
        Task task = taskService.findById(taskId);
        if (task == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Task not found"));
        }
        if (!task.getUserId().equals(userId)) {
            return ResponseEntity.status(403).body(Map.of("error", "Unauthorized to delete this task"));
        }
        taskService.deleteTask(taskId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Task deleted successfully!");
        return ResponseEntity.ok(response);
    }
}
