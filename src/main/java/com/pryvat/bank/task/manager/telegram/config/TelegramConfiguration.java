package com.pryvat.bank.task.manager.telegram.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;

/**
 * Telegram bot configuration
 */
@Configuration
@Getter
public class TelegramConfiguration {
    /**
     * Bot api token
     */
    @Value("${telegram.bot.token}")
    private String botToken;

    /**
     * Telegram client bean that is used for communication with telegram api
     * @return telegram client
     */
    @Bean
    public OkHttpTelegramClient okClientHttp() {
        return new OkHttpTelegramClient(botToken);
    }
}
