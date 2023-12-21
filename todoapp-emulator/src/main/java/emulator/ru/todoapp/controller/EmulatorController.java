package emulator.ru.todoapp.controller;

import emulator.ru.todoapp.model.PingRequestEmulatorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.todoapp.model.dto.PingRequestDTO;
import ru.todoapp.utils.KafkaTopics;

import java.util.UUID;

/**
 * Основной контролер для эмулятора
 */
@RestController
@RequiredArgsConstructor
public class EmulatorController {
    private final KafkaTemplate<String, PingRequestDTO> pingRequestKafkaTemplate;

    /**
     * Обработка запроса Ping.
     * Пересылает сообщение в соответствующий топик Kafka
     */
    @PostMapping("/ping")
    public void ping(@RequestBody PingRequestEmulatorDTO requestDTO) {
        var request = new PingRequestDTO(UUID.randomUUID().toString(), requestDTO.userUUID());
        pingRequestKafkaTemplate.send(KafkaTopics.PING_TOPIC, request);
    }
}
