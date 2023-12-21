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
 * Класс с утилитками, которые упрощают работу с Kafka
 */
public class KafkaUtils {
    /**
     * Метод, создающий фабрику консьюмеров для заданного типа.
     * Для десериалзации значений используется Jackson.
     *
     * @param messageClass класс, для которого нужны консьюмеры
     * @param groupId      имя группы консьюмеров
     * @param kafkaUrl     адрес кафки
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
     * Метод, создающий фабрику продюсеров для заданного типа.
     * Сериализация происходит в формат JSON при помощи Jackson
     *
     * @param kafkaUrl адрес кафки
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
