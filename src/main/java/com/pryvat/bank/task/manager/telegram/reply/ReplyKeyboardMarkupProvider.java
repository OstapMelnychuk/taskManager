package com.pryvat.bank.task.manager.telegram.reply;

import com.pryvat.bank.task.manager.repository.h2.telegram.H2TelegramUserRepository;
import com.pryvat.bank.task.manager.repository.postgres.task.PostgresSqlTaskRepository;
import com.pryvat.bank.task.manager.telegram.constants.Commands;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

/**
 * ReplyKeyboardMarkupProvider that provides keyboard for a user
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class ReplyKeyboardMarkupProvider {
    private final H2TelegramUserRepository h2TelegramUserRepository;
    private final PostgresSqlTaskRepository postgresSqlTaskRepository;

    /**
     * Method that creates a keyboard for a user
     * @param chatId that is used to check if a user is subscribed for the task tracking
     * @return user keyboard with possible bot actions
     */
    public ReplyKeyboardMarkup buildMainKeyboard(Long chatId) {
        KeyboardRow firstKeyboardRow = new KeyboardRow();
        KeyboardRow secondKeyboardRow = new KeyboardRow();
        firstKeyboardRow.add(Commands.HELP);
        boolean userExistsInDB;
        try {
            userExistsInDB = h2TelegramUserRepository.existsById(chatId);
        } catch (DataAccessResourceFailureException | InvalidDataAccessResourceUsageException e) {
            log.error("Failed to check if telegram user exists in H2 database", e);
            userExistsInDB = postgresSqlTaskRepository.existsById(chatId);
        }
        if (userExistsInDB) {
            firstKeyboardRow.add(Commands.UNSUBSCRIBE);
        } else {
            firstKeyboardRow.add(Commands.SUBSCRIBE);
        }
        secondKeyboardRow.add(Commands.GET_ALL_TASKS);

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(firstKeyboardRow, secondKeyboardRow))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }
}
