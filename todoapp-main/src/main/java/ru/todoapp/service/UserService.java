package ru.todoapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.todoapp.model.RequestEntity;
import ru.todoapp.model.UserEntity;
import ru.todoapp.model.dto.RequestResultDTO;
import ru.todoapp.model.dto.RequestStatus;
import ru.todoapp.model.dto.UserRequestDTO;
import ru.todoapp.repository.RequestRepository;
import ru.todoapp.repository.UserRepository;
import ru.todoapp.utils.KafkaTopics;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final KafkaTemplate<String, RequestResultDTO> requestResultDTOKafkaTemplate;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    /**
     * Обработка запроса на добавление нового пользователя
     */
    public void handle(UserRequestDTO userRequestDTO) {
        requestRepository.save(RequestEntity.of(userRequestDTO));

        Optional<List<String>> unfilled = getAllUnfilledFields(userRequestDTO);
        if (unfilled.isPresent()) {
            var result = RequestResultDTO.builder()
                    .requestUUID(userRequestDTO.getRequestUUID())
                    .status(RequestStatus.FAIL)
                    .message("Не заполнены обязательные поля: " + String.join(", ",unfilled.get()) + "! ")
                    .build();
            requestResultDTOKafkaTemplate.send(KafkaTopics.REQUEST_RESULT_TOPIC, result);
        } else if (userRepository.check(userRequestDTO.getUserUUID())) {
            var result = RequestResultDTO.builder()
                    .requestUUID(userRequestDTO.getRequestUUID())
                    .status(RequestStatus.FAIL)
                    .message("Пользователь уже был зарегестрирован!")
                    .build();
            requestResultDTOKafkaTemplate.send(KafkaTopics.REQUEST_RESULT_TOPIC, result);
        } else {
            userRepository.saveUser(UserEntity.of(userRequestDTO));
            var result = RequestResultDTO.builder()
                    .requestUUID(userRequestDTO.getRequestUUID())
                    .status(RequestStatus.SUCCESS)
                    .build();
            requestResultDTOKafkaTemplate.send(KafkaTopics.REQUEST_RESULT_TOPIC, result);
        }
    }

    /**
     * Создает список всех незаполненных параметров или возвращает пустое значение если все поля заполенены
     * @param userRequestDTO - проверяем заполненность полей у пользователя
     */
    private Optional<List<String>> getAllUnfilledFields(UserRequestDTO userRequestDTO) {
        List<String> unfilled = new ArrayList<>();
        if (StringUtils.isEmpty(userRequestDTO.getUserUUID()) {
            unfilled.add("userUUID");
        }
        if (userRequestDTO.getName() == null || userRequestDTO.getName().isEmpty() || userRequestDTO.getName().equals("")) {
            unfilled.add("name");
        }
        if (userRequestDTO.getSurname() == null || userRequestDTO.getSurname().isEmpty() || userRequestDTO.getSurname().equals("")) {
            unfilled.add("surname");
        }

        if (unfilled.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(unfilled);
        }
    }
}
