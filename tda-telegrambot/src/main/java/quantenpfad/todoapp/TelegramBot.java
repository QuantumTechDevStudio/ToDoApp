package quantenpfad.todoapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Basic telegram-bot, that can receive and reply commands
 */
@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    @Autowired
    public TelegramBot(BotConfig botConfig) {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Telegram bot received an update: {}", update.getUpdateId());
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    private void executeCommand(TelegramBotCommand command) {
        if (command == TelegramBotCommand.REGISTER) {

        }
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error during sending telegram message. ChatId: {}", chatId);
        }
    }

    private boolean isTextMessage(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }
}
