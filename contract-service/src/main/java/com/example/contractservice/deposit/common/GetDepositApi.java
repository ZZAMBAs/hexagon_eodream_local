package com.example.contractservice.deposit.common;

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
@Operation(summary = "예치금 조회", description = "로그인한 사용자의 예치금 정보를 조회합니다.")
@Parameters({
    @Parameter(name = "X-CODE", in = ParameterIn.HEADER, description = "로그인 사용자 코드")
})
public @interface GetDepositApi {

}
