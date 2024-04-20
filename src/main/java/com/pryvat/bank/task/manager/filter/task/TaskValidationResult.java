package com.pryvat.bank.task.manager.filter.task;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskValidationResult {
    private boolean isValid;
    private String errorMessage;
}
