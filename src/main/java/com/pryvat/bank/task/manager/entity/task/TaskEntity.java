package com.pryvat.bank.task.manager.entity.task;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Length(min = 3, max = 64, message = "Name length must be between 3 and 64 letters")
    private String name;
    @Enumerated(EnumType.STRING)
    @NotNull
    private TaskStatus status;
    @NotBlank
    @Length(min = 8, max = 256, message = "Description length must be between 8 and 256 letters")
    private String description;
}
