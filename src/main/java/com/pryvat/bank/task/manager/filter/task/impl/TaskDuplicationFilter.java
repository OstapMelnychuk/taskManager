package com.pryvat.bank.task.manager.filter.task.impl;

import com.pryvat.bank.task.manager.domain.Task;
import com.pryvat.bank.task.manager.filter.task.TaskFilter;
import com.pryvat.bank.task.manager.filter.task.TaskFiltration;
import com.pryvat.bank.task.manager.filter.task.TaskValidationResult;
import com.pryvat.bank.task.manager.repository.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * TaskDuplication filter that validates if there is a task in a database with the same name and status
 */
@Component
@RequiredArgsConstructor
@TaskFiltration
public class TaskDuplicationFilter implements TaskFilter {
    private final TaskRepository taskRepository;
    private static final String ERROR_MESSAGE = "There is already created task with name: %s and status %s";

    /**
     * Method that validates if there is a task in a database with the same name and status
     * @param task task to validate
     * @return {@link TaskValidationResult} that contains validation result and error message
     */
    @Override
    public TaskValidationResult validate(Task task) {
        return new TaskValidationResult(!taskRepository.existsByNameAndStatus(task.getName(), task.getStatus()),
                ERROR_MESSAGE.formatted(task.getName(), task.getStatus()));
    }
}
