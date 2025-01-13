package com.example.demo.service;

import com.example.demo.Collections.ActiveToken;
import com.example.demo.dao.User;

public interface TokenService {
    void saveToken(String user, String token);
    ActiveToken findTokenByUser(String user);
}
