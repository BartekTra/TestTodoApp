package com.example.demo.service;

import com.example.demo.Collections.ActiveToken;
import com.example.demo.Repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * TokenServiceImpl provides the implementation for TokenService.
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    /**
     * Saves a token for a specific user.
     *
     * @param user  the username of the user.
     * @param token the token to save.
     */
    @Override
    public void saveToken(String user, String token) {
        tokenRepository.save(new ActiveToken(token, user));
    }

    /**
     * Finds an active token by the associated user's username.
     *
     * @param user the username of the user.
     * @return the ActiveToken object if found, otherwise null.
     */
    @Override
    public ActiveToken findTokenByUser(String user) {
        return tokenRepository.findActiveTokenByUser(user).orElse(null);
    }

    /**
     * Deletes tokens associated with a specific user.
     *
     * @param user the username of the user.
     */
    public void deleteTokenByUser(String user) {
        tokenRepository.DeleteTokenByUser(user);
    }
}

