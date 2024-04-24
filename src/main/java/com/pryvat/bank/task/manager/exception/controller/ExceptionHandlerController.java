package com.pryvat.bank.task.manager.exception.controller;

import com.pryvat.bank.task.manager.exception.EntityNotFoundException;
import com.pryvat.bank.task.manager.exception.TaskValidationException;
import com.pryvat.bank.task.manager.exception.WrongTaskStatusException;
import com.pryvat.bank.task.manager.router.DataSourceRouter;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
@Log4j2
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> handleValidationException(ConstraintViolationException ex) {
        log.error(ex);
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(constraintViolation -> {
            errors.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public Map<String, String> handleNotFoundException(RuntimeException ex) {
        log.error(ex);
        return prepareErrorMessage(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TaskValidationException.class)
    public Map<String, String> handleTaskValidationException(RuntimeException ex) {
        log.error(ex);
        return prepareErrorMessage(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WrongTaskStatusException.class)
    public Map<String, String> handleWrongTaskStatusException(RuntimeException ex) {
        log.error(ex);
        return prepareErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(value = {DataAccessResourceFailureException.class, InvalidDataAccessResourceUsageException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> changeDBToPostgres(RuntimeException e) {
        if (Objects.isNull(DataSourceRouter.getDataSourceKey()) || DataSourceRouter.getDataSourceKey().equals("h2")) {
            log.error(e);
            log.info("Switching database to postgres");
            DataSourceRouter.setDataSourceKey("postgresql");
            System.out.println(DataSourceRouter.getDataSourceKey());
        }
        return prepareErrorMessage("Changing database source. Please repeat request");
    }

    private Map<String, String> prepareErrorMessage(String errorMessage) {
        HashMap<String, String> errorMessages = new HashMap<>();
        errorMessages.put("message", errorMessage);
        return errorMessages;
    }
}
