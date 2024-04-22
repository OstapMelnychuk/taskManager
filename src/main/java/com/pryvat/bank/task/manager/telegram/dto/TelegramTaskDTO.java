package com.pryvat.bank.task.manager.telegram.dto;

import com.pryvat.bank.task.manager.entity.task.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelegramTaskDTO {
    private Long id;
    private String name;
    private TaskStatus status;
    private String description;
}
