package ru.todoapp.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.todoapp.configuration.KafkaConfig;
import ru.todoapp.model.dto.RegisterRequestDTO;
import ru.todoapp.service.UserService;
import ru.todoapp.utils.KafkaTopics;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaUserListener {
    private final UserService userService;

    /**
     * Обработчик сообщений UserRequestDTO, получаемых из Kafka
     *
     * @see RegisterRequestDTO
     */
    @KafkaListener(topics = KafkaTopics.REGISTRATION_TOPIC,
            groupId = KafkaConfig.GROUP_ID,
            containerFactory = "userRegistrationRequestContainerFactory")
    public void handleUserRegistrationMessage(RegisterRequestDTO registerRequestDTO) {
        log.info("Received message with type \"{}\": {}", registerRequestDTO.getType(), registerRequestDTO);
        if (registerRequestDTO.getType() != null) {
            userService.handle(registerRequestDTO);
        }
    }
}
