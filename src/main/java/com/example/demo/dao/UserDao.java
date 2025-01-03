package com.example.demo.dao;

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
    private final static List<User> APPLICATION_USERS = Arrays.asList(
            new User(
                    "admin",
                    "Password",
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))
            ),

            new User(
                    "user",
                    "Password",
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
            )
    );

    public User findUserByEmail(String email){
        return APPLICATION_USERS
                .stream()
                .filter(u -> u.getUsername().equals(email))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("No user was found"));
    }
}
