package com.example.profileservice.selfPromotion.api;

import com.example.profileservice.common.model.vo.PaymentType;
import com.example.profileservice.selfPromotion.model.dto.request.SelfPromotionCreateRequest;
import com.example.profileservice.selfPromotion.model.dto.request.SelfPromotionUpdateRequest;
import com.example.profileservice.selfPromotion.model.dto.response.SelfPromotionResponse;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/self-promotions")
public class SelfPromotionController implements SelfPromotionApiController {

    // 임시 Mock 데이터 생성 (Swagger 출력을 위해)
    private final SelfPromotionResponse mockPromotion = new SelfPromotionResponse("promo-001", "member-001",
            "MSA 전문가 찾으세요?", "복잡한 환경 구축 경험 보유", PaymentType.MONTHLY, 5000000L, "res-001", Instant.now(), Instant.now());

    @Override
    public ResponseEntity<List<SelfPromotionResponse>> getMyPromotions() {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok(List.of(mockPromotion));
    }

    @Override
    public ResponseEntity<SelfPromotionResponse> createPromotion(
            @Valid @RequestBody SelfPromotionCreateRequest request) {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok(mockPromotion);
    }

    @Override
    public ResponseEntity<SelfPromotionResponse> getPromotionDetail(@PathVariable String promotionCode) {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok(mockPromotion);
    }

    @Override
    public ResponseEntity<SelfPromotionResponse> updatePromotion(@PathVariable String promotionCode,
            @Valid @RequestBody SelfPromotionUpdateRequest request) {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok(mockPromotion);
    }

    @Override
    public ResponseEntity<Void> deletePromotion(@PathVariable String promotionCode) {
        // TODO: 실제 로직 구현
        return ResponseEntity.noContent().build();
    }
}
