package ru.todoapp.model;

import ru.todoapp.model.dto.AddTaskRequestDTO;
import ru.todoapp.model.dto.TaskDTO;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Task entity to be used in database
 *
 * @param id          - task id
 * @param description - task description
 * @param userUUID    - userUUID of user that planned task
 * @param datetime    - timestamp then the task planned to take place
 */
public record TaskEntity(Long id, String description, String userUUID, OffsetDateTime datetime) {
    public static TaskEntity of(AddTaskRequestDTO addTaskRequestDTO) {
        return new TaskEntity(null,
                addTaskRequestDTO.getDescription(),
                addTaskRequestDTO.getUserUUID(),
                Instant.parse(addTaskRequestDTO.getDatetime()).atOffset(ZoneOffset.UTC));
    }

    public TaskDTO toTaskDTO() {
        return new TaskDTO(id, description, datetime.toString());
    }
}
