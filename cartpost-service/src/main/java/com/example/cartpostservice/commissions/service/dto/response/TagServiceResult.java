package com.example.cartpostservice.commissions.service.dto.response;

import java.util.List;

public record TagServiceResult(
        String commissionCode,

        List<String> tagCodes
) {

}
