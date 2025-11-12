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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Override
    public ResponseEntity<List<ResumeSimpleResponse>> getMyResumes() {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok(List.of(mockResumeSimple));
    }

    @Override
    public ResponseEntity<ResumeDetailResponse> createResume(@Valid @RequestBody ResumeCreateRequest request) {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok(mockResumeDetail);
    }

    @Override
    public ResponseEntity<ResumeDetailResponse> getResumeDetail(@PathVariable String resumeCode) {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok(mockResumeDetail);
    }

    @Override
    public ResponseEntity<ResumeDetailResponse> updateResume(@PathVariable String resumeCode,
            @Valid @RequestBody ResumeUpdateRequest request) {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok(mockResumeDetail);
    }

    @Override
    public ResponseEntity<Void> deleteResume(@PathVariable String resumeCode) {
        // TODO: 실제 로직 구현
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ExperienceResponse> createExperience(@PathVariable String resumeCode,
            @Valid @RequestBody ExperienceRequest request) {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok(mockExperience);
    }

    @Override
    public ResponseEntity<ExperienceResponse> updateExperience(@PathVariable String resumeCode,
            @PathVariable String experienceCode, @Valid @RequestBody ExperienceRequest request) {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok(mockExperience);
    }

    @Override
    public ResponseEntity<Void> deleteExperience(@PathVariable String resumeCode, @PathVariable String experienceCode) {
        // TODO: 실제 로직 구현
        return ResponseEntity.noContent().build();
    }
}
