package com.example.demo.controllers;

import com.example.demo.Collections.Task;
import com.example.demo.Repository.TaskRepository;
import com.example.demo.service.TaskService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
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


        // Call the service to add the task
        taskService.AddTask(task);
        System.out.println(task);
        // Return a success response
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Task added successfully!");
        return ResponseEntity.ok(response);
    }

}
