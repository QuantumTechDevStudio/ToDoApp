package quantenpfad.todoapp;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * All commands dedicated to ToDoApp telegram-bot
 */
@AllArgsConstructor
@Getter
public enum BotCommand {

    /**
     * Basic user registration
     */
    REGISTER("/register", "Registration"),
    /**
     * Display all possible commands
     */
    COMMANDS("/commands", "Commands"),
    /**
     * Cancel current operation
     */
    CANCEL("/cancel", "Cancel"),
    ;

    /**
     * Command text, that can be received by bot
     */
    private final String text;
    /**
     * Readable name of command, that can be displayed to user
     */
    private final String locale;

    public static Optional<BotCommand> isKnownCommand(String text) {
        return Arrays.stream(values()).filter(command -> Objects.equals(command.getText(), text)).findFirst();
    }
}
