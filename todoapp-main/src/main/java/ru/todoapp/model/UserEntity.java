package ru.todoapp.model;

import ru.todoapp.model.dto.RegisterRequestDTO;

/**
 * User entity for saving in DataBase
 *
 * @param userUUID  unique user UUID - primary key
 * @param name      - users name
 * @param surname   - users surname
 */
public record UserEntity(String userUUID, String name, String surname) {
    public static UserEntity of (RegisterRequestDTO registerRequestDTO) {
        return new UserEntity(registerRequestDTO.getUserUUID(), registerRequestDTO.getName(), registerRequestDTO.getSurname());
    }
}
