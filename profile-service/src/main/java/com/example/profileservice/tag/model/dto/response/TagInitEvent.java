package com.example.profileservice.tag.model.dto.response;

import java.time.Instant;
import java.util.List;

public record TagInitEvent(
        Instant createdAt,
        List<TagResponse> tags
) {
    public static TagInitEvent create(List<TagResponse> dataList) {
        return new TagInitEvent(Instant.now(), dataList);
    }
}
