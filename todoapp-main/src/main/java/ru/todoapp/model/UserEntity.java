package ru.todoapp.model;

import ru.todoapp.model.dto.UserRequestDTO;

/**
 * Сущность Пользователь
 */
public record UserEntity(String userUUID, String name, String surname) {
    public static UserEntity of (UserRequestDTO userRequestDTO) {
        return new UserEntity(userRequestDTO.getUserUUID(), userRequestDTO.getName(), userRequestDTO.getSurname());
    }
}
