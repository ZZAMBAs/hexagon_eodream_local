package com.example.cartpostservice.commissions.controller.internal;

import com.example.cartpostservice.commissions.controller.dto.response.MemberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "member-service")
public interface MemberClient {

    @GetMapping("/internal/members")
    MemberResponse getMember(@RequestParam("member-code") String memberCode);
}
