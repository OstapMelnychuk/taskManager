package com.pryvat.bank.task.manager.filter.task.impl;

import com.pryvat.bank.task.manager.domain.Task;
import com.pryvat.bank.task.manager.filter.task.TaskFilter;
import com.pryvat.bank.task.manager.filter.task.TaskFiltration;
import com.pryvat.bank.task.manager.filter.task.TaskValidationResult;
import com.pryvat.bank.task.manager.repository.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@TaskFiltration
@RequiredArgsConstructor
public class TaskCountFilter implements TaskFilter {
    private final TaskRepository taskRepository;
    private static final int ROW_COUNT_LIMIT = 3;
    private static final String ERROR_MESSAGE = "Reached task creation limit. Delete some tasks to create new ones";

    @Override
    public TaskValidationResult validate(Task task) {
        return new TaskValidationResult(taskRepository.count() < ROW_COUNT_LIMIT, ERROR_MESSAGE);
    }
}
