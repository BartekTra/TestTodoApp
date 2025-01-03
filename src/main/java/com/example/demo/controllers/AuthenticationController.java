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
    @Autowired
    private UserService userService;

    @CrossOrigin(origins = "*")
    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, Object>> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            final UserDetails user = userDao.findUserByEmail(request.getEmail());
            if (user != null) {
                // Build JSON response with the token
                Map<String, Object> response = new HashMap<>();
                response.put("token", jwtUtils.generateToken(user));
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            // Authentication failed or other error
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "error");
            return ResponseEntity.status(400).body(errorResponse);
        }
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "error");
        return ResponseEntity.status(400).body(errorResponse);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/Register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        try {
            User savedUser = userService.registerUser(user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("user", savedUser);
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            e.printStackTrace(); // Log the exception details
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Registration failed");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

}