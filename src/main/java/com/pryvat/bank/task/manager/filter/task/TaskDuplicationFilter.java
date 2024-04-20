package com.pryvat.bank.task.manager.filter.task;

import com.pryvat.bank.task.manager.domain.Task;
import com.pryvat.bank.task.manager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@TaskFiltration
public class TaskDuplicationFilter implements TaskFilter {
    private final TaskRepository taskRepository;
    private static final String ERROR_MESSAGE = "There is already created task with name: %s and status %s";

    @Override
    public TaskValidationResult validate(Task task) {
        return new TaskValidationResult(!taskRepository.existsByNameAndStatus(task.getName(), task.getStatus()),
                ERROR_MESSAGE.formatted(task.getName(), task.getStatus()));
    }
}
