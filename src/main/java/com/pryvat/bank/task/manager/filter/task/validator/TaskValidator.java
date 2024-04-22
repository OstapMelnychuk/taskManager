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

@Component
@RequiredArgsConstructor
public class TaskValidator {
    private final ApplicationContext applicationContext;

    public void validate(Task task) {
        validate(task, new ArrayList<>());
    }

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
