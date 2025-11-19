package com.example.profileservice.common.model.vo;

import com.example.profileservice.selfPromotion.model.dto.response.SelfPromotionEsEventData;
import com.example.profileservice.selfPromotion.model.dto.response.SelfPromotionInitEvent;
import com.example.profileservice.selfPromotion.model.dto.response.SelfPromotionResponse;
import com.example.profileservice.selfPromotion.repository.SelfPromotionRepository;
import com.example.profileservice.selfPromotion.service.SelfPromotionService;
import com.example.profileservice.tag.model.dto.response.TagInitEvent;
import com.example.profileservice.tag.model.dto.response.TagResponse;
import com.example.profileservice.tag.repository.TagRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileDataInitializer {

    private final SelfPromotionRepository selfPromotionRepository;
    private final TagRepository tagRepository;
    private final KafkaProducer kafkaProducer;
    private final SelfPromotionService selfPromotionService;

    // 토픽 이름 설정 (application.yml 또는 default 값 사용)
    @Value("${topics.selfpromotion-init-events:selfpromotion-init-events}")
    private String selfPromotionInitTopic;

    @Value("${topics.tag-init-events:tag-init-events}")
    private String tagInitTopic;

    // 애플리케이션이 완전히 준비된 후 (DB 연결, 빈 초기화 완료) 초기화 이벤트를 발행합니다.
    @EventListener(ApplicationReadyEvent.class)
    @Transactional(readOnly = true)
    public void sendInitialDataEvents() {
        log.info("어플리케이션이 준비되었습니다. 초기 데이터 동기화 이벤트를 보내는 중...");

        // 1. Self Promotion 초기화 이벤트 전송
        sendInitialSelfPromotionData();

        // 2. Tag 초기화 이벤트 전송
        sendInitialTagData();

        log.info("초기 데이터 동기화 이벤트가 성공적으로 전송되었습니다.");
    }

    // Self Promotion 전체 데이터를 조회하여 Kafka에 전송
    private void sendInitialSelfPromotionData() {
        // Soft Delete 되지 않은 모든 Self Promotion 엔티티 조회
        List<SelfPromotionResponse> allPromotions = selfPromotionService.getAllPromotions();

        // ES 최적화 DTO로 변환
        List<SelfPromotionEsEventData> eventDataList = allPromotions.stream()
                .map(SelfPromotionEsEventData::fromResponse)
                .collect(Collectors.toList());

        SelfPromotionInitEvent event = SelfPromotionInitEvent.create(eventDataList);

        kafkaProducer.send(selfPromotionInitTopic, event);
        log.info("  -> {}개의 SelfPromotion 레코드를 토픽: {}로 보냈습니다.", eventDataList.size(), selfPromotionInitTopic);
    }

    // Tag 전체 데이터를 조회하여 Kafka에 전송
    private void sendInitialTagData() {
        // 모든 Tag 엔티티 조회
        List<TagResponse> allTags = tagRepository.findAll().stream()
                .map(tag -> new TagResponse(tag.getCode(), tag.getSkill()))
                .collect(Collectors.toList());

        TagInitEvent event = TagInitEvent.create(allTags);

        kafkaProducer.send(tagInitTopic, event);
        log.info("  -> Sent {} Tag records to topic: {}", allTags.size(), tagInitTopic);
    }
}