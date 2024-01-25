package emulator.ru.todoapp.model;

public record FetchTasksRequestEmulatorDTO(String userUUID, String beginDate, String endDate) {
}
