package com.pryvat.bank.task.manager.telegram.command.impl;

import com.pryvat.bank.task.manager.repository.telegram.TelegramUserRepository;
import com.pryvat.bank.task.manager.telegram.command.CommandHandler;
import com.pryvat.bank.task.manager.telegram.constants.Commands;
import com.pryvat.bank.task.manager.telegram.constants.StandartMessages;
import com.pryvat.bank.task.manager.telegram.model.UserRequest;
import com.pryvat.bank.task.manager.telegram.reply.ReplyKeyboardMarkupProvider;
import com.pryvat.bank.task.manager.telegram.service.TelegramSendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnsubscribeCommandHandler implements CommandHandler {
    private final TelegramUserRepository telegramUserRepository;
    private final TelegramSendingService telegramSendingService;
    private final ReplyKeyboardMarkupProvider replyKeyboardMarkupProvider;

    @Override
    public void handleCommand(UserRequest userRequest) {
        telegramUserRepository.deleteById(userRequest.getId());
        telegramSendingService.sendMessage(userRequest.getId(),
                StandartMessages.UNSUBSCRIBE_MESSAGE,
                replyKeyboardMarkupProvider.buildMainKeyboard(userRequest.getId()));
    }

    @Override
    public String getCommandName() {
        return Commands.UNSUBSCRIBE;
    }
}
