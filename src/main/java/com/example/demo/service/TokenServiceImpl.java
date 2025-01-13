package com.example.demo.service;

import com.example.demo.Collections.ActiveToken;
import com.example.demo.Repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void saveToken(String user, String token) {
        // Perform validation and save the user
        tokenRepository.save(new ActiveToken(token, user));
    }

    @Override
    public ActiveToken findTokenByUser(String user) {
        return tokenRepository.findActiveTokenByUser(user).orElse(null);
    }

    public void deleteTokenByUser(String user) {
        tokenRepository.DeleteTokenByUser(user);
    }
}
