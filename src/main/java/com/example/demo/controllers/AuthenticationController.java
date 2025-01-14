package com.example.demo.controllers;

import com.example.demo.config.JwtUtils;
import com.example.demo.dao.UserDao;
import com.example.demo.dto.AuthenticationRequest;
import com.example.demo.service.TokenServiceImpl;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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
    private TokenServiceImpl tokenService;
    @Autowired
    private UserService userService;

    /**
     * Authenticates a user based on email and password, and generates a JWT token.
     *
     * @param request the {@link AuthenticationRequest} containing email and password.
     * @return a {@link ResponseEntity} containing the generated token or an error message.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, Object>> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            final UserDetails user = userDao.findUserByEmail(request.getEmail());
            if (user == null) {
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }

            // Delete old token if exists
            if (tokenService.findTokenByUser(user.getUsername()) != null) {
                tokenService.deleteTokenByUser(user.getUsername());
            }

            // Generate new JWT token
            String token = jwtUtils.generateToken(user);
            tokenService.saveToken(user.getUsername(), token);

            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }
    }

    /**
     * Registers a new user.
     *
     * @param user the {@link User} object containing user details.
     * @return a {@link ResponseEntity} containing the saved user or an error message.
     */
    @PostMapping("/Register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        try {
            User savedUser = userService.registerUser(user);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("user", savedUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Registration failed");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Logs out a user by invalidating their JWT token.
     *
     * @param token the Bearer token from the Authorization header.
     * @return a {@link ResponseEntity} containing a success message or an error message.
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String token) {
        try {
            String username = jwtUtils.extractUsername(token.replace("Bearer ", ""));
            if (tokenService.findTokenByUser(username) != null) {
                tokenService.deleteTokenByUser(username);
                return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
            }
            return ResponseEntity.status(400).body(Map.of("error", "Token not found or already logged out"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", "Invalid token"));
        }
    }
}