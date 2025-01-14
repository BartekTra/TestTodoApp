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

/**
 * Represents a user account stored in the "Accounts" MongoDB collection.
 */
@Getter
@Setter
@Document(collection = "Accounts")
public class User implements UserDetails {

    /**
     * Unique identifier for the user.
     */
    @Id
    private String id;

    /**
     * Username of the user.
     */
    private String username;

    /**
     * Password for the user account.
     */
    private String password;

    /**
     * Indicates if the user account is enabled.
     */
    private boolean enabled;

    /**
     * Indicates if the account is non-expired.
     */
    private boolean accountNonExpired;

    /**
     * Indicates if the account is non-locked.
     */
    private boolean accountNonLocked;

    /**
     * Indicates if the credentials are non-expired.
     */
    private boolean credentialsNonExpired;

    /**
     * List of roles assigned to the user.
     */
    private List<String> roles;

    /**
     * Set of authorities granted to the user.
     */
    private Set<GrantedAuthority> authorities;

    /**
     * Default constructor initializing default values.
     */
    public User() {
        this.authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        this.enabled = true;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
    }

    /**
     * Constructor to create a User object with username and password.
     *
     * @param username the username.
     * @param password the password.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        this.enabled = true;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
    }

    /**
     * Constructor to create a User object with username, password, and authorities.
     *
     * @param username    the username.
     * @param password    the password.
     * @param authorities the authorities.
     */
    public User(String username, String password, Set<GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = true;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
    }

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

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", roles=" + roles +
                ", authorities=" + authorities +
                '}';
    }
}