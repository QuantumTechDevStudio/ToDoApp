package ru.todoapp.service;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.todoapp.model.RequestEntity;
import ru.todoapp.model.TaskEntity;
import ru.todoapp.model.dto.AddTaskRequestDTO;
import ru.todoapp.model.dto.FetchTasksRequestDTO;
import ru.todoapp.model.dto.FetchTasksResponseDTO;
import ru.todoapp.model.dto.RequestResultDTO;
import ru.todoapp.model.dto.RequestStatus;
import ru.todoapp.model.dto.TaskDTO;
import ru.todoapp.repository.RequestRepository;
import ru.todoapp.repository.TaskRepository;
import ru.todoapp.repository.UserRepository;
import ru.todoapp.utils.KafkaTopics;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for Task related requests
 */
@Service
@RequiredArgsConstructor
public class TaskService {
    private final KafkaTemplate<String, RequestResultDTO> requestResultDTOKafkaTemplate;
    private final KafkaTemplate<String, FetchTasksResponseDTO> fetchTasksResponseDTOKafkaTemplate;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    /**
     * Task addition handler
     */
    public void handleAddition(AddTaskRequestDTO addTaskRequestDTO) {
        if (addTaskRequestDTO.isAnyUnfilledFields()) {
            sendRequestResultDTO(addTaskRequestDTO.getRequestUUID(),
                    RequestStatus.FAIL,
                    addTaskRequestDTO.toString());
        } else if (!userRepository.exists(addTaskRequestDTO.getUserUUID())) {
            sendRequestResultDTO(addTaskRequestDTO.getRequestUUID(), RequestStatus.FAIL,
                    "Can't find user!");
        } else if (Instant.now().isAfter(addTaskRequestDTO.getDatetime())) {
            sendRequestResultDTO(addTaskRequestDTO.getRequestUUID(), RequestStatus.FAIL,
                    "Can't create task in past!");
        } else {
            taskRepository.saveNewTask(TaskEntity.of(addTaskRequestDTO));
            sendRequestResultDTO(addTaskRequestDTO.getRequestUUID(), RequestStatus.SUCCESS, null);
        }

        requestRepository.save(RequestEntity.of(addTaskRequestDTO));
    }

    /**
     * Task fetching handler
     */
    public void handleFetching(FetchTasksRequestDTO fetchTasksRequestDTO) {
        if (fetchTasksRequestDTO.isAnyUnfilledFields()) {
            sendRequestResultDTO(fetchTasksRequestDTO.getRequestUUID(),
                    RequestStatus.FAIL,
                    fetchTasksRequestDTO.toString());
        } else if (!userRepository.exists(fetchTasksRequestDTO.getUserUUID())) {
            sendRequestResultDTO(fetchTasksRequestDTO.getRequestUUID(), RequestStatus.FAIL,
                    "Can't find user!");
        } else if (fetchTasksRequestDTO.getBeginDate()
                .isAfter(fetchTasksRequestDTO.getEndDate())) {
            sendRequestResultDTO(fetchTasksRequestDTO.getRequestUUID(), RequestStatus.FAIL,
                    "Incorrect search filter: begin date is after end date!");
        } else {
            List<TaskEntity> tasksEntities = taskRepository.getTasksList(fetchTasksRequestDTO);
            List<TaskDTO> taskDTOS = convertTaskEntityListTypeToDTO(tasksEntities);
            var result = new FetchTasksResponseDTO(
                    fetchTasksRequestDTO.getRequestUUID(),
                    fetchTasksRequestDTO.getUserUUID(),
                    taskDTOS);
            sendRequestResultDTO(fetchTasksRequestDTO.getRequestUUID(), RequestStatus.SUCCESS,
                    null);
            fetchTasksResponseDTOKafkaTemplate.send(KafkaTopics.FETCH_RESPONSE_TASKS_TOPIC, result);
        }

        requestRepository.save(RequestEntity.of(fetchTasksRequestDTO));
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
     * method that convert List<TaskEntity> to List<TaskDTO>
     */
    private List<TaskDTO> convertTaskEntityListTypeToDTO(List<TaskEntity> taskEntities) {
        List<TaskDTO> taskDTOS = new ArrayList<>();
        if (!taskEntities.isEmpty()) {
            for (TaskEntity taskEntity : taskEntities) {
                taskDTOS.add(taskEntity.toTaskDTO());
            }
        }
        return taskDTOS;
    }
}