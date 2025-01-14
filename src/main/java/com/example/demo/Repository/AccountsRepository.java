package com.example.demo.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.example.demo.dao.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * AccountsRepository provides database operations for the User entity.
 */
@Repository
public interface AccountsRepository extends MongoRepository<User, String> {

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user.
     * @return an Optional containing the User object if found, otherwise empty.
     */
    Optional<User> findByUsername(String username);
}
