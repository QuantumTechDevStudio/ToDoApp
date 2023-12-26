package ru.todoapp.configuration;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.todoapp.model.dto.PingRequestDTO;
import ru.todoapp.model.dto.RequestResultDTO;
import ru.todoapp.model.dto.UserRequestDTO;
import ru.todoapp.utils.KafkaTopics;
import ru.todoapp.utils.KafkaUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация для Kafka
 */
@Configuration
public class KafkaConfig {
    public static final String GROUP_ID = "todoapp-main";


    /**
     * URL для Kafka
     */
    @Value("${todoapp.kafka.url}")
    private String kafkaUrl;

    /**
     * Бин с настройками кафки
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> conf = new HashMap<>();
        conf.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);

        return new KafkaAdmin(conf);
    }

    /**
     * Создание топика в Kafka для PingRequest
     *
     * @see ru.todoapp.model.dto.PingRequestDTO
     */
    @Bean
    public NewTopic pingTopic() {
        return new NewTopic(KafkaTopics.PING_TOPIC, 1, (short) 1);
    }

    /**
     * Создание топика в Kafka для отправки результата запроса
     *
     * @see ru.todoapp.model.dto.RequestResultDTO
     */
    @Bean
    public NewTopic requestResultTopic() {
        return new NewTopic(KafkaTopics.REQUEST_RESULT_TOPIC, 1, (short) 1);
    }

    /**
     * Создание топика в Kafka для отправки результата запроса
     *
     * @see UserRequestDTO
     */
    @Bean
    public NewTopic userRegistrationTopic() {
        return new NewTopic(KafkaTopics.REGISTRATION_TOPIC, 1, (short) 1);
    }

    /**
     * Фабрика консьюмеров для получения PingRequestDTO
     */
    @Bean
    public ConsumerFactory<String, PingRequestDTO> pingRequestConsumerFactory() {
        return KafkaUtils.getKafkaConsumerFactory(PingRequestDTO.class, GROUP_ID, kafkaUrl);
    }

    /**
     * Фабрика listener для получения PingRequestDTO
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PingRequestDTO> pingRequestContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, PingRequestDTO>();
        factory.setConsumerFactory(pingRequestConsumerFactory());

        return factory;
    }

    /**
     * Бин фабрики продюсеров для отправки RequestResultDTO
     */
    @Bean
    public ProducerFactory<String, RequestResultDTO> requestResultDTOProducerFactory() {
        return KafkaUtils.getKafkaProducerFactory(kafkaUrl);
    }

    /**
     * Бин KafkaTemplate для отправки RequestResultDTO
     */
    @Bean
    public KafkaTemplate<String, RequestResultDTO> requestResultDTOKafkaTemplate() {
        return new KafkaTemplate<>(requestResultDTOProducerFactory());
    }

    @Bean
    public ConsumerFactory<String, UserRequestDTO> userRegistrationConsumerFactory() {
        return KafkaUtils.getKafkaConsumerFactory(UserRequestDTO.class, GROUP_ID, kafkaUrl);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserRequestDTO> userRegistrationRequestContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, UserRequestDTO>();

        factory.setConsumerFactory(userRegistrationConsumerFactory());
        return factory;
    }
}
