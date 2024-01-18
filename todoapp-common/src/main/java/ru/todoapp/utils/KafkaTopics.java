package ru.todoapp.utils;

public class KafkaTopics {
    /**
     * Topic in Kafka for sending PingRequestDTO
     *
     * @see ru.todoapp.model.dto.PingRequestDTO
     */
    public static final String PING_TOPIC = "todoapp.ping.topic";

    /**
     * Topic in Kafka for sending RequestResultDTO
     *
     * @see ru.todoapp.model.dto.RequestResultDTO
     */
    public static final String REQUEST_RESULT_TOPIC = "todoapp.request.result.topic";

    /**
     * Topic in Kafka for sending UserRequestDTO
     *
     * @see ru.todoapp.model.dto.RegisterRequestDTO
     */
    public static final String REGISTRATION_TOPIC = "todoapp.user.registration.topic";

    /**
     * Topic in Kafka for sending AddTaskRequestDTO
     *
     * @see ru.todoapp.model.dto.AddTaskRequestDTO
     */
    public static final String ADD_TASK_TOPIC = "todoapp.tasks.addition.topic";

    /**
     * Topic in Kafka for sending FetchTasksRequestDTO
     */
    public static final String FETCH_REQUEST_TASKS_TOPIC = "todoapp.tasks.fetch.request.topic";

    /**
     * Topic in Kafka for sending FetchTasksResponseDTO
     */
    public static final String FETCH_RESPONSE_TASKS_TOPIC = "todoapp.tasks.fetch.response.topic";
}
