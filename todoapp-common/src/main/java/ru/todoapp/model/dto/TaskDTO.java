package ru.todoapp.model.dto;

import lombok.Builder;

@Builder
public record TaskDTO(long id, String description, String datetime) {
}
