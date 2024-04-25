package com.pryvat.bank.task.manager.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Object id, String className) {
        super("%s with id %s has not been found".formatted(className, id));
    }
}
