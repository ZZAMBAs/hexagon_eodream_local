package com.example.cartpostservice.commissions.service;

import com.example.cartpostservice.commissions.service.dto.response.CommissionCreateResult;
import com.example.cartpostservice.commissions.service.dto.response.CommissionDeleteResult;
import com.example.cartpostservice.commissions.service.dto.response.CommissionFinishResult;
import com.example.cartpostservice.commissions.service.dto.response.CommissionReadResult;
import com.example.cartpostservice.commissions.service.dto.response.CommissionSortReadResult;
import com.example.cartpostservice.commissions.service.dto.response.CommissionUpdateResult;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public interface CommissionsService {

    public CommissionCreateResult createCommission(String code);

    public CommissionReadResult readCommission(String commissionsCode);

    public CommissionUpdateResult updateCommission(String code, String commissionsCode);

    public CommissionDeleteResult deleteCommission(String code, String commissionsCode);

    public CommissionFinishResult finishCommission(String code);

    public CommissionSortReadResult readOwnCommissions(String code, Pageable pageable);
}