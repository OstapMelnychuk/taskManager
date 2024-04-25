package com.pryvat.bank.task.manager.filter.task.impl;

import com.pryvat.bank.task.manager.domain.Task;
import com.pryvat.bank.task.manager.filter.task.TaskFilter;
import com.pryvat.bank.task.manager.filter.task.TaskFiltration;
import com.pryvat.bank.task.manager.filter.task.TaskValidationResult;
import com.pryvat.bank.task.manager.repository.h2.task.H2TaskRepository;
import com.pryvat.bank.task.manager.repository.postgres.task.PostgresSqlTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Component;

/**
 * TaskCount filter that checks max allowed quantity for tasks in the database
 */
@Component
@TaskFiltration
@RequiredArgsConstructor
@Log4j2
public class TaskCountFilter implements TaskFilter {
    private final H2TaskRepository h2TaskRepository;
    private final PostgresSqlTaskRepository postgresSqlTaskRepository;
    /**
     * Maximum quantity of task allowed to be created. Can be set in application.properties
     */
    @Value("${task.max-row-quantity}")
    private int rowCountLimit;
    private static final String ERROR_MESSAGE = "Reached task creation limit. Delete some tasks to create new ones";

    /**
     * Method that validates max task quantity in a database
     * @param task task to validate
     * @return {@link TaskValidationResult} that contains validation result and error message
     */
    @Override
    public TaskValidationResult validate(Task task) {
        log.info("Validating task row count");
        try {
            return new TaskValidationResult(h2TaskRepository.count() < rowCountLimit, ERROR_MESSAGE);
        } catch (DataAccessResourceFailureException | InvalidDataAccessResourceUsageException e) {
            log.error("Error validating task count in H2 database", e);
        }
        return new TaskValidationResult(postgresSqlTaskRepository.count() < rowCountLimit, ERROR_MESSAGE);
    }
}
