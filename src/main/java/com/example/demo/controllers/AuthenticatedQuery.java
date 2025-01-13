package com.example.demo.controllers;

import com.example.demo.Collections.Task;
import com.example.demo.Repository.TaskRepository;
import com.example.demo.service.TaskService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
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
        // Extract userId from the token
        String userId = getUserIdFromToken(token.replace("Bearer ", ""));

        // Get tasks for the user
        List<Task> tasks = taskService.findByUserId(userId);

        return ResponseEntity.ok(tasks);
    }

    private String getUserIdFromToken(String token) {
        // Token decoding logic (example shown earlier)
        return Jwts.parser()
                .setSigningKey("secret") // Replace with your secret key
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // Assuming the subject is the user ID
    }


    @PostMapping("/AddTask")
    public ResponseEntity<Map<String, Object>> AddTask(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody Task task) {

        // Check if the Authorization header is present and starts with "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Token is missing or invalid"));
        }

        // Extract the token (remove the "Bearer " prefix)
        String token = authorizationHeader.substring(7);

        // Decode the token to get the user ID
        String userId;
        try {
            userId = getUserIdFromToken(token); // Decode the token to get the user ID
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }


        // Set the user ID in the task or process as needed
        task.setUserId(userId); // Assuming your Task class has a userId field

        if (task.getPriority() == null) {
            task.setPriority(Task.Priority.NORMAL); // Domyślny priorytet
        }

        task.setIsDone(false);


        // Call the service to add the task
        taskService.AddTask(task);
        System.out.println(task);
        // Return a success response
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Task added successfully!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/MarkTaskAsDone/{taskId}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Map<String, Object>> markTaskAsDone(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable String taskId) {

        // Check if the Authorization header is present and starts with "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Token is missing or invalid"));
        }

        // Extract the token (remove the "Bearer " prefix)
        String token = authorizationHeader.substring(7);

        // Decode the token to get the user ID
        String userId;
        try {
            userId = getUserIdFromToken(token); // Decode the token to get the user ID
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }

        // Find the task by taskId
        Task task = taskService.findById(taskId);
        if (task == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Task not found"));
        }

        // Verify the task belongs to the user
        if (!task.getUserId().equals(userId)) {
            return ResponseEntity.status(403).body(Map.of("error", "Unauthorized to modify this task"));
        }

        // Update the task's isDone field and set completionDate
        task.setIsDone(true);
        task.setCompletionDate(java.time.LocalDate.now()); // Set the completion date to the current date

        // Save the updated task
        taskService.updateTask(task);

        // Return success response
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Task marked as done successfully!");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/GetTaskReport")
    public ResponseEntity<Map<String, Object>> getTaskReport(@RequestHeader("Authorization") String token) {
        // Wyciągnij userId z tokena
        String userId;
        try {
            userId = getUserIdFromToken(token.replace("Bearer ", ""));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }

        // Pobierz wszystkie zadania użytkownika
        List<Task> tasks = taskService.findByUserId(userId);

        // Przygotuj statystyki
        int inProgress = 0;
        int overdue = 0;
        int completedOnTime = 0;
        int allTasks = 0;
        for (Task task : tasks) {
            LocalDate dueDate = task.getDueDate(); // Zakładam, że dueDate jest typu LocalDate
            LocalDate completionDate = task.getCompletionDate(); // completionDate również typu LocalDate

            if (task.getIsDone()) {
                // Sprawdź, czy obie daty są nie-`null` przed użyciem `isAfter`
                if (dueDate != null && completionDate != null && dueDate.isAfter(completionDate)) {
                    completedOnTime++;
                }
            } else {
                // Zadania w trakcie realizacji lub przeterminowane
                if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
                    overdue++;
                } else {
                    inProgress++;
                }
            }
            allTasks++;
        }


        // Przygotuj odpowiedź
        Map<String, Object> report = new HashMap<>();
        report.put("inProgress", inProgress);
        report.put("overdue", overdue);
        report.put("completedOnTime", completedOnTime);
        report.put("allTasks", allTasks);

        return ResponseEntity.ok(report);
    }

    @DeleteMapping("/DeleteTask/{taskId}")
    public ResponseEntity<Map<String, Object>> deleteTask(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable String taskId) {

        // Sprawdź, czy nagłówek Authorization jest obecny i zaczyna się od "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Token is missing or invalid"));
        }

        // Wyodrębnij token (usuń prefiks "Bearer ")
        String token = authorizationHeader.substring(7);

        // Dekoduj token, aby uzyskać userId
        String userId;
        try {
            userId = getUserIdFromToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }

        // Znajdź zadanie po taskId
        Task task = taskService.findById(taskId);
        if (task == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Task not found"));
        }

        // Zweryfikuj, czy zadanie należy do użytkownika
        if (!task.getUserId().equals(userId)) {
            return ResponseEntity.status(403).body(Map.of("error", "Unauthorized to delete this task"));
        }

        // Usuń zadanie
        taskService.deleteTask(taskId);

        // Zwróć odpowiedź sukcesu
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Task deleted successfully!");
        return ResponseEntity.ok(response);
    }






}
