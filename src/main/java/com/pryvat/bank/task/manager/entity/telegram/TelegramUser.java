package com.pryvat.bank.task.manager.entity.telegram;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Telegram user database representation
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TelegramUser {
    /**
     * Chat id identifier
     */
    @Id
    private Long chatId;
    /**
     * Name of the user or chat
     */
    private String name;
}
