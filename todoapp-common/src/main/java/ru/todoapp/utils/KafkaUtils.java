package ru.todoapp.utils;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Class with utilities, that simplify work with Kafka
 */
public class KafkaUtils {
    /**
     * Method that creates consumer factory of requested type.
     * For value deserialization Jackson is used.
     *
     * @param messageClass class that requires consumers
     * @param groupId      name of the consumer group
     * @param kafkaUrl     Kafka address
     */
    public static <T> ConsumerFactory<String, T> getKafkaConsumerFactory(Class<T> messageClass, String groupId, String kafkaUrl) {
        var deserializer = new JsonDeserializer<>(messageClass);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");

        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafkaUrl);
        configProps.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                groupId
        );

        configProps.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), deserializer);
    }

    /**
     * Method that creates producer factory of requested type.
     * For value deserialization Jackson is used.
     *
     * @param kafkaUrl Kafka address
     */
    public static <T> ProducerFactory<String, T> getKafkaProducerFactory(String kafkaUrl) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafkaUrl);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
}
