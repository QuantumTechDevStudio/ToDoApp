package quantenpfad.todoapp.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import quantenpfad.todoapp.configuration.KafkaConfig;
import quantenpfad.todoapp.model.dto.RegisterRequestDTO;
import quantenpfad.todoapp.service.UserService;
import quantenpfad.todoapp.utils.KafkaTopics;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaUserListener {
    private final UserService userService;

    /**
     * UserRequestDTO massage received from Kafka
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
