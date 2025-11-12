package com.example.profileservice.tag.api;

import com.example.profileservice.tag.model.dto.request.TagRequest;
import com.example.profileservice.tag.model.dto.response.TagResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tags")
public class TagController implements TagApiController {

    // 임시 Mock 데이터 생성 (Swagger 출력을 위해)
    private final TagResponse tagSpring = new TagResponse("tag-001", "Spring Boot");
    private final TagResponse tagJPA = new TagResponse("tag-002", "Java");
    private final List<TagResponse> mockTags = List.of(tagSpring, tagJPA);

    @Override
    public ResponseEntity<List<TagResponse>> getAllTags() {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok(mockTags);
    }

    @Override
    public ResponseEntity<TagResponse> createTag(@Valid @RequestBody TagRequest request) {
        // TODO: 실제 로직 구현
        // Mock 응답: 생성된 태그의 코드를 반환한다고 가정
        TagResponse newTag = new TagResponse("tag-999", request.skill());
        // 새 리소스 생성 시 HTTP 201 Created 반환이 권장됨
        return ResponseEntity.status(HttpStatus.CREATED).body(newTag);
    }

    @Override
    public ResponseEntity<List<TagResponse>> getMyTags() {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok(List.of(tagSpring));
    }

    @Override
    public ResponseEntity<Void> linkMemberTag(@PathVariable String tagCode) {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> unlinkMemberTag(@PathVariable String tagCode) {
        // TODO: 실제 로직 구현
        return ResponseEntity.noContent().build();
    }
}
