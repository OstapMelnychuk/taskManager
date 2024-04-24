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
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Handler that sends all created tasks to a specific chat
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class GetAllTasksCommandHandler implements CommandHandler {
    private final TaskRepository taskRepository;
    private final TelegramSendingService telegramSendingService;
    private final ModelMapper modelMapper;

    /**
     * Method that handles Get all tasks command
     * Sends all tasks created to a user
     * @param userRequest that contains update data
     */
    @Override
    public void handleCommand(UserRequest userRequest) {
        log.info("Handling %s command".formatted(getCommandName()));
        List<TaskEntity> taskEntities = taskRepository.findAll();
        if (taskEntities.isEmpty()) {
            telegramSendingService.sendMessage(userRequest.getId(), StandartMessages.NO_TASKS_CREATED_MESSAGE);
            return;
        }
        taskEntities.stream()
                .map(taskEntity -> modelMapper.map(taskEntity, TelegramTaskDTO.class))
                .forEach(taskDTO -> telegramSendingService.sendMessage(userRequest.getId(), taskMessage(taskDTO)));
        log.info("Successfully sent created task to users");
    }

    private String taskMessage(TelegramTaskDTO task) {
        return "Task:\n" + StandartMessages.TASK_MESSAGE.formatted(task.getId(), task.getName(), task.getStatus(), task.getDescription());
    }

    @Override
    public String getCommandName() {
        return Commands.GET_ALL_TASKS;
    }
}
