package ru.todoapp.model.dto;

import lombok.NoArgsConstructor;

/**
 * Запрос Ping.
 * Позволяет проверить, что сервис получает данные из Kafka
 */
@NoArgsConstructor
public class PingRequestDTO extends RequestDTO {
    public PingRequestDTO(String requestUUID, String userUUID) {
        super(requestUUID, userUUID, RequestType.PING);
    }
}
