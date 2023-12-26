package ru.todoapp.model;

import ru.todoapp.model.dto.PingRequestDTO;
import ru.todoapp.model.dto.RequestType;
import ru.todoapp.model.dto.UserRequestDTO;

import java.time.Instant;

/**
 * Сущность "запрос" для сохранения информации о запросе в БД
 *
 * @param requestUUID      уникальный UUID запроса - первичный ключ
 * @param requestType      - тип запроса
 * @param requestTimestamp - временная метка запроса
 */
public record RequestEntity(String requestUUID, RequestType requestType, Instant requestTimestamp) {
    public static RequestEntity of(PingRequestDTO request) {
        return new RequestEntity(request.getRequestUUID(), request.getType(), Instant.now());
    }

    public static RequestEntity of(UserRequestDTO request) {
        return new RequestEntity(request.getRequestUUID(), request.getType(), Instant.now());
    }
}
