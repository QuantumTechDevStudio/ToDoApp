package emulator.ru.todoapp.listener;

import emulator.ru.todoapp.configuration.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.todoapp.model.dto.RequestResultDTO;
import ru.todoapp.utils.KafkaTopics;

/**
 * Request result message listener
 */
@Slf4j
@Service
public class RequestResultListener {
    /**
     * Kafka massage handler for RequestResultDTO
     */
    @KafkaListener(topics = KafkaTopics.REQUEST_RESULT_TOPIC,
            groupId = KafkaConfig.GROUP_ID,
            containerFactory = "requestResultContainerFactory")
    public void handleRequestResultMessage(RequestResultDTO requestResultDTO) {
        log.info("Received request result: {}", requestResultDTO);
    }
}
