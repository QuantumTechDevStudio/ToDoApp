package quantenpfad.todoapp.model.dto;

import lombok.Builder;

@Builder
public record RequestResultDTO(String requestUUID, RequestStatus status, String message) {
}
