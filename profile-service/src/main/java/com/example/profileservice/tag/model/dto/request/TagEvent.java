package com.example.profileservice.tag.model.dto.request;

import com.example.profileservice.common.model.vo.OperationType;
import com.example.profileservice.tag.model.dto.response.TagResponse;
import java.time.Instant;

public record TagEvent(
        OperationType operationType,
        Instant createdAt,
        TagResponse data
) {
    public static TagEvent create(TagResponse data) {
        return new TagEvent(OperationType.CREATE, Instant.now(), data);
    }
    public static TagEvent update(TagResponse data) {
        return new TagEvent(OperationType.UPDATE, Instant.now(), data);
    }
    // 태그는 삭제 기능이 없으나, 만약을 위해 정의
    public static TagEvent delete(TagResponse data) {
        return new TagEvent(OperationType.DELETE, Instant.now(), data);
    }
}
