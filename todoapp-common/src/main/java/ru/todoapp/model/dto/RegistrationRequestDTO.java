package ru.todoapp.model.dto;

/**
 * Запрос на регистацию пользователя
 */
public class RegistrationRequestDTO extends RequestDTO {
    private String name;
    private String surname;
    public RegistrationRequestDTO(String requestUUID, String userUUID, String name, String surname) {
        super(requestUUID, userUUID, RequestType.REGISTER);
        this.name = name;
        this.surname = surname;
    }
}
