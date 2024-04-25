package com.pryvat.bank.task.manager.telegram.service;

import com.pryvat.bank.task.manager.domain.Task;
import com.pryvat.bank.task.manager.entity.telegram.TelegramUser;
import com.pryvat.bank.task.manager.repository.h2.telegram.H2TelegramUserRepository;
import com.pryvat.bank.task.manager.repository.postgres.telegram.PostgresTelegramUserRepository;
import com.pryvat.bank.task.manager.telegram.constants.StandartMessages;
import com.pryvat.bank.task.manager.telegram.dto.TelegramTaskDTO;
import com.pryvat.bank.task.manager.telegram.reply.ReplyKeyboardMarkupProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TelegramTaskUpdateService that sends all the task updates to the user
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class TelegramTaskUpdateService {
    private final TelegramSendingService telegramSendingService;
    private final ReplyKeyboardMarkupProvider replyKeyboardMarkupProvider;
    private final H2TelegramUserRepository h2TelegramUserRepository;
    private final PostgresTelegramUserRepository postgresTelegramUserRepository;
    private final ModelMapper modelMapper;

    /**
     * Method that sends the notification that specific task was created
     * @param task that have been created
     */
    public void sendTaskCreationMessage(Task task) {
        log.info("Sending created task notification to telegram users");
        sendUpdateMessageToAllUsers(taskCreatedMessage(modelMapper.map(task, TelegramTaskDTO.class)));
    }

    /**
     * Method that sends the notification that specific task was updated
     * @param primaryTask task before updating
     * @param updatedTask task after updating
     */
    public void sendTaskUpdatedMessage(Task primaryTask, Task updatedTask) {
        log.info("Sending updated task notification to telegram users");
        sendUpdateMessageToAllUsers(taskUpdatedMessage(modelMapper.map(primaryTask, TelegramTaskDTO.class),
                modelMapper.map(updatedTask, TelegramTaskDTO.class)));
    }

    private void sendUpdateMessageToAllUsers(String message) {
        List<TelegramUser> telegramUsers;
        try {
            telegramUsers = h2TelegramUserRepository.findAll();
        } catch (Exception e) {
            log.error("Failed to retrieve users from H2 database", e);
            telegramUsers = postgresTelegramUserRepository.findAll();
        }
        telegramUsers.forEach(telegramUser -> telegramSendingService.sendMessage(telegramUser.getChatId(),
                message,
                replyKeyboardMarkupProvider.buildMainKeyboard(telegramUser.getChatId())));
    }

    private String taskUpdatedMessage(TelegramTaskDTO primaryTask, TelegramTaskDTO updatedTask) {
        return """
                Task updated from -> to
                id: %d,
                name: %s -> %s,
                status: %s -> %s,
                description: %s -> %s
                """.formatted(updatedTask.getId(),
                primaryTask.getName(), updatedTask.getName(),
                primaryTask.getStatus(), updatedTask.getStatus(),
                primaryTask.getDescription(), updatedTask.getDescription());
    }

    private String taskCreatedMessage(TelegramTaskDTO task) {
        return "Task created:\n"
                + StandartMessages.TASK_MESSAGE.formatted(task.getId(), task.getName(), task.getStatus(), task.getDescription());
    }
}
