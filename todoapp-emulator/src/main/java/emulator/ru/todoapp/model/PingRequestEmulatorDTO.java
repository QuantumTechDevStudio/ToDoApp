package emulator.ru.todoapp.model;

/**
 * Ping request
 *
 * @param userUUID users UUID
 * @see ru.todoapp.model.dto.PingRequestDTO
 */
public record PingRequestEmulatorDTO(String userUUID) {
}
