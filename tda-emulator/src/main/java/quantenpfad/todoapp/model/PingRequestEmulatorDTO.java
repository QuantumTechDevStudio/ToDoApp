package quantenpfad.todoapp.model;

import quantenpfad.todoapp.model.dto.PingRequestDTO;

/**
 * Ping request
 *
 * @param userUUID users UUID
 * @see PingRequestDTO
 */
public record PingRequestEmulatorDTO(String userUUID) {
}
