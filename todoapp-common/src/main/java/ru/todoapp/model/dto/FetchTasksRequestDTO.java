package ru.todoapp.model.dto;

import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

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
     * creates a list of all unfilled fields
     */
    public List<String> getAllUnfilledFields() {
        List<String> unfilled = new ArrayList<>();
        if (StringUtils.isEmpty(getUserUUID())) {
            unfilled.add("userUUID");
        }
        if (beginDate == null) {
            unfilled.add("beginDate");
        }
        if (endDate == null) {
            unfilled.add("endDate");
        }
        return unfilled;
    }
}
