package ru.todoapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import ru.todoapp.model.TaskEntity;
import ru.todoapp.model.dto.AddTaskRequestDTO;
import ru.todoapp.model.dto.FetchTasksRequestDTO;
import ru.todoapp.model.dto.FetchTasksResponseDTO;
import ru.todoapp.model.dto.RequestResultDTO;
import ru.todoapp.model.dto.RequestStatus;
import ru.todoapp.repository.RequestRepository;
import ru.todoapp.repository.TaskRepository;
import ru.todoapp.repository.UserRepository;
import ru.todoapp.utils.KafkaTopics;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    private AddTaskRequestDTO addTaskRequestDTO;

    private RequestResultDTO successfulResult;

    private RequestResultDTO failedResult;

    @Mock
    private KafkaTemplate<String, RequestResultDTO> requestResultDTOKafkaTemplateMock;

    @Mock
    private KafkaTemplate<String, FetchTasksResponseDTO> fetchTasksResponseDTOKafkaTemplateMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private RequestRepository requestRepositoryMock;

    @Mock
    private TaskRepository taskRepositoryMock;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        addTaskRequestDTO = new AddTaskRequestDTO("1", "2", "Do something", "2032-12-26T09:40:23+03:00");
        successfulResult = RequestResultDTO.builder().requestUUID("1").status(RequestStatus.SUCCESS).build();
        taskService = new TaskService(requestResultDTOKafkaTemplateMock,
                fetchTasksResponseDTOKafkaTemplateMock,
                requestRepositoryMock,
                userRepositoryMock,
                taskRepositoryMock);
    }

    /**
     * Test for successful completion of the method
     */
    @Test
    void handleAdditionSuccessTest() {
        // given:
        when(userRepositoryMock.exists(any(String.class))).thenReturn(true);
        // when:
        taskService.handleAddition(addTaskRequestDTO);
        // then:
        verify(requestRepositoryMock, only()).save(any());
        verify(userRepositoryMock, only()).exists(any());
        verify(taskRepositoryMock, only()).saveNewTask(any());
        verify(requestResultDTOKafkaTemplateMock, only()).send(any(), any());
        verify(requestResultDTOKafkaTemplateMock, only()).send(KafkaTopics.REQUEST_RESULT_TOPIC, successfulResult);
    }

    /**
     * Test for failed attempt of adding tasks. Reason: user does not exist.
     */
    @Test
    void handleAdditionFailOnUserTest() {
        // given:
        failedResult = RequestResultDTO.builder().requestUUID("1").status(RequestStatus.FAIL).message("Can't find user!").build();
        when(userRepositoryMock.exists(any(String.class))).thenReturn(false);
        // when:
        taskService.handleAddition(addTaskRequestDTO);
        // then:
        verify(requestRepositoryMock, only()).save(any());
        verify(userRepositoryMock, only()).exists(any());
        verify(taskRepositoryMock, never()).saveNewTask(any());
        verify(requestResultDTOKafkaTemplateMock, only()).send(any(), any());
        verify(requestResultDTOKafkaTemplateMock, only()).send(KafkaTopics.REQUEST_RESULT_TOPIC, failedResult);
    }

    /**
     * Test for failed attempt of adding tasks. Reason: fields not filled
     */
    @Test
    void handleAdditionFailOnEmptyFieldsTest() {
        // given:
        addTaskRequestDTO = new AddTaskRequestDTO(
                "1", "2", "", "2032-12-26T09:40:23+03:00");
        failedResult = RequestResultDTO.builder().requestUUID("1")
                .status(RequestStatus.FAIL).message(addTaskRequestDTO.toString()).build();
        // when:
        taskService.handleAddition(addTaskRequestDTO);
        // then:
        verify(requestRepositoryMock, only()).save(any());
        verify(userRepositoryMock, never()).exists(any());
        verify(taskRepositoryMock, never()).saveNewTask(any());
        verify(requestResultDTOKafkaTemplateMock, only()).send(any(), any());
        verify(requestResultDTOKafkaTemplateMock, only()).send(KafkaTopics.REQUEST_RESULT_TOPIC, failedResult);
    }

    /**
     * Test for failed attempt of adding tasks. Reason: time in past.
     */
    @Test
    void handleAdditionFailOnTimeInPastTest() {
        // given:
        addTaskRequestDTO = new AddTaskRequestDTO(
                "1", "2", "Drinking Blazer", "2007-12-26T09:40:23+03:00");
        failedResult = RequestResultDTO.builder().requestUUID("1").status(RequestStatus.FAIL).message("Can't create task in past!").build();
        when(userRepositoryMock.exists(any(String.class))).thenReturn(true);
        // when:
        taskService.handleAddition(addTaskRequestDTO);
        // then:
        verify(requestRepositoryMock, only()).save(any());
        verify(userRepositoryMock, only()).exists(any());
        verify(taskRepositoryMock, never()).saveNewTask(any());
        verify(requestResultDTOKafkaTemplateMock, only()).send(any(), any());
        verify(requestResultDTOKafkaTemplateMock, only()).send(KafkaTopics.REQUEST_RESULT_TOPIC, failedResult);
    }

    /**
     * Test for successful fetching of tasks
     */
    @Test
    void handleFetchingSuccess() {
        // given:
        FetchTasksRequestDTO fetchTasksRequestDTO = new FetchTasksRequestDTO(
                "1", "2", "2032-01-01", "2032-01-01");
        when(userRepositoryMock.exists(any(String.class))).thenReturn(true);
        OffsetDateTime offsetDateTime = OffsetDateTime.parse("2032-12-26T09:40:23+03:00");
        TaskEntity taskEntity = new TaskEntity(1L, "doing smh", "2", offsetDateTime);
        List<TaskEntity> taskEntities = new ArrayList<>();
        taskEntities.add(taskEntity);
        when(taskRepositoryMock.getTasksList(fetchTasksRequestDTO)).thenReturn(taskEntities);
        // when:
        taskService.handleFetching(fetchTasksRequestDTO);
        // then:
        verify(userRepositoryMock, only()).exists(any());
        verify(taskRepositoryMock, only()).getTasksList(any());
        verify(requestResultDTOKafkaTemplateMock, only()).send(any(), any());
        verify(requestResultDTOKafkaTemplateMock, only()).send(KafkaTopics.REQUEST_RESULT_TOPIC, successfulResult);
        verify(fetchTasksResponseDTOKafkaTemplateMock, times(1)).send(any(), any());
        verify(fetchTasksResponseDTOKafkaTemplateMock, only()).send(eq(KafkaTopics.FETCH_RESPONSE_TASKS_TOPIC), any());
    }

    /**
     * Test for fetching of tasks fails. Reason: user does not exist.
     */
    @Test
    void handleFetchingFailUser() {
        // given:
        FetchTasksRequestDTO fetchTasksRequestDTO = new FetchTasksRequestDTO(
                "1", "2", "2032-01-01", "2032-01-01");
        when(userRepositoryMock.exists(any(String.class))).thenReturn(false);
        failedResult = new RequestResultDTO("1", RequestStatus.FAIL, "Can't find user!");
        // when:
        taskService.handleFetching(fetchTasksRequestDTO);
        // then:
        verify(userRepositoryMock, only()).exists(any());
        verify(taskRepositoryMock, never()).getTasksList(any());
        verify(requestResultDTOKafkaTemplateMock, only()).send(any(), any());
        verify(requestResultDTOKafkaTemplateMock, only()).send(KafkaTopics.REQUEST_RESULT_TOPIC, failedResult);
        verify(fetchTasksResponseDTOKafkaTemplateMock, never()).send(any(), any());
    }

    /**
     * Test for fetching of tasks fails. Reason: begin date is after end date.
     */
    @Test
    void handleFetchingFailBeginDateAfterEndDate() {
        // given:
        FetchTasksRequestDTO fetchTasksRequestDTO = new FetchTasksRequestDTO(
                "1", "2", "2032-01-02", "2032-01-01");
        when(userRepositoryMock.exists(any(String.class))).thenReturn(true);
        failedResult = new RequestResultDTO("1", RequestStatus.FAIL,
                "Incorrect search filter: begin date is after end date!");
        // when:
        taskService.handleFetching(fetchTasksRequestDTO);
        // then:
        verify(userRepositoryMock, only()).exists(any());
        verify(taskRepositoryMock, never()).getTasksList(any());
        verify(requestResultDTOKafkaTemplateMock, only()).send(any(), any());
        verify(requestResultDTOKafkaTemplateMock, only()).send(KafkaTopics.REQUEST_RESULT_TOPIC, failedResult);
        verify(fetchTasksResponseDTOKafkaTemplateMock, never()).send(any(), any());
    }

    /**
     * Test for fetching of tasks fails. Reason: fields not filled
     */
    @Test
    void handleFetchingFailFieldsNotFilled() {
        // given:
        FetchTasksRequestDTO fetchTasksRequestDTO = new FetchTasksRequestDTO(
                "1", "", "2032-01-01", "2032-01-01");
        failedResult = new RequestResultDTO("1", RequestStatus.FAIL,
                fetchTasksRequestDTO.toString());
        // when:
        taskService.handleFetching(fetchTasksRequestDTO);
        // then:
        verify(userRepositoryMock, never()).exists(any());
        verify(taskRepositoryMock, never()).getTasksList(any());
        verify(requestResultDTOKafkaTemplateMock, only()).send(any(), any());
        verify(requestResultDTOKafkaTemplateMock, only()).send(KafkaTopics.REQUEST_RESULT_TOPIC, failedResult);
        verify(fetchTasksResponseDTOKafkaTemplateMock, never()).send(any(), any());
    }
}