package emulator.ru.todoapp.model;

/**
 * Request for fetching a list of tasks
 *
 * @param userUUID  - UUID of user for whom we want to fetch tasks
 * @param beginDate - date from which we want to display tasks in format (yyyy-MM-dd) tasks will be included from 00:00:00 by the local time
 * @param endDate   - date to which we want to display tasks in format (yyyy-MM-dd) tasks will be included to 23:59:59 by the local time
 */
public record FetchTasksRequestEmulatorDTO(String userUUID, String beginDate, String endDate) {
}
