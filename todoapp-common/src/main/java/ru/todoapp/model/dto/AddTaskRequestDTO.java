package ru.todoapp.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * Add task request.
 * Allows for user to save new planned tasks for themselves
 */
@Getter
@NoArgsConstructor
public class AddTaskRequestDTO extends RequestDTO {
    /**
     * task description/name aka whatever the user is planning to do
     */
    private String description;

    /**
     * datetime, when the task is planned to take place, in ISO 8601 format (yyyy-MM-ddThh:mm:ss+hh)
     */
    private String datetime;

    public AddTaskRequestDTO(String requestUUID, String userUUID, String description, ZonedDateTime datetime) {
        super(requestUUID, userUUID, RequestType.ADD_TASK);
        this.description = description;
        this.datetime = datetime.toString();
    }
}
