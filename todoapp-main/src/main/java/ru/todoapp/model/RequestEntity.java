package ru.todoapp.model;

import ru.todoapp.model.dto.RequestDTO;
import ru.todoapp.model.dto.RequestType;

import java.time.Instant;

/**
 * Сущность "запрос" для сохранения информации о запросе в БД
 *
 * @param requestUUID      уникальный UUID запроса - первичный ключ
 * @param requestType      - тип запроса
 * @param requestTimestamp - временная метка запроса
 */
public record RequestEntity(String requestUUID, RequestType requestType, Instant requestTimestamp) {
    public static RequestEntity of(RequestDTO request) {
        return new RequestEntity(request.getRequestUUID(), request.getType(), Instant.now());
    }
}
