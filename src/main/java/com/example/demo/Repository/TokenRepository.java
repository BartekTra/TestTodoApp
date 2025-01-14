package com.example.demo.Repository;

import com.example.demo.Collections.ActiveToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

/**
 * TokenRepository provides database operations for the ActiveToken entity.
 */
@Repository
public interface TokenRepository extends MongoRepository<ActiveToken, String> {

    /**
     * Finds an active token by the associated user's username.
     *
     * @param user the username of the user.
     * @return an Optional containing the ActiveToken object if found, otherwise empty.
     */
    Optional<ActiveToken> findActiveTokenByUser(String user);

    /**
     * Deletes tokens associated with a specific user.
     *
     * @param user the username of the user.
     */
    @Query(value = "{ 'user': ?0 }", delete = true)
    void DeleteTokenByUser(String user);
}

