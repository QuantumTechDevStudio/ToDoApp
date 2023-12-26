package ru.todoapp.model;

import ru.todoapp.model.dto.RegisterRequestDTO;

/**
 * Сущность Пользователь
 */
public record UserEntity(String userUUID, String name, String surname) {
    public static UserEntity of (RegisterRequestDTO registerRequestDTO) {
        return new UserEntity(registerRequestDTO.getUserUUID(), registerRequestDTO.getName(), registerRequestDTO.getSurname());
    }
}
