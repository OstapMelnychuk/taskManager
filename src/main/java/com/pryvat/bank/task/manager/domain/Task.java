package com.pryvat.bank.task.manager.domain;

import com.pryvat.bank.task.manager.entity.task.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain model for the task
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private Long id;
    private String name;
    private TaskStatus status;
    private String description;
}
