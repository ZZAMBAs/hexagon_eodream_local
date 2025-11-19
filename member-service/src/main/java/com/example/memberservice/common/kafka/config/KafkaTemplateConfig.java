package com.example.memberservice.common.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaTemplateConfig {

    @Value("${kafka.topic.member.create-topic}")
    private String memberCreateTopicName;

    @Value("${kafka.topic.member.update-topic}")
    private String memberUpdateTopicName;


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
    public NewTopic memberCreateTopic() {
        return TopicBuilder.name(memberCreateTopicName)
            .partitions(topicPartitions)
            .replicas(topicReplications)
            .build();
    }

    @Bean
    public NewTopic memberUpdateTopic() {
        return TopicBuilder.name(memberUpdateTopicName)
            .partitions(topicPartitions)
            .replicas(topicReplications)
            .build();
    }
}
