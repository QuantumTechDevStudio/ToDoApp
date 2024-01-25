package ru.todoapp.service;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.todoapp.model.GetTaskRequestEntity;
import ru.todoapp.model.RequestEntity;
import ru.todoapp.model.SaveTaskEntity;
import ru.todoapp.model.TaskEntity;
import ru.todoapp.model.dto.*;
import ru.todoapp.repository.RequestRepository;
import ru.todoapp.repository.TaskRepository;
import ru.todoapp.repository.UserRepository;
import ru.todoapp.utils.DateTimeUtils;
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
        List<String> unfilled = allUnfilledFields(addTaskRequestDTO);
        if (!unfilled.isEmpty()) {
            sendRequestResultDTO(addTaskRequestDTO.getRequestUUID(),
                    RequestStatus.FAIL,
                    "Fields: " + String.join(", ", unfilled) + " are empty!");
        } else if (!userRepository.exists(addTaskRequestDTO.getUserUUID())) {
            sendRequestResultDTO(addTaskRequestDTO.getRequestUUID(), RequestStatus.FAIL,
                    "Can't find user!");
        } else if (!DateTimeUtils.isCorrectTimeFormat(addTaskRequestDTO.getDatetime())) {
            sendRequestResultDTO(addTaskRequestDTO.getRequestUUID(), RequestStatus.FAIL,
                    "Can't create task with incorrect datetime!");
        } else if (Instant.now().isAfter(Instant.parse(addTaskRequestDTO.getDatetime()))) {
            sendRequestResultDTO(addTaskRequestDTO.getRequestUUID(), RequestStatus.FAIL,
                    "Can't create task in past!");
        } else {
            taskRepository.saveNewTask(SaveTaskEntity.of(addTaskRequestDTO));
            sendRequestResultDTO(addTaskRequestDTO.getRequestUUID(), RequestStatus.SUCCESS, null);
        }

        requestRepository.save(RequestEntity.of(addTaskRequestDTO));
    }

    /**
     * Task fetching handler
     */
    public void handleFetching(FetchTasksRequestDTO fetchTasksRequestDTO) {
        List<String> unfilled = allUnfilledFields(fetchTasksRequestDTO);
        if (!unfilled.isEmpty()) {
            sendRequestResultDTO(fetchTasksRequestDTO.getRequestUUID(),
                    RequestStatus.FAIL,
                    "Fields: " + String.join(", ", unfilled) + " are empty!");
        } else if (!userRepository.exists(fetchTasksRequestDTO.getUserUUID())) {
            sendRequestResultDTO(fetchTasksRequestDTO.getRequestUUID(), RequestStatus.FAIL,
                    "Can't find user!");
        } else if (!DateTimeUtils.isCorrectDateFormat(fetchTasksRequestDTO.getBeginDate())
                || !DateTimeUtils.isCorrectDateFormat(fetchTasksRequestDTO.getEndDate())) {
            sendRequestResultDTO(fetchTasksRequestDTO.getRequestUUID(), RequestStatus.FAIL,
                    "Incorrect search filter: " + incorrectDateOrDates(fetchTasksRequestDTO)
                            + " have incorrect format!");
        } else if (DateTimeUtils.setAtTheStartOfTheDay(fetchTasksRequestDTO.getBeginDate())
                .isAfter(DateTimeUtils.setAtTheEndOfTheDay(fetchTasksRequestDTO.getEndDate()))) {
            sendRequestResultDTO(fetchTasksRequestDTO.getRequestUUID(), RequestStatus.FAIL,
                    "Incorrect search filter: begin date is after end date!");
        } else {
            List<TaskEntity> tasksEntities = taskRepository.getTasksList(GetTaskRequestEntity.of(fetchTasksRequestDTO));
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
     * creates a list of all unfilled fields for adding tasks
     *
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
        if (StringUtils.isEmpty(addTaskRequestDTO.getDatetime())) {
            unfilled.add("datetime");
        }
        return unfilled;
    }

    /**
     * creates a list of all unfilled fields for fetching tasks
     *
     * @param fetchTasksRequestDTO - the object checked for unfilled fields
     */
    private List<String> allUnfilledFields(FetchTasksRequestDTO fetchTasksRequestDTO) {
        List<String> unfilled = new ArrayList<>();
        if (StringUtils.isEmpty(fetchTasksRequestDTO.getUserUUID())) {
            unfilled.add("userUUID");
        }
        if (StringUtils.isEmpty(fetchTasksRequestDTO.getBeginDate())) {
            unfilled.add("beginDate");
        }
        if (StringUtils.isEmpty(fetchTasksRequestDTO.getEndDate())) {
            unfilled.add("endDate");
        }
        return unfilled;
    }

    /**
     * creates a string in plural or singular format for sending response of fetching tasks
     * method should be used in case then you already know that at least one of them is incorrect
     */
    private String incorrectDateOrDates(FetchTasksRequestDTO fetchTasksRequestDTO) {
        if (!DateTimeUtils.isCorrectDateFormat(fetchTasksRequestDTO.getBeginDate())
                && DateTimeUtils.isCorrectDateFormat(fetchTasksRequestDTO.getEndDate())) {
            return "beginDate";
        } else if (DateTimeUtils.isCorrectDateFormat(fetchTasksRequestDTO.getBeginDate())
                && !DateTimeUtils.isCorrectDateFormat(fetchTasksRequestDTO.getEndDate())) {
            return "endDate";
        } else {
            return "dates";
        }
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