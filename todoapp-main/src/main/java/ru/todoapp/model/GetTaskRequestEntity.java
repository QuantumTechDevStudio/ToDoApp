package ru.todoapp.model;

import ru.todoapp.model.dto.FetchTasksRequestDTO;
import ru.todoapp.utils.DateTimeUtils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Entity of parameters required to get list of tasks for specified user in specified range of dates
 *
 * @param userUUID  - tasks for that userUUID will be provided from DB
 * @param beginDate - tasks starting from that date
 * @param endDate   - tasks up to that date
 */
public record GetTaskRequestEntity(String userUUID, OffsetDateTime beginDate, OffsetDateTime endDate) {
    public static GetTaskRequestEntity of(FetchTasksRequestDTO fetchTasksRequestDTO) {
        return new GetTaskRequestEntity(fetchTasksRequestDTO.getUserUUID(),
                DateTimeUtils.setAtTheStartOfTheDay(fetchTasksRequestDTO.getBeginDate()).atOffset(ZoneOffset.UTC),
                DateTimeUtils.setAtTheEndOfTheDay(fetchTasksRequestDTO.getEndDate()).atOffset(ZoneOffset.UTC));
    }
}
