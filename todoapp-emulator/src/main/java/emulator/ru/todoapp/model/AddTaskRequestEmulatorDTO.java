package emulator.ru.todoapp.model;

import java.time.ZonedDateTime;

/**
 * Request for adding new task.
 *
 * @param userUUID          users UUID
 * @param description       task description
 * @param zonedDateTime     time of task to task place in
 */
public record AddTaskRequestEmulatorDTO(String userUUID, String description, ZonedDateTime zonedDateTime) {
}
