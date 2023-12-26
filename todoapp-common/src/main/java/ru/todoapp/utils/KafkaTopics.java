package ru.todoapp.utils;

import ru.todoapp.model.dto.RegisterRequestDTO;

public class KafkaTopics {
    /**
     * Топик в кафке для отправки PingRequestDTO
     *
     * @see ru.todoapp.model.dto.PingRequestDTO
     */
    public static final String PING_TOPIC = "todoapp.ping.topic";

    /**
     * Топик в кафке для отправки RequestResultDTO
     *
     * @see ru.todoapp.model.dto.RequestResultDTO
     */
    public static final String REQUEST_RESULT_TOPIC = "todoapp.request.result.topic";

    /**
     * Топик в кафке для отправки UserRequestDTO
     *
     * @see RegisterRequestDTO
     */
    public static final String REGISTRATION_TOPIC = "todoapp.user.registration.topic";
}
