package ru.todoapp.service;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.todoapp.model.RequestEntity;
import ru.todoapp.model.SaveTaskEntity;
import ru.todoapp.model.dto.AddTaskRequestDTO;
import ru.todoapp.model.dto.RequestResultDTO;
import ru.todoapp.model.dto.RequestStatus;
import ru.todoapp.repository.RequestRepository;
import ru.todoapp.repository.TaskRepository;
import ru.todoapp.repository.UserRepository;
import ru.todoapp.utils.KafkaTopics;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for Task related requests
 */
@Service
@RequiredArgsConstructor
public class TaskService {
    private final KafkaTemplate<String, RequestResultDTO> requestResultDTOKafkaTemplate;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    /**
     * Task addition handler
     */
    public void handle(AddTaskRequestDTO addTaskRequestDTO) {
        requestRepository.save(RequestEntity.of(addTaskRequestDTO));

        List<String> unfilled = allUnfilledFields(addTaskRequestDTO);
        if (!unfilled.isEmpty()) {
            sendRequestResultDTO(addTaskRequestDTO.getRequestUUID(),
                    RequestStatus.FAIL,
                    "Fields: " + String.join(", ", unfilled) + " are empty!");
        } else if (!userRepository.exists(addTaskRequestDTO.getUserUUID())){
            sendRequestResultDTO(addTaskRequestDTO.getRequestUUID(), RequestStatus.FAIL,
                    "Can't find user!");
        } else if (!correctTimeFormat(addTaskRequestDTO.getDatetime())) {
            sendRequestResultDTO(addTaskRequestDTO.getRequestUUID(), RequestStatus.FAIL,
                    "Can't create task with incorrect datetime!");
        } else if (Instant.now().isAfter(getTimeInstant(addTaskRequestDTO.getDatetime()))) {
            sendRequestResultDTO(addTaskRequestDTO.getRequestUUID(), RequestStatus.FAIL,
                    "Can't create task in past!");
        } else {
            taskRepository.saveNewTask(SaveTaskEntity.of(addTaskRequestDTO));
            sendRequestResultDTO(addTaskRequestDTO.getRequestUUID(), RequestStatus.SUCCESS, null);
        }
    }

    /**
     * creates a list of all unfilled fields
     * @param addTaskRequestDTO - the object checked for unfilled fields
     */
    private List<String> allUnfilledFields(AddTaskRequestDTO addTaskRequestDTO) {
        List<String> unfilled = new ArrayList<>();
        if (StringUtils.isEmpty(addTaskRequestDTO.getUserUUID())) {
            unfilled.add("userUUID");
        }
        if (StringUtils.isEmpty(addTaskRequestDTO.getDescription())) {
            unfilled.add("description");
        }
        if (addTaskRequestDTO.getDatetime() == null) {
            unfilled.add("datetime");
        }
        return unfilled;
    }

    /**
     * method that sends response in Kafka
     */
    private void sendRequestResultDTO(String requestUUID, RequestStatus status, @Nullable String message) {
        var result = RequestResultDTO.builder()
                .requestUUID(requestUUID)
                .status(status)
                .message(message)
                .build();
        requestResultDTOKafkaTemplate.send(KafkaTopics.REQUEST_RESULT_TOPIC, result);
    }

    /**
     * method to check for correct formatting
     * @param time to check for formatting
     */
    private boolean correctTimeFormat(ZonedDateTime time) {
        try {
            time.toInstant();
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * method that creates correct Instant of time
     */
    private Instant getTimeInstant(ZonedDateTime time) {
        return time.toInstant();
    }
}
