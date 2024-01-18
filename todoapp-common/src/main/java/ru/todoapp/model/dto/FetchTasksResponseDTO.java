package ru.todoapp.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response sent in successful case of FetchTasksRequestDTO
 */
@Getter
@NoArgsConstructor
public class FetchTasksResponseDTO extends RequestDTO {
    /**
     * List of tasks for user from FetchTaskRequestDTO
     */
    private List<TaskDTO> tasks;

    public FetchTasksResponseDTO(String requestUUID, String userUUID, List<TaskDTO> tasks) {
        super(requestUUID, userUUID, RequestType.FETCH_TASKS);
        this.tasks = tasks;
    }
}
