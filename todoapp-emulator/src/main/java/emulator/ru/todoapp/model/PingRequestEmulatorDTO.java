package emulator.ru.todoapp.model;

/**
 * Запрос Ping
 *
 * @param userUUID UUID пользователя
 * @see ru.todoapp.model.dto.PingRequestDTO
 */
public record PingRequestEmulatorDTO(String userUUID) {
}
