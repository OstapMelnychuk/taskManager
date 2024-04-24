package com.pryvat.bank.task.manager.telegram.command.impl;

import com.pryvat.bank.task.manager.telegram.command.CommandHandler;
import com.pryvat.bank.task.manager.telegram.constants.Commands;
import com.pryvat.bank.task.manager.telegram.constants.StandartMessages;
import com.pryvat.bank.task.manager.telegram.model.UserRequest;
import com.pryvat.bank.task.manager.telegram.reply.ReplyKeyboardMarkupProvider;
import com.pryvat.bank.task.manager.telegram.service.TelegramSendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Handles /start command
 */
@Component
@RequiredArgsConstructor
public class StartCommandHandler implements CommandHandler {
    private final TelegramSendingService telegramSendingService;
    private final ReplyKeyboardMarkupProvider replyKeyboardMarkupProvider;

    /**
     * Sends a greeting message to a user
     * @param userRequest that contains update data
     */
    @Override
    public void handleCommand(UserRequest userRequest) {
        telegramSendingService.sendMessage(userRequest.getId(),
                StandartMessages.START_MESSAGE,
                replyKeyboardMarkupProvider.buildMainKeyboard(userRequest.getId()));
    }

    @Override
    public String getCommandName() {
        return Commands.START;
    }
}
