package com.pryvat.bank.task.manager.telegram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * TelegramSendingService that sends messages to the telegram user
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class TelegramSendingService {
    /**
     * Telegram client for the message sending
     */
    private final TelegramClient telegramClient;

    /**
     * Method that send message to a telegram user without keyboard updates
     * @param chatId of a user
     * @param text message
     */
    public void sendMessage(Long chatId, String text) {
        sendMessage(chatId, text, null);
    }

    /**
     * Method that send message to a telegram user and updates his keyboard
     * @param chatId of a user
     * @param text message
     * @param replyKeyboard of actions available for the user
     */
    public void sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard) {
        log.info("Trying to send message to a chat with id %d".formatted(chatId));
        SendMessage sendMessage = SendMessage
                .builder()
                .text(text)
                .chatId(chatId.toString())
                .parseMode(ParseMode.HTML)
                .replyMarkup(replyKeyboard)
                .build();
        execute(sendMessage);
    }

    private void execute(BotApiMethod botApiMethod) {
        try {
            telegramClient.execute(botApiMethod);
        } catch (Exception e) {
            log.error("Error during sending message: ", e);
        }
    }
}
