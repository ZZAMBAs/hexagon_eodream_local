package com.example.contractservice.contract.common.swagger.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "계약 취소", description = "로그인한 회원이 자신의 계약을 취소")
@Parameters({
    @Parameter(name = "X-CODE", description = "로그인 사용자 코드", in = ParameterIn.HEADER, required = true),
    @Parameter(name = "code", description = "취소할 계약 코드", in = ParameterIn.PATH)
})
public @interface ContractCancelApi {

}
