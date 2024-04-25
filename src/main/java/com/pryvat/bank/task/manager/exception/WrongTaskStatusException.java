package com.pryvat.bank.task.manager.exception;

import com.pryvat.bank.task.manager.entity.task.TaskStatus;

import java.util.Arrays;
import java.util.stream.Collectors;

public class WrongTaskStatusException extends RuntimeException {
    public WrongTaskStatusException(String wrongStatus) {
        super("Wrong status %s. Correct statuses: %s".formatted(wrongStatus,
                        Arrays.stream(TaskStatus.values())
                                .map(TaskStatus::getValue)
                                .collect(Collectors.joining(" "))));
    }
}
