package com.example.memberservice.common.kafka.producer;

import com.example.memberservice.common.kafka.model.dto.MemberCreateEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MemberCreateKafkaEventProducer extends AbstractKafkaEventProducer<MemberCreateEvent> {

    public MemberCreateKafkaEventProducer(
        KafkaTemplate<String, MemberCreateEvent> kafkaTemplate,
        @Value("${kafka.topic.member.create-topic}") String createTopicName) {
        super(kafkaTemplate, createTopicName);
    }
}
