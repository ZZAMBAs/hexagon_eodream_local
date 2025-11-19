package com.example.profileservice.common.model.vo.util;

import com.example.profileservice.rating.model.dto.request.ContractEvent;
import com.example.profileservice.rating.model.dto.request.RatingRequest;
import com.example.profileservice.rating.service.RatingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class ContractEventConsumer {

    // 주입받을 서비스 (실제 평가 로직 호출 시 사용)
    private final RatingService ratingService;
    private final ObjectMapper objectMapper;

    private static final String CONTRACT_TOPIC = "${kafka.topic.contract.name}";
    private static final String CONTRACT_STATUS_DONE = "DONE";
    private static final RatingRequest DUMMY_SATISFIED_REQUEST = new RatingRequest(true);

    @KafkaListener(topics = CONTRACT_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeContractEvent(String message) {
        ContractEvent event;
        try {
            event = objectMapper.readValue(message, ContractEvent.class);
            log.info("Contract Event received: {}", event);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse Contract Event JSON: {}", message, e);
            return;
        }

        if (CONTRACT_STATUS_DONE.equalsIgnoreCase(event.status())) {
            log.info("Processing DONE contract event for code: {}", event.code());

            String clientCode = event.clientCode();
            String freelancerCode = event.freelancerCode();

            // 1. 클라이언트가 프리랜서에게 평가 (Client -> Freelancer)
            try {
                // 클라이언트(Caller)가 프리랜서(Receiver)를 평가
                log.info("Attempting to trigger Client({}) -> Freelancer({}) rating.", clientCode, freelancerCode);
                ratingService.updateRating(clientCode, freelancerCode, DUMMY_SATISFIED_REQUEST);
                log.info("Client -> Freelancer rating successful.");
            } catch (Exception e) {
                // 평가 중 발생한 예외(예: 유효하지 않은 회원 코드, 자기 자신 평가 시도 등) 처리
                log.error("Failed to process Client -> Freelancer rating for contract {}. Error: {}", event.code(), e.getMessage());
                // Kafka 메시지를 재처리하지 않으려면 여기서 Exception을 throw하지 않아야 합니다.
            }

            // 2. 프리랜서가 클라이언트에게 평가 (Freelancer -> Client)
            try {
                // 프리랜서(Caller)가 클라이언트(Receiver)를 평가
                log.info("Attempting to trigger Freelancer({}) -> Client({}) rating.", freelancerCode, clientCode);
                ratingService.updateRating(freelancerCode, clientCode, DUMMY_SATISFIED_REQUEST);
                log.info("Freelancer -> Client rating successful.");
            } catch (Exception e) {
                log.error("Failed to process Freelancer -> Client rating for contract {}. Error: {}", event.code(), e.getMessage());
            }

        } else {
            log.debug("Skipping contract event with status: {}", event.status());
        }
    }
}
