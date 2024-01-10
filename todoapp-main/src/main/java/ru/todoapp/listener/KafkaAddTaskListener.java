package ru.todoapp.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.todoapp.configuration.KafkaConfig;
import ru.todoapp.model.dto.AddTaskRequestDTO;
import ru.todoapp.service.TaskService;
import ru.todoapp.utils.KafkaTopics;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaAddTaskListener {
    private final TaskService taskService;

    /**
     * AddTaskRequestDTO message received from Kafka
     *
     * @see AddTaskRequestDTO
     */
    @KafkaListener(topics = KafkaTopics.ADD_TASK_TOPIC,
            groupId = KafkaConfig.GROUP_ID,
            containerFactory = "addTaskRequestContainerFactory")
    public void handleTaskAdditionMessage(AddTaskRequestDTO addTaskRequestDTO) {
        log.info("Received message with type \"{}\": {}", addTaskRequestDTO.getType(), addTaskRequestDTO);
        if (addTaskRequestDTO.getType() != null) {
            taskService.handle(addTaskRequestDTO);
        }
    }
}
