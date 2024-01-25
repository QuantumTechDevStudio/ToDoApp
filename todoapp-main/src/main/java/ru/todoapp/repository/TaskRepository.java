package ru.todoapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.todoapp.model.GetTaskRequestEntity;
import ru.todoapp.model.SaveTaskEntity;
import ru.todoapp.model.TaskEntity;

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
    public void saveNewTask(SaveTaskEntity saveTaskEntity) {
        var params = Arrays.asList(
                saveTaskEntity.description(),
                saveTaskEntity.datetime(),
                saveTaskEntity.userUUID());
        jdbcClient.sql(SAVE_NEW_TASK).params(params).update();
    }

    public List<TaskEntity> getTasksList(GetTaskRequestEntity getTaskRequestEntity) {
        var params = Arrays.asList(getTaskRequestEntity.userUUID(),
                getTaskRequestEntity.beginDate(),
                getTaskRequestEntity.endDate());
        return jdbcClient.sql(GET_TASKS_FROM_TO).params(params).query(TaskEntity.class).list();
    }
}