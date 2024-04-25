package com.pryvat.bank.task.manager.telegram.bot;

import com.pryvat.bank.task.manager.telegram.config.TelegramConfiguration;
import com.pryvat.bank.task.manager.telegram.dispatcher.CommandDispatcher;
import com.pryvat.bank.task.manager.telegram.model.UserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Telegram bot for tracking task creation/updating
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class TaskManagerBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    /**
     * Basic Telegram bot configuration
     */
    private final TelegramConfiguration configuration;
    /**
     * Command dispatcher that dispatches command to a specific {@link com.pryvat.bank.task.manager.telegram.command.CommandHandler}
     */
    private final CommandDispatcher commandDispatcher;

    /**
     * Method that consumes updates from a telegram bot and dispatches it to a specific {@link com.pryvat.bank.task.manager.telegram.command.CommandHandler}
     * @param update updates from a telegram bot
     */
    @Override
    public void consume(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            log.info("Received an updated from chat with id %d".formatted(update.getMessage().getChatId()));
            UserRequest userRequest = UserRequest.builder()
                    .id(update.getMessage().getChatId())
                    .update(update)
                    .build();
            commandDispatcher.dispatchCommandToAHandler(userRequest);
        }
    }

    /**
     * Method that provides Telegram API with a telegram bot token
     * @return telegram bot api token
     */
    @Override
    public String getBotToken() {
        return configuration.getBotToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }
}
