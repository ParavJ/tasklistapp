package com.example.tasklistapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tasklistapi.model.Task;

// @Repository marks this interface as a Spring Data repository.
// It's technically optional for JpaRepository but is good practice.
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
}