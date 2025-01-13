package com.example.demo.Collections;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "ActiveTokens")
public class ActiveToken {
    @Id
    private String token;
    private String user;

    public ActiveToken(String token, String user) {
        this.token = token;
        this.user = user;
    }
}
