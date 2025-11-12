package com.example.profileservice.rating.api;

import com.example.profileservice.rating.model.dto.request.RatingRequest;
import com.example.profileservice.rating.model.dto.response.RatingResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ratings")
public class RatingController implements RatingApiController {

    // 임시 Mock 데이터 생성 (Swagger 출력을 위해)
    private final RatingResponse mockRating = new RatingResponse("member-001", 10, 2);

    @Override
    public ResponseEntity<RatingResponse> getMemberRating(@PathVariable String memberCode) {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok(mockRating);
    }

    @Override
    public ResponseEntity<RatingResponse> updateRating(@PathVariable String memberCode,
            @Valid @RequestBody RatingRequest request) {
        // TODO: 실제 로직 구현
        return ResponseEntity.ok(mockRating);
    }
}
