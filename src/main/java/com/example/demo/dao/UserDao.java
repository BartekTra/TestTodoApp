package com.example.demo.dao;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.example.demo.dao.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
public class UserDao {
    private final UserService userService;

    @Autowired
    public UserDao(UserService userService) {
        this.userService = userService;
    }

    public User findUserByEmail(String email) {
        return userService.findUserByEmail(email);
    }
}
