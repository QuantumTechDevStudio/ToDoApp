package ru.todoapp.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Запрос на обработку регистации пользователя
 */
@Getter
@NoArgsConstructor
public class UserRequestDTO extends RequestDTO {
    private String name;
    private String surname;
    public UserRequestDTO(String requestUUID, String userUUID, String name, String surname) {
        super(requestUUID, userUUID, RequestType.REGISTER);
        this.name = name;
        this.surname = surname;
    }
}
