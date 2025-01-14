package com.example.demo.service;

import com.example.demo.Collections.ActiveToken;
import com.example.demo.dao.User;

/**
 * TokenService defines operations for managing ActiveToken entities.
 */
public interface TokenService {

    /**
     * Saves a token for a specific user.
     *
     * @param user  the username of the user.
     * @param token the token to save.
     */
    void saveToken(String user, String token);

    /**
     * Finds an active token by the associated user's username.
     *
     * @param user the username of the user.
     * @return the ActiveToken object if found, otherwise null.
     */
    ActiveToken findTokenByUser(String user);
}

