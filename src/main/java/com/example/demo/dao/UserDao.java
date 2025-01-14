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

/**
 * UserDao provides methods for retrieving user details from the database.
 */
@Repository
public class UserDao {

    private final UserService userService;

    /**
     * Constructor for UserDao.
     *
     * @param userService the UserService to use for user operations.
     */
    @Autowired
    public UserDao(UserService userService) {
        this.userService = userService;
    }

    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user.
     * @return the User object if found, otherwise null.
     */
    public User findUserByEmail(String email) {
        return userService.findUserByEmail(email);
    }
}
