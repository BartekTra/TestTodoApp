package com.example.demo.dao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Document(collection = "Accounts") // Specify MongoDB collection name
public class User implements UserDetails {
    @Id
    private String id; // MongoDB's unique identifier
    private String username;
    private String password;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private List<String> roles; // Simplified role storage for MongoDB
    private Set<GrantedAuthority> authorities;

    // Constructors
    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        this.enabled = true; // Default value
        this.accountNonExpired = true; // Default value
        this.accountNonLocked = true; // Default value
        this.credentialsNonExpired = true; // Default value
    }

    public User(String username, String password, Set<GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = true; // Default value
        this.accountNonExpired = true; // Default value
        this.accountNonLocked = true; // Default value
        this.credentialsNonExpired = true; // Default value
    }

    // Methods from UserDetails interface
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
