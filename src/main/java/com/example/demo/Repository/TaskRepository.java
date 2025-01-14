package com.example.demo.Repository;

import com.example.demo.Collections.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * TaskRepository provides database operations for the Task entity.
 */
public interface TaskRepository extends MongoRepository<Task, String> {

    /**
     * Finds tasks associated with a specific user ID.
     *
     * @param userId the ID of the user.
     * @return a list of tasks belonging to the specified user.
     */
    List<Task> findByUserId(String userId);
}

