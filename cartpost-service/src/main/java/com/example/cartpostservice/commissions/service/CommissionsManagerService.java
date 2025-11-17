package com.example.cartpostservice.commissions.service;

import com.example.cartpostservice.commissions.service.dto.response.CommissionCreateResult;
import com.example.cartpostservice.commissions.service.dto.response.CommissionDeleteResult;
import com.example.cartpostservice.commissions.service.dto.response.CommissionFinishResult;
import com.example.cartpostservice.commissions.service.dto.response.CommissionReadResult;
import com.example.cartpostservice.commissions.service.dto.response.CommissionSortReadResult;
import com.example.cartpostservice.commissions.service.dto.response.CommissionUpdateResult;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommissionsManagerService {

    public CommissionCreateResult createCommission(String code) {
        return null;
    }

    public CommissionReadResult readCommission(String commissionsCode) {
        return null;
    }

    public CommissionUpdateResult updateCommission(String code, String commissionsCode) {
        return null;
    }

    public CommissionDeleteResult deleteCommission(String code, String commissionsCode) {
        return null;
    }

    public CommissionFinishResult finishCommission(String code) {
        return null;
    }

    public CommissionSortReadResult readOwnCommissions(String code, Pageable pageable) {
        return null;
    }
}
