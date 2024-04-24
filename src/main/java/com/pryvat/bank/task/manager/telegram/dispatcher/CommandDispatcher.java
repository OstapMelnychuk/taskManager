package com.pryvat.bank.task.manager.telegram.dispatcher;

import com.pryvat.bank.task.manager.telegram.command.CommandHandler;
import com.pryvat.bank.task.manager.telegram.model.UserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * CommandDispatcher that dispatcher command to a specific {@link CommandHandler}
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class CommandDispatcher {
    private final ApplicationContext applicationContext;

    /**
     * Method that dispatches user updates to a specific {@link CommandHandler} to process
     * @param userRequest user updates from a bot
     */
    public void dispatchCommandToAHandler(UserRequest userRequest) {
        log.info("Dispatching user request to an appropriate handler");
        Map<String, CommandHandler> handlers = applicationContext.getBeansOfType(CommandHandler.class);
        for (CommandHandler commandHandler : handlers.values()) {
            if (commandHandler.getCommandName().equals(userRequest.getUpdate().getMessage().getText())) {
                commandHandler.handleCommand(userRequest);
                return;
            }
        }
        log.info("No handler for a command %s has been found".formatted(userRequest.getUpdate().getMessage().getText()));
    }
}
