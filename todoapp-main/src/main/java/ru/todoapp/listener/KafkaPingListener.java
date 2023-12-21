package ru.todoapp.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.todoapp.configuration.KafkaConfig;
import ru.todoapp.model.dto.PingRequestDTO;
import ru.todoapp.service.PingService;
import ru.todoapp.utils.KafkaTopics;

/**
 * Listener для обработки сообщений Ping
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaPingListener {
    private final PingService pingService;

    /**
     * Обработчик сообщений PingRequestDTO, получаемых из Kafka
     *
     * @see PingRequestDTO
     */
    @KafkaListener(topics = KafkaTopics.PING_TOPIC,
            groupId = KafkaConfig.GROUP_ID,
            containerFactory = "pingRequestContainerFactory")
    public void handlePingMessage(PingRequestDTO pingRequest) {
        log.info("Received message with type \"{}\": {}", pingRequest.getType(), pingRequest);
        pingService.handle(pingRequest);
    }
}
