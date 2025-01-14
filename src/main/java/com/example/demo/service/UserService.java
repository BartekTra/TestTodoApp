package com.example.demo.service;

import com.example.demo.dao.User;

/**
 * UserService defines operations for managing User entities.
 */
public interface UserService {

    /**
     * Registers a new user in the system.
     *
     * @param user the user to register.
     * @return the registered User object.
     */
    User registerUser(User user);

    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user.
     * @return the User object if found, otherwise null.
     */
    User findUserByEmail(String email);
}

