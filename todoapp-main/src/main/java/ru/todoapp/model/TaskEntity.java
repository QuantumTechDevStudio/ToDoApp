package ru.todoapp.model;

import ru.todoapp.model.dto.TaskDTO;

import java.time.OffsetDateTime;

public record TaskEntity(long id, String description, OffsetDateTime datetime) {
    public TaskDTO toTaskDTO() {
        return new TaskDTO(id, description, datetime.toInstant().toString());
    }
}
