package com.example.memberservice.common.kafka.model.dto;


import com.example.memberservice.common.kafka.event.KafkaEvent;


public record MemberCreateEvent(String memberCode) implements KafkaEvent<MemberCreateEvent> {

    @Override
    public MemberCreateEvent toEventData() {
        return this;
    }

    @Override
    public String getEventKey() {
        return memberCode;
    }
}