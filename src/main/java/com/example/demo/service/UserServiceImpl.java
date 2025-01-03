package com.example.demo.service;

import com.example.demo.Repository.AccountsRepository;
import com.example.demo.dao.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AccountsRepository accountsRepository;

    @Override
    public User registerUser(User user) {
        // Perform validation and save the user
        return accountsRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        // Add custom logic to find user by email if needed
        return accountsRepository.findByUsername(email).orElse(null);
    }
}
