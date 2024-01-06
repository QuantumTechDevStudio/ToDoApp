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
    private String requestUUID;

    /**
     * User UUID
     */
    private String userUUID;

    /**
     * Request type
     */
    private RequestType type;
}
