package ru.todoapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.todoapp.model.TaskEntity;
import ru.todoapp.model.dto.FetchTasksRequestDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

/**
 * Repository for any task related actions (addition and fetching for now)
 */
@Repository
@RequiredArgsConstructor
public class TaskRepository {
    public static final String SAVE_NEW_TASK = "INSERT INTO tda_task (description, datetime, user_uuid) VALUES (?,?,?)";

    public static final String GET_TASKS_FROM_TO = "SELECT id, description, datetime FROM tda_task WHERE user_uuid = ? AND datetime BETWEEN ? AND ?";

    private final JdbcClient jdbcClient;

    /**
     * Saves new task to DataBase
     */
    public void saveNewTask(TaskEntity taskEntity) {
        var params = Arrays.asList(
                taskEntity.description(),
                taskEntity.datetime(),
                taskEntity.userUUID());
        jdbcClient.sql(SAVE_NEW_TASK).params(params).update();
    }

    /**
     * Fetches list of tasks that satisfies entry parameters
     *
     * @param fetchTaskRequestDTO - object containing parameters for fetching
     */
    public List<TaskEntity> getTasksList(FetchTasksRequestDTO fetchTaskRequestDTO) {
        var params = Arrays.asList(fetchTaskRequestDTO.getUserUUID(),
                fetchTaskRequestDTO.getBeginDate().atOffset(ZoneOffset.UTC),
                fetchTaskRequestDTO.getEndDate().atOffset(ZoneOffset.UTC));
        return jdbcClient.sql(GET_TASKS_FROM_TO).params(params).query(new TasksRowMapper()).list();
    }

    /**
     * Mapper for Task entities
     */
    private static class TasksRowMapper implements RowMapper<TaskEntity> {

        @Override
        public TaskEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TaskEntity(rs.getLong("id"),
                    rs.getString("description"),
                    null,
                    rs.getTimestamp("datetime").toInstant().atOffset(ZoneOffset.UTC));
        }
    }
}