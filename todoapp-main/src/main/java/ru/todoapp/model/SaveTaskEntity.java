package ru.todoapp.model;

import ru.todoapp.model.dto.AddTaskRequestDTO;

import java.time.Instant;

/**
 * Task entity to be saved in database
 *
 * @param description   - task description
 * @param userUUID      - userUUID of user that planned task
 * @param datetime      - timestamp then the task planned to take place
 */
public record SaveTaskEntity(String description, String userUUID, Instant datetime) {
    public static SaveTaskEntity of (AddTaskRequestDTO addTaskRequestDTO) {
        return new SaveTaskEntity(
                addTaskRequestDTO.getDescription(),
                addTaskRequestDTO.getUserUUID(),
                Instant.parse(addTaskRequestDTO.getDatetime()));
    }
}
