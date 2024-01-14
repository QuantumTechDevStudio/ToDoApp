package quantenpfad.todoapp.utils;

import quantenpfad.todoapp.model.dto.PingRequestDTO;
import quantenpfad.todoapp.model.dto.RegisterRequestDTO;
import quantenpfad.todoapp.model.dto.RequestResultDTO;

public class KafkaTopics {
    /**
     * Topic in Kafka for sending PingRequestDTO
     *
     * @see PingRequestDTO
     */
    public static final String PING_TOPIC = "todoapp.ping.topic";

    /**
     * Topic in Kafka for sending RequestResultDTO
     *
     * @see RequestResultDTO
     */
    public static final String REQUEST_RESULT_TOPIC = "todoapp.request.result.topic";

    /**
     * Topic in Kafka for sending UserRequestDTO
     *
     * @see RegisterRequestDTO
     */
    public static final String REGISTRATION_TOPIC = "todoapp.user.registration.topic";
}
