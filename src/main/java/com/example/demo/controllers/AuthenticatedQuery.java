package com.example.demo.controllers;

import com.example.demo.Collections.Task;
import com.example.demo.service.TaskService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class AuthenticatedQuery {

    @Autowired
    private TaskService taskService;

    @GetMapping("/GetTasks")
    public ResponseEntity<List<Task>> getTasks(@RequestHeader("Authorization") String token) {
        String userId = getUserIdFromToken(token.replace("Bearer ", ""));
        List<Task> tasks = taskService.findByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/GetArchivedTasks")
    public ResponseEntity<List<Task>> getArchivedTasks(@RequestHeader("Authorization") String token) {
        String userId = getUserIdFromToken(token.replace("Bearer ", ""));
        List<Task> archivedTasks = taskService.findArchivedTasksByUserId(userId);
        return ResponseEntity.ok(archivedTasks);
    }

    @GetMapping("/GetTaskReport")
    public ResponseEntity<Map<String, Integer>> getTaskReport(@RequestHeader("Authorization") String token) {
        String userId = getUserIdFromToken(token.replace("Bearer ", ""));
        List<Task> tasks = taskService.findByUserId(userId);

        int inProgress = (int) tasks.stream().filter(task -> !task.isDone() && !task.isArchived()).count();
        int overdue = (int) tasks.stream().filter(task -> !task.isDone() && task.getDueDate().isBefore(LocalDate.now()) && !task.isArchived()).count();
        int completedOnTime = (int) tasks.stream().filter(task -> task.isDone() && (task.getCompletionDate() == null || !task.getCompletionDate().isAfter(task.getDueDate()))).count();
        int allTasks = tasks.size();

        Map<String, Integer> reportData = new HashMap<>();
        reportData.put("inProgress", inProgress);
        reportData.put("overdue", overdue);
        reportData.put("completedOnTime", completedOnTime);
        reportData.put("allTasks", allTasks);

        return ResponseEntity.ok(reportData);
    }

    private String getUserIdFromToken(String token) {
        return Jwts.parser()
                .setSigningKey("secret")
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @PostMapping("/AddTask")
    public ResponseEntity<Map<String, Object>> addTask(
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
        task.setArchived(false);
        taskService.AddTask(task);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Task added successfully!");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/EditTask/{taskId}")
    public ResponseEntity<Map<String, Object>> editTask(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable String taskId,
            @RequestBody Task updatedTask) {

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

        Task existingTask = taskService.findById(taskId);
        if (existingTask == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Task not found"));
        }

        if (!existingTask.getUserId().equals(userId)) {
            return ResponseEntity.status(403).body(Map.of("error", "Unauthorized to edit this task"));
        }

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCategory(updatedTask.getCategory());
        existingTask.setPriority(updatedTask.getPriority());
        existingTask.setDueDate(updatedTask.getDueDate());

        taskService.updateTask(existingTask);

        return ResponseEntity.ok(Map.of("message", "Task updated successfully!"));
    }

    @PostMapping("/MarkTaskAsDone/{taskId}")
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

    @PostMapping("/ArchiveTask/{taskId}")
    public ResponseEntity<Map<String, Object>> archiveTask(
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
            return ResponseEntity.status(403).body(Map.of("error", "Unauthorized to archive this task"));
        }

        task.setArchived(true);
        taskService.updateTask(task);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Task archived successfully!");
        return ResponseEntity.ok(response);
    }

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
