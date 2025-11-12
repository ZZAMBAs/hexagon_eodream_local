package com.example.profileservice.rating.api;

import com.example.profileservice.rating.model.dto.request.RatingRequest;
import com.example.profileservice.rating.model.dto.response.RatingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Rating API", description = "회원 평가 관리 (카운트 집계)")
public interface RatingApiController {

    // 특정 회원 평가 조회
    @Operation(summary = "특정 회원 평가 조회", description = "특정 회원이 받은 만족/불만족 평가 카운트를 조회합니다.")
    @GetMapping("/{memberCode}")
    ResponseEntity<RatingResponse> getMemberRating(@PathVariable String memberCode);

    // 평가 등록/업데이트
    @Operation(summary = "평가 등록/업데이트", description = "특정 회원에게 만족 또는 불만족 평가 카운트를 1 증가시킵니다. (최초 평가 시 엔티티 자동 생성)")
    @PatchMapping("/{memberCode}")
    ResponseEntity<RatingResponse> updateRating(@PathVariable String memberCode,
            @Valid @RequestBody RatingRequest request);
}
