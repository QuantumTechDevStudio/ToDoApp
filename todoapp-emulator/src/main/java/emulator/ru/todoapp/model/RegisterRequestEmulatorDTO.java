package emulator.ru.todoapp.model;

/**
 * Register request.
 *
 * @param userUUID  users UUID
 * @param name      users name
 * @param surname   users surname
 */
public record RegisterRequestEmulatorDTO(String userUUID, String name, String surname) {
}
