package com.banque.comptes.config;

import com.banque.events.dto.AccountDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.reply.topic}")
    private String replyTopic;

    @Value("${spring.kafka.request.topic}")
    private String requestTopic;
    @Value("${spring.kafka.topic.account-update-topic}")
    private String accountUpdateTopic;

    // Producer Factory
    @Bean
    public ProducerFactory<String, AccountDto> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    // Kafka Template for sending messages
    @Bean
    public KafkaTemplate<String, AccountDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


    // Consumer Factory
    @Bean
    public ConsumerFactory<String, AccountDto> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), new JsonDeserializer<>(AccountDto.class));
    }

    // Kafka Listener Container Factory
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AccountDto> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AccountDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }


    @Bean
    public ReplyingKafkaTemplate<String, AccountDto, AccountDto> replyingKafkaTemplate(
            ProducerFactory<String, AccountDto> producerFactory,
            ConcurrentKafkaListenerContainerFactory<String, AccountDto> factory) {

        ConcurrentMessageListenerContainer<String, AccountDto> replyContainer =
                factory.createContainer(requestTopic);
        replyContainer.getContainerProperties().setGroupId(groupId+"-reply");
        replyContainer.getContainerProperties().setMissingTopicsFatal(false);


        return new ReplyingKafkaTemplate<>(producerFactory, replyContainer);
    }

}