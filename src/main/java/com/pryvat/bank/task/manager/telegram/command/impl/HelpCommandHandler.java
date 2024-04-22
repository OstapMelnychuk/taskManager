package com.pryvat.bank.task.manager.telegram.command.impl;

import com.pryvat.bank.task.manager.telegram.command.CommandHandler;
import com.pryvat.bank.task.manager.telegram.constants.Commands;
import com.pryvat.bank.task.manager.telegram.constants.StandartMessages;
import com.pryvat.bank.task.manager.telegram.model.UserRequest;
import com.pryvat.bank.task.manager.telegram.service.TelegramSendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelpCommandHandler implements CommandHandler {
    private final TelegramSendingService telegramSendingService;

    @Override
    public void handleCommand(UserRequest userRequest) {
        telegramSendingService.sendMessage(userRequest.getId(), StandartMessages.HELP_MESSAGE);
    }

    @Override
    public String getCommandName() {
        return Commands.HELP;
    }
}
