package com.example.demo.service;

import com.example.demo.Repository.AccountsRepository;
import com.example.demo.dao.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserServiceImpl provides the implementation for UserService.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AccountsRepository accountsRepository;

    /**
     * Registers a new user in the system.
     * Performs validation before saving the user.
     *
     * @param user the user to register.
     * @return the registered User object.
     */
    @Override
    public User registerUser(User user) {
        return accountsRepository.save(user);
    }

    /**
     * Finds a user by their email address.
     * This implementation uses the username field to store the email address.
     *
     * @param email the email address of the user.
     * @return the User object if found, otherwise null.
     */
    @Override
    public User findUserByEmail(String email) {
        return accountsRepository.findByUsername(email).orElse(null);
    }
}

