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
@Operation(summary = "계약 생성", description = "REQUESTED 상태인 계약을 생성")
@Parameters({
        @Parameter(name = "X-CODE", description = "로그인 사용자 코드", in = ParameterIn.HEADER, required = true)
})
public @interface ContractCreateApi {

}
