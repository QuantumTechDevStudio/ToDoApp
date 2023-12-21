package emulator.ru.todoapp.configuration;

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
import ru.todoapp.utils.KafkaConstants;
import ru.todoapp.utils.KafkaTopics;
import ru.todoapp.utils.KafkaUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация для Kafka
 */
@Configuration
public class KafkaConfig {
    public static final String GROUP_ID = "todoapp-emulator";


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
     * @see PingRequestDTO
     */
    @Bean
    public NewTopic pingTopic() {
        return new NewTopic(KafkaTopics.PING_TOPIC, KafkaConstants.DEFAULT_NUMBER_OF_PARTITIONS, KafkaConstants.DEFAULT_REPLICATION_FACTOR);
    }

    /**
     * Создание топика в Kafka для отправки результата запроса
     *
     * @see ru.todoapp.model.dto.RequestResultDTO
     */
    @Bean
    public NewTopic requestResultTopic() {
        return new NewTopic(KafkaTopics.REQUEST_RESULT_TOPIC, KafkaConstants.DEFAULT_NUMBER_OF_PARTITIONS, KafkaConstants.DEFAULT_REPLICATION_FACTOR);
    }

    /**
     * Бин фабрики продюсеров для отправки PingRequest
     */
    @Bean
    public ProducerFactory<String, PingRequestDTO> requestResultDTOProducerFactory() {
        return KafkaUtils.getKafkaProducerFactory(kafkaUrl);
    }

    /**
     * Бин KafkaTemplate для отправки PingRequest
     */
    @Bean
    public KafkaTemplate<String, PingRequestDTO> requestResultDTOKafkaTemplate() {
        return new KafkaTemplate<>(requestResultDTOProducerFactory());
    }

    /**
     * Фабрика консьюмеров для получения RequestResultDTO
     */
    @Bean
    public ConsumerFactory<String, RequestResultDTO> requstResultConsumerFactory() {
        return KafkaUtils.getKafkaConsumerFactory(RequestResultDTO.class, GROUP_ID, kafkaUrl);
    }

    /**
     * Фабрика listener для получения RequestResultDTO
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RequestResultDTO> requestResultContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, RequestResultDTO>();
        factory.setConsumerFactory(requstResultConsumerFactory());

        return factory;
    }
}
