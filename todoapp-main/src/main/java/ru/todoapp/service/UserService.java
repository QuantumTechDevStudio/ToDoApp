package ru.todoapp.service;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.todoapp.model.RequestEntity;
import ru.todoapp.model.UserEntity;
import ru.todoapp.model.dto.RequestResultDTO;
import ru.todoapp.model.dto.RequestStatus;
import ru.todoapp.model.dto.RegisterRequestDTO;
import ru.todoapp.repository.RequestRepository;
import ru.todoapp.repository.UserRepository;
import ru.todoapp.utils.KafkaTopics;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final KafkaTemplate<String, RequestResultDTO> requestResultDTOKafkaTemplate;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    /**
     * Обработка запроса на добавление нового пользователя
     */
    public void handle(RegisterRequestDTO registerRequestDTO) {
        requestRepository.save(RequestEntity.of(registerRequestDTO));

        List<String> unfilled = getAllUnfilledFields(registerRequestDTO);
        if (!unfilled.isEmpty()) {
            sendRequestResultDTO(registerRequestDTO.getRequestUUID(), RequestStatus.FAIL,
                    "Не заполнены обязательные поля: " + String.join(", ", unfilled) + "! ");
        } else if (userRepository.exists(registerRequestDTO.getUserUUID())) {
            sendRequestResultDTO(registerRequestDTO.getRequestUUID(), RequestStatus.FAIL,
                    "Пользователь уже был зарегестрирован!");
        } else {
            userRepository.saveUser(UserEntity.of(registerRequestDTO));
            sendRequestResultDTO(registerRequestDTO.getRequestUUID(), RequestStatus.SUCCESS, null);
        }
    }

    /**
     * Создает список всех незаполненных параметров или возвращает пустое значение если все поля заполенены
     * @param registerRequestDTO - проверяем заполненность полей у пользователя
     */
    private List<String> getAllUnfilledFields(RegisterRequestDTO registerRequestDTO) {
        List<String> unfilled = new ArrayList<>();
        if (StringUtils.isEmpty(registerRequestDTO.getUserUUID())) {
            unfilled.add("userUUID");
        }
        if (StringUtils.isEmpty(registerRequestDTO.getName())) {
            unfilled.add("name");
        }
        if (StringUtils.isEmpty(registerRequestDTO.getSurname())) {
            unfilled.add("surname");
        }

        return unfilled;
    }

    /**
     * Метод отправляющий соответствующий ответ в Кафку
     */
    private void sendRequestResultDTO(String requestUUID, RequestStatus status, @Nullable String message) {
        var result = RequestResultDTO.builder()
                .requestUUID(requestUUID)
                .status(status)
                .message(message)
                .build();
        requestResultDTOKafkaTemplate.send(KafkaTopics.REQUEST_RESULT_TOPIC, result);
    }
}
