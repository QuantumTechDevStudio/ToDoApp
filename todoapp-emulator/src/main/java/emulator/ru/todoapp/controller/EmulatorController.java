package emulator.ru.todoapp.controller;

import emulator.ru.todoapp.model.PingRequestEmulatorDTO;
import emulator.ru.todoapp.model.RegisterRequestEmulatorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.todoapp.model.dto.PingRequestDTO;
import ru.todoapp.model.dto.RegistrationRequestDTO;
import ru.todoapp.utils.KafkaTopics;

import java.util.UUID;

/**
 * Основной контролер для эмулятора
 */
@RestController
@RequiredArgsConstructor
public class EmulatorController {
    private final KafkaTemplate<String, PingRequestDTO> pingRequestKafkaTemplate;

    private final KafkaTemplate<String, RegistrationRequestDTO> registrationRequestKafkaTemplate;

    /**
     * Обработка запроса Ping.
     * Пересылает сообщение в соответствующий топик Kafka
     */
    @PostMapping("/ping")
    public void ping(@RequestBody PingRequestEmulatorDTO requestDTO) {
        var request = new PingRequestDTO(UUID.randomUUID().toString(), requestDTO.userUUID());
        pingRequestKafkaTemplate.send(KafkaTopics.PING_TOPIC, request);
    }

    /**
     * Обработка запроса Register.
     * Отправляет запрос на регистрацию пользователя, должен получать ответ от Kafka об успехе/провале регистрации
     * с соответсвующим сообщением
     * @param registerRequestEmulatorDTO - параметр эмулирующий регистрацию пользователя
     */
    @PostMapping("/register")
    public void register(@RequestBody RegisterRequestEmulatorDTO registerRequestEmulatorDTO) {
        var request = new RegistrationRequestDTO(UUID.randomUUID().toString(),
                registerRequestEmulatorDTO.userUUID(),
                registerRequestEmulatorDTO.name(),
                registerRequestEmulatorDTO.surname());
        registrationRequestKafkaTemplate.send(KafkaTopics.REQUEST_RESULT_TOPIC, request);
    }
}
