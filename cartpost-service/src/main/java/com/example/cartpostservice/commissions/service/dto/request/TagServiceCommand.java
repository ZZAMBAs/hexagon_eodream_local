package com.example.cartpostservice.commissions.service.dto.request;

import java.util.List;

public record TagServiceCommand(
        String commissionsCode,

        List<String> tagCodes
) {
}
