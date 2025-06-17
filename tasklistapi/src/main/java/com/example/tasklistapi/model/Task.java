package com.example.tasklistapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
}