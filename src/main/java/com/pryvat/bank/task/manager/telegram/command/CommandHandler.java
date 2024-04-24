package com.pryvat.bank.task.manager.telegram.command;

import com.pryvat.bank.task.manager.telegram.model.UserRequest;

/**
 * Basic CommandHandler that is used to handle a telegram bot command
 */
public interface CommandHandler {
    /**
     * Handles some telegram command
     * @param userRequest that contains update data
     */
    void handleCommand(UserRequest userRequest);

    /**
     * Method that returns command name to handle updates to a right handler
     * @return {@link com.pryvat.bank.task.manager.telegram.constants.Commands} command name
     */
    String getCommandName();
}
