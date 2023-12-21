package ru.todoapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Базовый класс запроса
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class RequestDTO {
    /**
     * Уникальный UUID запроса
     */
    private String requestUUID;

    /**
     * UUID пользователя
     */
    private String userUUID;

    /**
     * Тип запроса
     */
    private RequestType type;
}
