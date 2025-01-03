package com.example.demo.controllers;

import com.example.demo.Repository.AccountsRepository;
import com.example.demo.config.JwtUtils;
import com.example.demo.dao.UserDao;
import com.example.demo.dto.AuthenticationRequest;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dao.User;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;
    private final JwtUtils jwtUtils;
    private final Map<String, String> activeTokens = new HashMap<>(); // In-memory token store (or replace with database)

    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, Object>> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            // Authenticate user credentials
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // Retrieve user details
            final UserDetails user = userDao.findUserByEmail(request.getEmail());
            if (user == null) {
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }

            // Check if user is already logged in
            if (activeTokens.containsKey(user.getUsername())) {
                return ResponseEntity.status(400).body(Map.of("error", "User already logged in"));
            }

            // Generate JWT token
            String token = jwtUtils.generateToken(user);

            // Store the token in the active tokens map
            activeTokens.put(user.getUsername(), token);

            // Return success response with the token
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String token) {
        try {
            // Extract username from token
            String username = jwtUtils.extractUsername(token.replace("Bearer ", ""));

            // Remove the token from active tokens
            if (activeTokens.containsKey(username)) {
                activeTokens.remove(username);
                return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
            }

            return ResponseEntity.status(400).body(Map.of("error", "Token not found or already logged out"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", "Invalid token"));
        }
    }

}