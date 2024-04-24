package com.pryvat.bank.task.manager.telegram.command.impl;

import com.pryvat.bank.task.manager.telegram.command.CommandHandler;
import com.pryvat.bank.task.manager.telegram.constants.Commands;
import com.pryvat.bank.task.manager.telegram.constants.StandartMessages;
import com.pryvat.bank.task.manager.telegram.model.UserRequest;
import com.pryvat.bank.task.manager.telegram.service.TelegramSendingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * HelpCommandHandler that handles Help command
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class HelpCommandHandler implements CommandHandler {
    private final TelegramSendingService telegramSendingService;

    /**
     * Handles Help command. Sends command description message to a user
     * @param userRequest that contains update data
     */
    @Override
    public void handleCommand(UserRequest userRequest) {
        log.info("Handling %s command".formatted(getCommandName()));
        telegramSendingService.sendMessage(userRequest.getId(), StandartMessages.HELP_MESSAGE);
        log.info("Successfully handled %s command".formatted(getCommandName()));
    }

    @Override
    public String getCommandName() {
        return Commands.HELP;
    }
}
