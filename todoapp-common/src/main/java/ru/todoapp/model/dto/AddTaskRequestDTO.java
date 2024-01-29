package ru.todoapp.model.dto;

import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

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
    private Instant datetime;

    public AddTaskRequestDTO(String requestUUID, String userUUID, String description, String datetime) {
        super(requestUUID, userUUID, RequestType.ADD_TASK);
        this.description = description;
        this.datetime = Instant.parse(datetime);
    }

    /**
     * creates a list of all unfilled fields
     */
    public boolean validateAllFields() {
        return StringUtils.isEmpty(getUserUUID()) || StringUtils.isEmpty(description) || datetime == null;
    }

    @Override
    public String toString() {
        return "AddTaskRequestDTO{" +
                "description='" + description + '\'' +
                ", datetime=" + datetime +
                ", requestUUID='" + requestUUID + '\'' +
                ", userUUID='" + userUUID + '\'' +
                '}';
    }
}
