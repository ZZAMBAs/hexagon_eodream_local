package com.example.contractservice.common.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaTemplateConfig {

    @Value("${kafka.producer.topic.contract.name}")
    private String contractTopicName;

    @Value("${kafka.config.topic-partitions}")
    private int topicPartitions;

    @Value("${kafka.config.topic-replications}")
    private int topicReplications;


    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(
        ProducerFactory<String, Object> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public NewTopic contractTopic() {
        return TopicBuilder.name(contractTopicName)
                .partitions(topicPartitions)
                .replicas(topicReplications)
                .build();
    }
}
