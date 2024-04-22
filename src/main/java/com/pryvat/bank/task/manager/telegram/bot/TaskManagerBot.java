package com.pryvat.bank.task.manager.telegram.bot;

import com.pryvat.bank.task.manager.telegram.config.TelegramConfiguration;
import com.pryvat.bank.task.manager.telegram.dispatcher.CommandDispatcher;
import com.pryvat.bank.task.manager.telegram.model.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class TaskManagerBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final TelegramConfiguration configuration;
    private final CommandDispatcher commandDispatcher;

    @Override
    public void consume(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            UserRequest userRequest = UserRequest.builder()
                    .id(update.getMessage().getChatId())
                    .update(update)
                    .build();
            commandDispatcher.dispatchCommandToAHandler(userRequest);
        }
    }

    @Override
    public String getBotToken() {
        return configuration.getBotToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }
}
