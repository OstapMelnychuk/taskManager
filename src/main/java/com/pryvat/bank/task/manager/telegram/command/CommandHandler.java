package com.pryvat.bank.task.manager.telegram.command;

import com.pryvat.bank.task.manager.telegram.model.UserRequest;

public interface CommandHandler {
    void handleCommand(UserRequest userRequest);
    String getCommandName();
}
