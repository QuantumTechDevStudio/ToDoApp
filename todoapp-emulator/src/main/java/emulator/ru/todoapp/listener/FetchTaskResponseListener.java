package emulator.ru.todoapp.listener;

import emulator.ru.todoapp.configuration.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.todoapp.model.dto.FetchTasksResponseDTO;
import ru.todoapp.utils.KafkaTopics;

/**
 * Fetch tasks response listener
 */
@Slf4j
@Service
public class FetchTaskResponseListener {
    /**
     * Kafka massage handler for FetchTasksResponseDTO
     */
    @KafkaListener(topics = KafkaTopics.FETCH_RESPONSE_TASKS_TOPIC,
            groupId = KafkaConfig.GROUP_ID,
            containerFactory = "fetchTasksContainerFactory")
    public void handleFetchTasksResponseMessage(FetchTasksResponseDTO fetchTasksResponseDTO) {
        log.info("Received fetched tasks response message: {}", fetchTasksResponseDTO);
    }
}
