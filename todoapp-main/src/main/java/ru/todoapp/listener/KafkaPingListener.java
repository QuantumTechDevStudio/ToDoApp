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
 * Ping message processing Listener
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaPingListener {
    private final PingService pingService;

    /**
     * PingRequestDTO massage received from Kafka handler
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
