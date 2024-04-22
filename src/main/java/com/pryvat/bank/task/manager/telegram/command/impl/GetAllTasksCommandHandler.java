package com.pryvat.bank.task.manager.telegram.command.impl;

import com.pryvat.bank.task.manager.entity.task.TaskEntity;
import com.pryvat.bank.task.manager.repository.task.TaskRepository;
import com.pryvat.bank.task.manager.telegram.command.CommandHandler;
import com.pryvat.bank.task.manager.telegram.constants.Commands;
import com.pryvat.bank.task.manager.telegram.constants.StandartMessages;
import com.pryvat.bank.task.manager.telegram.dto.TelegramTaskDTO;
import com.pryvat.bank.task.manager.telegram.model.UserRequest;
import com.pryvat.bank.task.manager.telegram.reply.ReplyKeyboardMarkupProvider;
import com.pryvat.bank.task.manager.telegram.service.TelegramSendingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllTasksCommandHandler implements CommandHandler {
    private final TaskRepository taskRepository;
    private final TelegramSendingService telegramSendingService;
    private final ModelMapper modelMapper;

    @Override
    public void handleCommand(UserRequest userRequest) {
        List<TaskEntity> taskEntities = taskRepository.findAll();
        if (taskEntities.isEmpty()) {
            telegramSendingService.sendMessage(userRequest.getId(), StandartMessages.NO_TASKS_CREATED_MESSAGE);
            return;
        }
        taskEntities.stream()
                .map(taskEntity -> modelMapper.map(taskEntity, TelegramTaskDTO.class))
                .forEach(taskDTO -> telegramSendingService.sendMessage(userRequest.getId(), taskMessage(taskDTO)));
    }

    private String taskMessage(TelegramTaskDTO task) {
        return "Task:\n" + StandartMessages.TASK_MESSAGE.formatted(task.getId(), task.getName(), task.getStatus(), task.getDescription());
    }

    @Override
    public String getCommandName() {
        return Commands.GET_ALL_TASKS;
    }
}
