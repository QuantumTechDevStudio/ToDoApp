package ru.todoapp.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request that asks for the list of tasks for a specific user from beginDate to endDate
 */
@Getter
@NoArgsConstructor
public class FetchTasksRequestDTO extends RequestDTO {
    /**
     * Date from which we need the list of tasks
     */
    private String beginDate;

    /**
     * Date to which we need the list of tasks
     */
    private String endDate;

    public FetchTasksRequestDTO(String requestUUID, String userUUID, String beginDate, String endDate) {
        super(requestUUID, userUUID, RequestType.FETCH_TASKS);
        this.beginDate = beginDate;
        this.endDate = endDate;
    }
}
