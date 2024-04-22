package com.pryvat.bank.task.manager.entity.task;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.pryvat.bank.task.manager.model.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
public enum TaskStatus {
    CREATED("CREATED"),
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE");

    @Length(min = 4, max = 64, message = "Status length must be between 4 and 64 letters")
    @NotBlank
    private String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static Status fromValue(String value) {
        for (Status b : Status.values()) {
            if (b.getValue().equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}