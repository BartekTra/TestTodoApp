package com.example.demo.service;

import com.example.demo.dao.User;

public interface UserService {
    User registerUser(User user);
    User findUserByEmail(String email);
}
