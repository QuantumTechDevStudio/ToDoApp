package quantenpfad.todoapp.configuration;

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
import quantenpfad.todoapp.model.dto.PingRequestDTO;
import quantenpfad.todoapp.model.dto.RegisterRequestDTO;
import quantenpfad.todoapp.model.dto.RequestResultDTO;
import quantenpfad.todoapp.utils.KafkaConstants;
import quantenpfad.todoapp.utils.KafkaTopics;
import quantenpfad.todoapp.utils.KafkaUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka configuration
 */
@Configuration
public class KafkaConfig {
    public static final String GROUP_ID = "todoapp-emulator";


    /**
     * Kafka URL
     */
    @Value("${todoapp.kafka.url}")
    private String kafkaUrl;

    /**
     * Kafka settings bean
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> conf = new HashMap<>();
        conf.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);

        return new KafkaAdmin(conf);
    }

    /**
     * Kafka topic creation for PingRequest
     *
     * @see PingRequestDTO
     */
    @Bean
    public NewTopic pingTopic() {
        return new NewTopic(KafkaTopics.PING_TOPIC, KafkaConstants.DEFAULT_NUMBER_OF_PARTITIONS, KafkaConstants.DEFAULT_REPLICATION_FACTOR);
    }

    /**
     * Kafka topic creation for sending request result
     *
     * @see RequestResultDTO
     */
    @Bean
    public NewTopic requestResultTopic() {
        return new NewTopic(KafkaTopics.REQUEST_RESULT_TOPIC, KafkaConstants.DEFAULT_NUMBER_OF_PARTITIONS, KafkaConstants.DEFAULT_REPLICATION_FACTOR);
    }

    /**
     * Kafka topic creation for registration
     *
     * @see RegisterRequestDTO
     */
    @Bean
    public NewTopic userRegistrationTopic() {
        return new NewTopic(KafkaTopics.REGISTRATION_TOPIC, KafkaConstants.DEFAULT_NUMBER_OF_PARTITIONS, KafkaConstants.DEFAULT_REPLICATION_FACTOR);
    }

    /**
     * Producer factory bean for sending PingRequest
     */
    @Bean
    public ProducerFactory<String, PingRequestDTO> requestResultDTOProducerFactory() {
        return KafkaUtils.getKafkaProducerFactory(kafkaUrl);
    }

    /**
     * KafkaTemplate bean for sending PingRequest
     */
    @Bean
    public KafkaTemplate<String, PingRequestDTO> requestResultDTOKafkaTemplate() {
        return new KafkaTemplate<>(requestResultDTOProducerFactory());
    }

    /**
     * Consumer factory for receiving RequestResultDTO
     */
    @Bean
    public ConsumerFactory<String, RequestResultDTO> requstResultConsumerFactory() {
        return KafkaUtils.getKafkaConsumerFactory(RequestResultDTO.class, GROUP_ID, kafkaUrl);
    }

    /**
     * Listener factory for receiving RequestResultDTO
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RequestResultDTO> requestResultContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, RequestResultDTO>();
        factory.setConsumerFactory(requstResultConsumerFactory());

        return factory;
    }

    /**
     * Producer factory bean for sending RegisterRequestDTO
     */
    @Bean
    public ProducerFactory<String, RegisterRequestDTO> requestUserRequestProducerFactory() {
        return KafkaUtils.getKafkaProducerFactory(kafkaUrl);
    }

    /**
     * KafkaTemplate bean for sending RegisterRequestDTO
     */
    @Bean
    public KafkaTemplate<String, RegisterRequestDTO> requestUserResultDTOKafkaTemplate() {
        return new KafkaTemplate<>(requestUserRequestProducerFactory());
    }
}
