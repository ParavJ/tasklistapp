package com.example.tasklistapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// --- Lombok Annotations ---
// @Data is a convenient shortcut that bundles @Getter, @Setter, 
// @ToString, @EqualsAndHashCode and @RequiredArgsConstructor.
@Data
// Generates a constructor with no arguments, which is required by JPA.
@NoArgsConstructor
// Generates a constructor with all arguments.
@AllArgsConstructor

// --- JPA Annotations ---
// Marks this class as a JPA entity (i.e., a manageable, persistent object).
@Entity
// Specifies the name of the database table this entity maps to.
@Table(name = "tasks")
public class Task {

    // @Id marks this field as the primary key.
    @Id
    // @GeneratedValue specifies how the primary key is generated.
    // GenerationType.IDENTITY is suitable for PostgreSQL's BIGSERIAL type.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column is optional for simple fields, but here we use it to enforce
    // that the title cannot be null at the database level.
    @Column(nullable = false)
    private String title;

    // This field can be null, so no @Column annotation is strictly needed.
    private String description;

    // We can also set default values at the object level.
    private boolean completed = false;

    // --- NEW RELATIONSHIP MAPPING ---

    // Defines a many-to-one relationship between Task and User.
    // FetchType.LAZY is a performance optimization. It means the user data
    // will only be loaded from the database when it's explicitly accessed.
    @ManyToOne(fetch = FetchType.LAZY)
    
    // Specifies the foreign key column in the "tasks" table.
    // Every task must be linked to a user, so it cannot be null.
    @JoinColumn(name = "user_id", nullable = false)
    
    // This annotation prevents the user object from being serialized into JSON
    // when we return a Task object, which avoids an infinite loop issue.
    @JsonIgnore
    private User user;
}