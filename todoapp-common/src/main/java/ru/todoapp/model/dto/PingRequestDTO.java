package ru.todoapp.model.dto;

import lombok.NoArgsConstructor;

/**
 * Ping request.
 * Allows to check that service receives data from Kafka
 */
@NoArgsConstructor
public class PingRequestDTO extends RequestDTO {
    public PingRequestDTO(String requestUUID, String userUUID) {
        super(requestUUID, userUUID, RequestType.PING);
    }
}
