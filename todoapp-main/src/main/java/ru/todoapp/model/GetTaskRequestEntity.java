package ru.todoapp.model;

import ru.todoapp.model.dto.FetchTasksRequestDTO;

/**
 * Entity of parameters required to get list of tasks for specified user in specified range of dates
 *
 * @param userUUID  - tasks for that userUUID will be provided from DB
 * @param beginDate - tasks starting from that date
 * @param endDate   - tasks up to that date
 */
public record GetTaskRequestEntity(String userUUID, String beginDate, String endDate) {
    public static GetTaskRequestEntity of(FetchTasksRequestDTO fetchTasksRequestDTO) {
        return new GetTaskRequestEntity(fetchTasksRequestDTO.getUserUUID(), fetchTasksRequestDTO.getBeginDate(), fetchTasksRequestDTO.getEndDate());
    }
}
