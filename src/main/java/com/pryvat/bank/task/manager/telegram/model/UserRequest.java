package com.pryvat.bank.task.manager.telegram.model;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Update;

@Data
@Builder
public class UserRequest {
    private Long id;
    private Update update;
}
