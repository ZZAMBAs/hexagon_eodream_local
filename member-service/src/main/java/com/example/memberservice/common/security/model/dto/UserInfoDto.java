package com.example.memberservice.common.security.model.dto;

import com.example.memberservice.common.model.vo.Provider;

public record UserInfoDto(
    String memberCode,

    String providerId,

    Provider provider
) {

}
