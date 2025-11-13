package com.example.profileservice.resume.api;

import com.example.profileservice.experience.model.dto.request.ExperienceRequest;
import com.example.profileservice.experience.model.dto.response.ExperienceResponse;
import com.example.profileservice.resume.model.dto.request.ResumeCreateRequest;
import com.example.profileservice.resume.model.dto.request.ResumeUpdateRequest;
import com.example.profileservice.resume.model.dto.response.ResumeDetailResponse;
import com.example.profileservice.resume.model.dto.response.ResumeSimpleResponse;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController implements ResumeApiController {

    // 임시 Mock 데이터 생성 (Swagger 출력을 위해)
    private final ExperienceResponse mockExperience = new ExperienceResponse("exp-001", "헥사곤 프로젝트", "헥사곤", "MSA 개발",
            Instant.now().minusSeconds(3600), null);
    private final ResumeDetailResponse mockResumeDetail = new ResumeDetailResponse("res-001", "백엔드 개발자 이력서 1",
            "최신 기술 스택 활용", "http://github.com/test", Instant.now(), Instant.now(), List.of(mockExperience));
    private final ResumeSimpleResponse mockResumeSimple = new ResumeSimpleResponse("res-001", "백엔드 이력서 1",
            Instant.now());

    private static final String DEFAULT_MEMBER_CODE = "member-uuid-code-001";

    @Override
    @GetMapping("/me")
    public ResponseEntity<List<ResumeSimpleResponse>> getMyResumes(
            @RequestHeader(value = MEMBER_CODE_HEADER, defaultValue = DEFAULT_MEMBER_CODE) String memberCode
    ) {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok(List.of(mockResumeSimple));
    }

    @Override
    @PostMapping
    public ResponseEntity<ResumeDetailResponse> createResume(
            @RequestHeader(value = MEMBER_CODE_HEADER, defaultValue = DEFAULT_MEMBER_CODE) String memberCode,
            @Valid @RequestBody ResumeCreateRequest request) {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok(mockResumeDetail);
    }

    @Override
    @GetMapping("/{resumeCode}")
    public ResponseEntity<ResumeDetailResponse> getResumeDetail(
            @RequestHeader(value = MEMBER_CODE_HEADER, defaultValue = DEFAULT_MEMBER_CODE) String memberCode,
            @PathVariable String resumeCode) {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok(mockResumeDetail);
    }

    @Override
    @PatchMapping("/{resumeCode}")
    public ResponseEntity<ResumeDetailResponse> updateResume(
            @RequestHeader(value = MEMBER_CODE_HEADER, defaultValue = DEFAULT_MEMBER_CODE) String memberCode,
            @PathVariable String resumeCode,
            @Valid @RequestBody ResumeUpdateRequest request) {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok(mockResumeDetail);
    }

    @Override
    @DeleteMapping("/{resumeCode}")
    public ResponseEntity<Void> deleteResume(
            @RequestHeader(value = MEMBER_CODE_HEADER, defaultValue = DEFAULT_MEMBER_CODE) String memberCode,
            @PathVariable String resumeCode
    ) {
        // TODO: 실제 로직 구현
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/{resumeCode}/experiences")
    public ResponseEntity<ExperienceResponse> createExperience(
            @RequestHeader(value = MEMBER_CODE_HEADER, defaultValue = DEFAULT_MEMBER_CODE) String memberCode,
            @PathVariable String resumeCode,
            @Valid @RequestBody ExperienceRequest request
    ) {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok(mockExperience);
    }

    @Override
    @PatchMapping("/{resumeCode}/experiences/{experienceCode}")
    public ResponseEntity<ExperienceResponse> updateExperience(
            @RequestHeader(value = MEMBER_CODE_HEADER, defaultValue = DEFAULT_MEMBER_CODE) String memberCode,
            @PathVariable String resumeCode,
            @PathVariable String experienceCode,
            @Valid @RequestBody ExperienceRequest request
    ) {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok(mockExperience);
    }

    @Override
    @DeleteMapping("/{resumeCode}/experiences/{experienceCode}")
    public ResponseEntity<Void> deleteExperience(
            @RequestHeader(value = MEMBER_CODE_HEADER, defaultValue = DEFAULT_MEMBER_CODE) String memberCode,
            @PathVariable String resumeCode,
            @PathVariable String experienceCode
    ) {
        // TODO: 실제 로직 구현
        return ResponseEntity.noContent().build();
    }
}
