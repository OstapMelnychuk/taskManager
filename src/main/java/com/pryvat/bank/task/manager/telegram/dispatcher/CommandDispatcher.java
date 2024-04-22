package com.pryvat.bank.task.manager.telegram.dispatcher;

import com.pryvat.bank.task.manager.telegram.command.CommandHandler;
import com.pryvat.bank.task.manager.telegram.model.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CommandDispatcher {
    private final ApplicationContext applicationContext;
    public void dispatchCommandToAHandler(UserRequest userRequest) {
        Map<String, CommandHandler> handlers = applicationContext.getBeansOfType(CommandHandler.class);
        for (CommandHandler commandHandler : handlers.values()) {
            if (commandHandler.getCommandName().equals(userRequest.getUpdate().getMessage().getText())) {
                commandHandler.handleCommand(userRequest);
            }
        }
    }
}
