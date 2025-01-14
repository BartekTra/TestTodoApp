package com.example.demo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * AuthenticationRequest represents the request payload for user authentication.
 */
@Document
@Data
@Getter
@Setter
@NoArgsConstructor
public class AuthenticationRequest {

    /** The email address of the user. */
    private String email;

    /** The password of the user. */
    private String password;
}

