package ru.todoapp.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Запрос на обработку регистации пользователя
 */
@Getter
@NoArgsConstructor
public class RegisterRequestDTO extends RequestDTO {
    /**
     * имя
     */
    private String name;
    /**
     * фамилия
     */
    private String surname;
    public RegisterRequestDTO(String requestUUID, String userUUID, String name, String surname) {
        super(requestUUID, userUUID, RequestType.REGISTER);
        this.name = name;
        this.surname = surname;
    }
}
