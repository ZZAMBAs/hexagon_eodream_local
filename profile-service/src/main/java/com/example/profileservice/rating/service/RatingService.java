package com.example.profileservice.rating.service;

import static com.example.profileservice.common.model.vo.ErrorCode.CANNOT_RATE_MYSELF;
import static com.example.profileservice.common.model.vo.ErrorCode.RATING_MEMBER_NOT_FOUND;

import com.example.profileservice.common.model.vo.exception.CustomException;
import com.example.profileservice.rating.model.dto.request.RatingRequest;
import com.example.profileservice.rating.model.dto.response.RatingResponse;
import com.example.profileservice.rating.model.entity.RatingEntity;
import com.example.profileservice.rating.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RatingService {

    private final RatingRepository ratingRepository;

    // 특정 회원의 평가 카운트를 조회
    public RatingResponse getMemberRating(String receiverCode) {
        RatingEntity rating = ratingRepository.findByReceiverCode(receiverCode)
                .orElseGet(() -> RatingEntity.builder().receiverCode(receiverCode).build()); // 존재하지 않으면 0/0으로 임시 생성

        return toResponse(rating);
    }

    // 특정 회원에게 만족 또는 불만족 평가 카운트를 1 증가
    @Transactional
    public RatingResponse updateRating(String callerCode, String receiverCode, RatingRequest request) {
        // 1. 자기 자신 평가 금지
        if (callerCode.equals(receiverCode)) {
            throw new CustomException(CANNOT_RATE_MYSELF);
        }

        // 2. 평가 대상 엔티티 확인 (없으면 생성)
        if (!ratingRepository.existsByReceiverCode(receiverCode)) {
            RatingEntity newRating = RatingEntity.builder().receiverCode(receiverCode).build();
            ratingRepository.save(newRating);
        }

        // 3. 원자적 업데이트 쿼리 실행
        if (request.satisfied()) {
            ratingRepository.incrementSatisfiedCount(receiverCode);
        } else {
            ratingRepository.incrementUnsatisfiedCount(receiverCode);
        }

        // 4. 업데이트된 최신 데이터 조회 후 반환
        RatingEntity updatedRating = ratingRepository.findByReceiverCode(receiverCode)
                .orElseThrow(() -> new CustomException(RATING_MEMBER_NOT_FOUND));

        return toResponse(updatedRating);
    }

    private RatingResponse toResponse(RatingEntity entity) {
        return new RatingResponse(
                entity.getReceiverCode(),
                entity.getSatisfiedCount(),
                entity.getUnsatisfiedCount()
        );
    }
}
