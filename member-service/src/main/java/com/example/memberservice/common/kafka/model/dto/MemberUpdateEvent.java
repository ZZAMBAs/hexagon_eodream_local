package com.example.memberservice.common.kafka.model.dto;

import com.example.memberservice.common.kafka.event.KafkaEvent;


public record MemberUpdateEvent(String memberCode, String nickName) implements KafkaEvent<MemberUpdateEvent> {

    @Override
    public MemberUpdateEvent toEventData() {
        return this;
    }

    @Override
    public String getEventKey() {
        return memberCode;
    }
}
