package quantenpfad.todoapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import quantenpfad.todoapp.model.RequestEntity;
import quantenpfad.todoapp.model.dto.PingRequestDTO;
import quantenpfad.todoapp.model.dto.RequestResultDTO;
import quantenpfad.todoapp.model.dto.RequestStatus;
import quantenpfad.todoapp.repository.RequestRepository;
import quantenpfad.todoapp.utils.KafkaTopics;

import java.time.LocalDateTime;

/**
 * Service for Ping request
 */
@Service
@RequiredArgsConstructor
public class PingService {
    private final KafkaTemplate<String, RequestResultDTO> requestResultDTOKafkaTemplate;
    private final RequestRepository requestRepository;

    /**
     * PingRequestDTO request handler
     */
    public void handle(PingRequestDTO request) {
        requestRepository.save(RequestEntity.of(request));
        var result = RequestResultDTO.builder()
                .requestUUID(request.getRequestUUID())
                .status(RequestStatus.SUCCESS)
                .message("Received at " + LocalDateTime.now())
                .build();
        requestResultDTOKafkaTemplate.send(KafkaTopics.REQUEST_RESULT_TOPIC, result);
    }
}
