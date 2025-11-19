package com.example.memberservice.common.kafka.producer;

import com.example.memberservice.common.kafka.model.dto.MemberUpdateEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MemberUpdateKafkaEventProducer extends AbstractKafkaEventProducer<MemberUpdateEvent> {

    public MemberUpdateKafkaEventProducer(
        KafkaTemplate<String, MemberUpdateEvent> kafkaTemplate,
        @Value("${kafka.topic.member.update-topic}") String updateTopicName) {
        super(kafkaTemplate, updateTopicName);
    }
}
