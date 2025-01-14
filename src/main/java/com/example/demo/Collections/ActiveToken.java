package com.example.demo.Collections;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents an active JWT token stored in the "ActiveTokens" MongoDB collection.
 */
@Getter
@Setter
@Document(collection = "ActiveTokens")
public class ActiveToken {

    /**
     * The token string.
     */
    @Id
    private String token;

    /**
     * The username associated with the token.
     */
    private String user;

    /**
     * Constructor to create an ActiveToken object.
     *
     * @param token the JWT token.
     * @param user  the associated username.
     */
    public ActiveToken(String token, String user) {
        this.token = token;
        this.user = user;
    }
}

