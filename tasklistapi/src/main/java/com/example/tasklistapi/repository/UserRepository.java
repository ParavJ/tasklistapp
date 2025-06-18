package com.example.tasklistapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tasklistapi.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query to find a user by their username.
    // Spring Data JPA automatically implements this method based on its name.
    // It will generate the SQL: "SELECT * FROM users WHERE username = ?"
    Optional<User> findByUsername(String username);

}