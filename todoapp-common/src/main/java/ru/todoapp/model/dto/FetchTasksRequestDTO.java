package ru.todoapp.model.dto;

import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Request that asks for the list of tasks for a specific user from beginDate to endDate
 */
@Getter
@NoArgsConstructor
public class FetchTasksRequestDTO extends RequestDTO {
    /**
     * Date from which we need the list of tasks
     */
    private Instant beginDate;

    /**
     * Date to which we need the list of tasks
     */
    private Instant endDate;

    public FetchTasksRequestDTO(String requestUUID, String userUUID, String beginDate, String endDate) {
        super(requestUUID, userUUID, RequestType.FETCH_TASKS);
        this.beginDate = LocalDate.parse(beginDate).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        this.endDate = LocalDate.parse(endDate).atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
    }

    /**
     * checks for existence of any unfilled fields
     */
    public boolean isAnyUnfilledFields() {
        return StringUtils.isEmpty(userUUID) || beginDate == null || endDate == null;
    }

    @Override
    public String toString() {
        return "FetchTasksRequestDTO{" +
                "userUUID=" + userUUID +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                '}';
    }
}
