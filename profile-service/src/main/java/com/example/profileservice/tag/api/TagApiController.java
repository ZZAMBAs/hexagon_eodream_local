package com.example.profileservice.tag.api;

import com.example.profileservice.tag.model.dto.request.TagRequest;
import com.example.profileservice.tag.model.dto.response.TagResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Tag API", description = "기술 태그 및 회원-태그 연결 관리")
public interface TagApiController {

    // 전체 태그 목록 조회
    @Operation(summary = "전체 태그 목록 조회", description = "시스템에 등록된 모든 기술 태그 목록을 조회합니다.")
    @GetMapping
    ResponseEntity<List<TagResponse>> getAllTags();

    // 새 태그 등록
    @Operation(summary = "새 태그 등록", description = "새로운 기술 태그를 시스템에 등록합니다. (중복 등록 시 에러)")
    @PostMapping
    ResponseEntity<TagResponse> createTag(@Valid @RequestBody TagRequest request);

    // 회원 태그 목록 조회
    @Operation(summary = "회원 태그 목록 조회", description = "로그인된 회원이 보유한 기술 태그 목록을 조회합니다.")
    @GetMapping("/me")
    ResponseEntity<List<TagResponse>> getMyTags();

    // 회원 태그 연결
    @Operation(summary = "회원 태그 연결", description = "로그인된 회원의 프로필에 특정 태그를 연결합니다. (MemberTagEntity 생성)")
    @PostMapping("/{tagCode}/members/me")
    ResponseEntity<Void> linkMemberTag(@PathVariable String tagCode);

    // 회원 태그 연결 해제
    @Operation(summary = "회원 태그 연결 해제", description = "로그인된 회원의 프로필에서 특정 태그 연결을 해제합니다. (MemberTagEntity 삭제)")
    @DeleteMapping("/{tagCode}/members/me")
    ResponseEntity<Void> unlinkMemberTag(@PathVariable String tagCode);
}
