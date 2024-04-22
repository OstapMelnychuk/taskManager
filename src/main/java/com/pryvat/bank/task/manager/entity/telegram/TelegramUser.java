package com.pryvat.bank.task.manager.entity.telegram;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TelegramUser {
    @Id
    private Long chatId;
    private String name;
}
