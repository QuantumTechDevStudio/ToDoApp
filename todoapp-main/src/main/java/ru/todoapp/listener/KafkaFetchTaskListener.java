package ru.todoapp.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.todoapp.configuration.KafkaConfig;
import ru.todoapp.model.dto.FetchTasksRequestDTO;
import ru.todoapp.service.TaskService;
import ru.todoapp.utils.KafkaTopics;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaFetchTaskListener {
    private final TaskService taskService;

    /**
     * @see FetchTasksRequestDTO
     */
    @KafkaListener(topics = KafkaTopics.FETCH_REQUEST_TASKS_TOPIC,
            groupId = KafkaConfig.GROUP_ID,
            containerFactory = "fetchTaskRequestContainerFactory")
    public void handleTaskFetchingMessage(FetchTasksRequestDTO fetchTasksRequestDTO) {

    }
}
