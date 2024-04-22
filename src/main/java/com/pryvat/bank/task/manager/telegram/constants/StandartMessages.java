package com.pryvat.bank.task.manager.telegram.constants;

public class StandartMessages {
    public static final String START_MESSAGE = """
           Greetings. Welcome to a test version of the TaskManager bot.
           Here you can track creation and updating of tasks.
           Check out the buttons below to use this bot.
           Enjoy!
            """;
    public static final String SUBSCRIBE_MESSAGE = "Successfully subscribed to task creation/updating";
    public static final String UNSUBSCRIBE_MESSAGE = "Successfully unsubscribed to task creation/updating";
    public static final String NO_TASKS_CREATED_MESSAGE = "There are no created tasks";
    public static final String TASK_MESSAGE = """
                id: %d,
                name: %s,
                status: %s,
                description: %s
            """;
    public static final String HELP_MESSAGE = """
            This bot is for test purposes.
            Here you can subscribe to task creation/updating.
            Command overview:
            - Subscribe - subscribe for task creation/updating
            - Unsubscribe - subscribe for task creation/updating
            - Get all tasks - get list of all tasks created
            """;
}
