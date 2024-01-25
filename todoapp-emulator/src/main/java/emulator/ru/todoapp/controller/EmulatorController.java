package emulator.ru.todoapp.controller;

import emulator.ru.todoapp.model.AddTaskRequestEmulatorDTO;
import emulator.ru.todoapp.model.FetchTasksRequestEmulatorDTO;
import emulator.ru.todoapp.model.PingRequestEmulatorDTO;
import emulator.ru.todoapp.model.RegisterRequestEmulatorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.todoapp.model.dto.AddTaskRequestDTO;
import ru.todoapp.model.dto.FetchTasksRequestDTO;
import ru.todoapp.model.dto.PingRequestDTO;
import ru.todoapp.model.dto.RegisterRequestDTO;
import ru.todoapp.utils.KafkaTopics;

import java.util.UUID;

/**
 * Primary emulator controller
 */
@RestController
@RequiredArgsConstructor
public class EmulatorController {
    private final KafkaTemplate<String, PingRequestDTO> pingRequestKafkaTemplate;

    private final KafkaTemplate<String, RegisterRequestDTO> registrationRequestKafkaTemplate;

    private final KafkaTemplate<String, AddTaskRequestDTO> addTaskRequestKafkaTemplate;

    private final KafkaTemplate<String, FetchTasksRequestDTO> fetchTasksRequestDTOKafkaTemplate;

    /**
     * Ping request processing.
     * Reposts message is corresponding Kafka topic
     */
    @PostMapping("/ping")
    public void ping(@RequestBody PingRequestEmulatorDTO requestDTO) {
        var request = new PingRequestDTO(UUID.randomUUID().toString(), requestDTO.userUUID());
        pingRequestKafkaTemplate.send(KafkaTopics.PING_TOPIC, request);
    }

    /**
     * Registration request processing.
     * Sends request for user registration, should receive response from Kafka with success/fail result of registration
     * containing corresponding message
     * @param registerRequestEmulatorDTO - parameter that emulates user registration
     */
    @PostMapping("/register")
    public void register(@RequestBody RegisterRequestEmulatorDTO registerRequestEmulatorDTO) {
        RegisterRequestDTO request;
        if (registerRequestEmulatorDTO.userUUID() == null) {
            request = new RegisterRequestDTO(UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    registerRequestEmulatorDTO.name(),
                    registerRequestEmulatorDTO.surname());
        } else {
            request = new RegisterRequestDTO(UUID.randomUUID().toString(),
                    registerRequestEmulatorDTO.userUUID(),
                    registerRequestEmulatorDTO.name(),
                    registerRequestEmulatorDTO.surname());
        }
        registrationRequestKafkaTemplate.send(KafkaTopics.REGISTRATION_TOPIC, request);
    }

    /**
     * Addition of new task request processing.
     * Sends request for adding new task for user, receives response from Kafka with success/fail result of task
     * addition containing corresponding message
     * @param addTaskRequestEmulatorDTO - parameter that emulates the action of user adding task for himself
     */
    @PostMapping("/add_new_task")
    public void addNewTask(@RequestBody AddTaskRequestEmulatorDTO addTaskRequestEmulatorDTO) {
        AddTaskRequestDTO addTaskRequestDTO = new AddTaskRequestDTO(UUID.randomUUID().toString(),
                addTaskRequestEmulatorDTO.userUUID(),
                addTaskRequestEmulatorDTO.description(),
                addTaskRequestEmulatorDTO.zonedDateTime());
        addTaskRequestKafkaTemplate.send(KafkaTopics.ADD_TASK_TOPIC, addTaskRequestDTO);
    }

    @PostMapping("/fetch_tasks_list")
    public void fetchTasks(@RequestBody FetchTasksRequestEmulatorDTO fetchTasksRequestEmulatorDTO) {
        FetchTasksRequestDTO fetchTasksRequestDTO = new FetchTasksRequestDTO(UUID.randomUUID().toString(),
                fetchTasksRequestEmulatorDTO.userUUID(),
                fetchTasksRequestEmulatorDTO.beginDate(),
                fetchTasksRequestEmulatorDTO.endDate());
        fetchTasksRequestDTOKafkaTemplate.send(KafkaTopics.FETCH_REQUEST_TASKS_TOPIC, fetchTasksRequestDTO);
    }
}
