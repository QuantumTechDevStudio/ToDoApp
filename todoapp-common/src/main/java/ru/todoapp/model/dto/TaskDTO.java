package ru.todoapp.model.dto;

import lombok.Builder;

import java.time.Instant;

/**
 * DTO of task that will be sent in FetchTasksResponseDTO
 *
 * @param id          - id of task (will be useful for deleting/changing task)
 * @param description - description of task (aka what user plans to do)
 * @param datetime    - datetime then the task is planned to happen
 */
@Builder
public record TaskDTO(long id, String description, Instant datetime) {
}
