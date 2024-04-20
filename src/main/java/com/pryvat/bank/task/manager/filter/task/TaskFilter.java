package com.pryvat.bank.task.manager.filter.task;

import com.pryvat.bank.task.manager.domain.Task;

public interface TaskFilter {
    TaskValidationResult validate(Task task);
}
