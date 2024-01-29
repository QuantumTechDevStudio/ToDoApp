package ru.todoapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Basic request class
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class RequestDTO {
    /**
     * Unique request UUID
     */
    protected String requestUUID;

    /**
     * User UUID
     */
    protected String userUUID;

    /**
     * Request type
     */
    private RequestType type;
}
