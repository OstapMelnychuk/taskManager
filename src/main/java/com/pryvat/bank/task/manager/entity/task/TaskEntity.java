package com.pryvat.bank.task.manager.entity.task;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Task entity. Represent some abstract task
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "task")
public class TaskEntity {
    /**
     * Identifier of the task
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Name of the task. Should not contain any special character except _
     * Should have length from 3 to 64 characters
     */
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Task name must not include any special character except _")
    @Length(min = 3, max = 64, message = "Name length must be between 3 and 64 letters")
    private String name;
    /**
     * Status of the task
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private TaskStatus status;
    /**
     * Description of the task. Should have length from 8 to 256 characters
     */
    @NotBlank
    @Length(min = 8, max = 256, message = "Description length must be between 8 and 256 letters")
    private String description;
}
