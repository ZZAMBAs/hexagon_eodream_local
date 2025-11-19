package com.example.profileservice.rating.model.dto.request;

import java.util.List;

public record MemberExistOutput(
        List<String> exists,
        List<String> notExists
) {

}
