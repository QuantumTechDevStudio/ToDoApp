package emulator.ru.todoapp.model;

/**
 * Request for adding new task.
 *
 * @param userUUID          users UUID
 * @param description       task description
 * @param zonedDateTime     time of task to task place in
 */
public record AddTaskRequestEmulatorDTO(String userUUID, String description, String zonedDateTime) {
}
