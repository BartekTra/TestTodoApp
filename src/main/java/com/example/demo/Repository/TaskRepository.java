package com.example.demo.Repository;

import com.example.demo.Collections.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByUserId(String userId);

    List<Task> findByUserIdAndIsDone(String userId, boolean isDone); // For archived tasks
}
