package quantenpfad.todoapp;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * All commands dedicated to ToDoApp telegram-bot
 */
@AllArgsConstructor
@Getter
public enum TelegramBotCommand {

    /**
     * Basic user registration
     */
    REGISTER("/register");

    private final String text;
}
