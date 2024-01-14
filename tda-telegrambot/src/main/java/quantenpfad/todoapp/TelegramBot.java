package quantenpfad.todoapp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Basic telegram-bot, that can receive and reply commands
 */
@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private Long chatId;
    private State state;

    @Autowired
    public TelegramBot(BotConfig botConfig) {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Telegram bot received an update: {}", update.getUpdateId());
        if (isTextMessage(update)) {
            chatId = update.getMessage().getChatId();
            BotCommand.isKnownCommand(update.getMessage().getText()).ifPresent(this::handleCommand);
        } else if (update.hasCallbackQuery()) {
            answerCallbackQuery(update.getCallbackQuery());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    private void answerCallbackQuery(CallbackQuery callbackQuery) {
        if (StringUtils.isNoneBlank(callbackQuery.getData())) {
            chatId = callbackQuery.getMessage().getChatId();
            BotCommand.isKnownCommand(callbackQuery.getData()).ifPresent(this::handleCommand);
        }
    }

    private void handleCommand(BotCommand command) {
        if (command == BotCommand.COMMANDS) {
            SendMessage message = createTextMessage(BotReplies.ALL_COMMANDS_REPLY);
            message.setReplyMarkup(createInlineKeyboardMarkup());
            sendMessage(message);
        } else if (command == BotCommand.REGISTER) {
            sendMessage(createTextMessage(BotReplies.REGISTRATION_NAME_REPLY));
        } else if (command == BotCommand.CANCEL) {
            state = State.READY;
            sendMessage(createTextMessage(BotReplies.COMMAND_CANCELED));
        }
    }

    private void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error during sending telegram message. ChatId: {}", chatId);
        }
    }

    private SendMessage createTextMessage(String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        return sendMessage;
    }

    private InlineKeyboardMarkup createInlineKeyboardMarkup() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> keyboardFirstRow = new ArrayList<>();
        Arrays.stream(BotCommand.values()).forEach(botCommand -> {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(botCommand.getLocale());
            inlineKeyboardButton.setCallbackData(botCommand.getText());
            keyboardFirstRow.add(inlineKeyboardButton);
        });
        keyboard.add(keyboardFirstRow);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    private boolean isTextMessage(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }
}
