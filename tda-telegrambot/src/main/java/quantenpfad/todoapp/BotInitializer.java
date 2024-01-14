package quantenpfad.todoapp;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

/**
 * Telegram bot(s) initializer. Can initialize several bots at ones.
 */
@Component
@Slf4j
@AllArgsConstructor
public class BotInitializer {
    private final TelegramBot telegramBot;

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBot);
            log.trace("Following bots were registered: {}", List.of(
                    telegramBot.getBotUsername()
            ));
        } catch (TelegramApiException e) {
            log.error("Error during initialing telegram bots");
        }
    }
}
