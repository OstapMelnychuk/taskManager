package com.pryvat.bank.task.manager.telegram.command.impl;

import com.pryvat.bank.task.manager.repository.telegram.TelegramUserRepository;
import com.pryvat.bank.task.manager.telegram.command.CommandHandler;
import com.pryvat.bank.task.manager.telegram.constants.Commands;
import com.pryvat.bank.task.manager.telegram.constants.StandartMessages;
import com.pryvat.bank.task.manager.telegram.model.UserRequest;
import com.pryvat.bank.task.manager.telegram.reply.ReplyKeyboardMarkupProvider;
import com.pryvat.bank.task.manager.telegram.service.TelegramSendingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * UnsubscribeCommandHandler that handles Unsubscribe command
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class UnsubscribeCommandHandler implements CommandHandler {
    private final TelegramUserRepository telegramUserRepository;
    private final TelegramSendingService telegramSendingService;
    private final ReplyKeyboardMarkupProvider replyKeyboardMarkupProvider;

    /**
     * Method that deletes the telegram user from the database
     * User will not receive notifications about tasks
     * @param userRequest that contains update data
     */
    @Override
    public void handleCommand(UserRequest userRequest) {
        log.info("Handling %s command".formatted(getCommandName()));
        telegramUserRepository.deleteById(userRequest.getId());
        telegramSendingService.sendMessage(userRequest.getId(),
                StandartMessages.UNSUBSCRIBE_MESSAGE,
                replyKeyboardMarkupProvider.buildMainKeyboard(userRequest.getId()));
        log.info("Successfully unsubscribed chat with id %d".formatted(userRequest.getId()));
    }

    @Override
    public String getCommandName() {
        return Commands.UNSUBSCRIBE;
    }
}
