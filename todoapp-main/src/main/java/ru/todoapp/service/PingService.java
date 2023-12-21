package ru.todoapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.todoapp.model.RequestEntity;
import ru.todoapp.model.dto.PingRequestDTO;
import ru.todoapp.model.dto.RequestResultDTO;
import ru.todoapp.model.dto.RequestStatus;
import ru.todoapp.repository.RequestRepository;
import ru.todoapp.utils.KafkaTopics;

import java.time.LocalDateTime;

/**
 * Сервис для обработки Ping запросов
 */
@Service
@RequiredArgsConstructor
public class PingService {
    private final KafkaTemplate<String, RequestResultDTO> requestResultDTOKafkaTemplate;
    private final RequestRepository requestRepository;

    /**
     * Обработчик запроса PingRequestDTO
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
