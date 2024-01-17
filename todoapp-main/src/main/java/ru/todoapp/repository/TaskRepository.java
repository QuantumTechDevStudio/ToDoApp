package ru.todoapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.todoapp.model.SaveTaskEntity;

import java.sql.Timestamp;
import java.util.Arrays;

/**
 * Repository for any task related actions (addition and fetching for now)
 */
@Repository
@RequiredArgsConstructor
public class TaskRepository {
    public static final String SAVE_NEW_TASK = "INSERT INTO tda_task (description, datetime, user_uuid) VALUES (?,?,?)";

    private final JdbcClient jdbcClient;

    public void saveNewTask(SaveTaskEntity saveTaskEntity) {
        //TODO: fix datetime to GMT+0
        var taskTimestamp = new Timestamp(saveTaskEntity.datetime().toEpochMilli());
        var params = Arrays.asList(
                saveTaskEntity.description(),
                taskTimestamp,
                saveTaskEntity.userUUID());
        jdbcClient.sql(SAVE_NEW_TASK).params(params).update();
    }
}