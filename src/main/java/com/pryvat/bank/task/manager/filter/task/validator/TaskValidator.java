package com.pryvat.bank.task.manager.filter.task.validator;

import com.pryvat.bank.task.manager.domain.Task;
import com.pryvat.bank.task.manager.exception.TaskValidationException;
import com.pryvat.bank.task.manager.filter.task.TaskFilter;
import com.pryvat.bank.task.manager.filter.task.TaskFiltration;
import com.pryvat.bank.task.manager.filter.task.TaskValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Component that validates business logic.
 * Uses {@link TaskFilter} that provide implementation of those rules
 */
@Component
@RequiredArgsConstructor
public class TaskValidator {
    private final ApplicationContext applicationContext;

    /**
     * Method that collects all the filters annotated with {@link TaskFiltration} annotation
     * and validates the provided task
     * @param task to validate
     * @throws TaskValidationException if any of the validation fails
     */
    public void validate(Task task) {
        validate(task, new ArrayList<>());
    }

    /**
     * Method that collects all the filters annotated with {@link TaskFiltration} annotation
     * and validates the provided task. Can skip certain filters
     * @param task to validate
     * @param excludedFilters filters that will not be used during validation
     */
    public void validate(Task task, List<Class> excludedFilters) {
        Map<String, Object> filters = applicationContext.getBeansWithAnnotation(TaskFiltration.class);
        for (Object filter : filters.values()) {
            if (filter instanceof TaskFilter && !excludedFilters.contains(filter.getClass())) {
                TaskValidationResult taskValidationResult = ((TaskFilter) filter).validate(task);
                if (!taskValidationResult.isValid()) {
                    throw new TaskValidationException(taskValidationResult.getErrorMessage());
                }
            }
        }
    }
}
