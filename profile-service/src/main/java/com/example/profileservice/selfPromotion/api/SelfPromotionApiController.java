package com.example.profileservice.selfPromotion.api;

import com.example.profileservice.selfPromotion.model.dto.request.SelfPromotionCreateRequest;
import com.example.profileservice.selfPromotion.model.dto.request.SelfPromotionUpdateRequest;
import com.example.profileservice.selfPromotion.model.dto.response.SelfPromotionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Self Promotion API", description = "프리랜서 수주글 관리")
public interface SelfPromotionApiController {

    // 내 프로모션 목록 조회
    @Operation(summary = "내 프로모션 목록 조회", description = "로그인된 프리랜서가 작성한 모든 프로모션 목록을 조회합니다.")
    ResponseEntity<List<SelfPromotionResponse>> getMyPromotions();

    // 프로모션 등록
    @Operation(summary = "프로모션 등록", description = "새로운 셀프 프로모션 게시글을 등록합니다.")
    ResponseEntity<SelfPromotionResponse> createPromotion(@Valid @RequestBody SelfPromotionCreateRequest request);

    // 프로모션 상세 조회
    @Operation(summary = "프로모션 상세 조회", description = "특정 프로모션 게시글의 상세 정보를 조회합니다.")
    ResponseEntity<SelfPromotionResponse> getPromotionDetail(@PathVariable String promotionCode);

    // 프로모션 수정
    @Operation(summary = "프로모션 수정", description = "특정 프로모션 게시글의 내용을 수정합니다.")
    ResponseEntity<SelfPromotionResponse> updatePromotion(@PathVariable String promotionCode,
            @Valid @RequestBody SelfPromotionUpdateRequest request);

    // 프로모션 삭제
    @Operation(summary = "프로모션 삭제", description = "특정 프로모션 게시글을 논리적으로 삭제합니다.")
    ResponseEntity<Void> deletePromotion(@PathVariable String promotionCode);
}
