package quantenpfad.todoapp.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Register request.
 * Allows for user to be registered
 */
@Getter
@NoArgsConstructor
public class RegisterRequestDTO extends RequestDTO {
    /**
     * name
     */
    private String name;
    /**
     * surname
     */
    private String surname;
    public RegisterRequestDTO(String requestUUID, String userUUID, String name, String surname) {
        super(requestUUID, userUUID, RequestType.REGISTER);
        this.name = name;
        this.surname = surname;
    }
}
