package com.example.demo.Repository;

import com.example.demo.Collections.ActiveToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<ActiveToken, String> {
    Optional<ActiveToken> findActiveTokenByUser(String user);
    @Query(value = "{ 'user': ?0 }", delete = true)
    void DeleteTokenByUser(String user);
}
