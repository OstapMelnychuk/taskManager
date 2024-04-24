package com.pryvat.bank.task.manager.filter.task;

import com.pryvat.bank.task.manager.domain.Task;

/**
 * Basic filter for task validation
 */
public interface TaskFilter {
    /**
     * Method that validates the task
     * @param task task to validate
     * @return {@link TaskValidationResult} that contains validation result and error message
     */
    TaskValidationResult validate(Task task);
}
