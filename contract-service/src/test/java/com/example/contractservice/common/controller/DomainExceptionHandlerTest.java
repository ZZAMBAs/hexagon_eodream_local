package com.example.contractservice.common.controller;

import static com.example.contractservice.settlement.domain.exception.SettlementErrorCode.SETTLEMENT_NOT_EXISTS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.contractservice.settlement.domain.exception.SettlementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

class DomainExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new SettlementExceptionTestController())
                .setControllerAdvice(new DomainExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("SettlementException이 발생했을 때 제대로 BAD_REQUEST 응답을 받을 수 있다")
    void map_settlement_exception_to_domain_error_response() throws Exception {
        mockMvc.perform(get("/settlement-exception-test"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(4201))
                .andExpect(jsonPath("$.httpStatus").value(400));
    }

    @Controller
    static class SettlementExceptionTestController {

        @ResponseBody
        @GetMapping("/settlement-exception-test")
        public void throwSettlementException() {
            throw new SettlementException(SETTLEMENT_NOT_EXISTS);
        }
    }
}
