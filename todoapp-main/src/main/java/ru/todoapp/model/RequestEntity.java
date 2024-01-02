package ru.todoapp.model;

import ru.todoapp.model.dto.RequestDTO;
import ru.todoapp.model.dto.RequestType;

import java.time.Instant;

/**
 * "Request" entity for saving information about request in DataBase
 *
 * @param requestUUID      unique request UUID - primary key
 * @param requestType      - request type
 * @param requestTimestamp - request timestamp
 */
public record RequestEntity(String requestUUID, RequestType requestType, Instant requestTimestamp) {
    public static RequestEntity of(RequestDTO request) {
        return new RequestEntity(request.getRequestUUID(), request.getType(), Instant.now());
    }
}
