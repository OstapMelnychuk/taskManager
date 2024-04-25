package com.pryvat.bank.task.manager.telegram.command.impl;

import com.pryvat.bank.task.manager.entity.telegram.TelegramUser;
import com.pryvat.bank.task.manager.repository.h2.telegram.H2TelegramUserRepository;
import com.pryvat.bank.task.manager.repository.postgres.telegram.PostgresTelegramUserRepository;
import com.pryvat.bank.task.manager.telegram.command.CommandHandler;
import com.pryvat.bank.task.manager.telegram.constants.Commands;
import com.pryvat.bank.task.manager.telegram.constants.StandartMessages;
import com.pryvat.bank.task.manager.telegram.model.UserRequest;
import com.pryvat.bank.task.manager.telegram.reply.ReplyKeyboardMarkupProvider;
import com.pryvat.bank.task.manager.telegram.service.TelegramSendingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Component;

/**
 * SubscribeCommandHandler that handles Subscribe command
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class SubscribeCommandHandler implements CommandHandler {
    private final H2TelegramUserRepository h2TelegramUserRepository;
    private final PostgresTelegramUserRepository postgresTelegramUserRepository;
    private final TelegramSendingService telegramSendingService;
    private final ReplyKeyboardMarkupProvider replyKeyboardMarkupProvider;

    /**
     * Method that saves new telegram user to a database for tracking the task creation/updating
     * @param userRequest that contains update data
     */
    @Override
    public void handleCommand(UserRequest userRequest) {
        log.info("Handling %s command".formatted(getCommandName()));
        TelegramUser telegramUser = TelegramUser.builder()
                .chatId(userRequest.getId())
                .name(userRequest.getUpdate().getMessage().getFrom().getUserName())
                .build();
        try {
            h2TelegramUserRepository.save(telegramUser);
        } catch (DataAccessResourceFailureException | InvalidDataAccessResourceUsageException e) {
            log.error("Failed to save telegram user to a H2 database", e);
        }
        postgresTelegramUserRepository.save(telegramUser);
        telegramSendingService.sendMessage(userRequest.getId(),
                StandartMessages.SUBSCRIBE_MESSAGE,
                replyKeyboardMarkupProvider.buildMainKeyboard(userRequest.getId()));
        log.info("Successfully subscribed chat with id %d".formatted(userRequest.getId()));
    }
    @Override
    public String getCommandName() {
        return Commands.SUBSCRIBE;
    }
}
