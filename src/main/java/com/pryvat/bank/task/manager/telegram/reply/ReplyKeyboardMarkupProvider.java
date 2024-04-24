package com.pryvat.bank.task.manager.telegram.reply;

import com.pryvat.bank.task.manager.repository.telegram.TelegramUserRepository;
import com.pryvat.bank.task.manager.telegram.constants.Commands;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

/**
 * ReplyKeyboardMarkupProvider that provides keyboard for a user
 */
@Component
@RequiredArgsConstructor
public class ReplyKeyboardMarkupProvider {
    private final TelegramUserRepository telegramUserRepository;

    /**
     * Method that creates a keyboard for a user
     * @param chatId that is used to check if a user is subscribed for the task tracking
     * @return user keyboard with possible bot actions
     */
    public ReplyKeyboardMarkup buildMainKeyboard(Long chatId) {
        KeyboardRow firstKeyboardRow = new KeyboardRow();
        KeyboardRow secondKeyboardRow = new KeyboardRow();
        firstKeyboardRow.add(Commands.HELP);
        if (telegramUserRepository.existsById(chatId)) {
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
