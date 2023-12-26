package emulator.ru.todoapp.model;

/**
 * Запрос Register.
 *
 * @param userUUID  UUID пользователя
 * @param name      Имя пользователя
 * @param surname   Фамилия пользователя
 */
public record RegisterRequestEmulatorDTO(String userUUID, String name, String surname) {
}
