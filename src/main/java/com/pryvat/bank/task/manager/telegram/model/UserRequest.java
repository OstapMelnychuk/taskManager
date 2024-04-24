package com.pryvat.bank.task.manager.telegram.model;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * A model that contains user updates from a bot
 */
@Data
@Builder
public class UserRequest {
    /**
     * Chat id
     */
    private Long id;
    /**
     * User updates
     */
    private Update update;
}
