package emulator.ru.todoapp.listener;

import emulator.ru.todoapp.configuration.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.todoapp.model.dto.RequestResultDTO;
import ru.todoapp.utils.KafkaTopics;

/**
 * Listener сообщений о результате выполнения запроса
 */
@Slf4j
@Service
public class RequestResultListener {
    /**
     * Обработчик сообщений из Kafka для RequestResultDTO
     */
    @KafkaListener(topics = KafkaTopics.REQUEST_RESULT_TOPIC,
            groupId = KafkaConfig.GROUP_ID,
            containerFactory = "requestResultContainerFactory")
    public void handleRequestResultMessage(RequestResultDTO requestResultDTO) {
        log.info("Received request result: {}", requestResultDTO);
    }
}
