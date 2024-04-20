package com.pryvat.bank.task.manager.domain;

import com.pryvat.bank.task.manager.entity.task.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private Long id;
    @NotBlank
    @Size(min = 3, max = 64, message = "Name length must be between 3 and 64 letters")
    private String name;
    @Size(min = 4, max = 64, message = "Status length must be between 4 and 64 letters")
    @NotBlank
    private TaskStatus status;
    @Size(min = 8, max = 256, message = "Description length must be between 8 and 256 letters")
    @NotBlank
    private String description;
}
