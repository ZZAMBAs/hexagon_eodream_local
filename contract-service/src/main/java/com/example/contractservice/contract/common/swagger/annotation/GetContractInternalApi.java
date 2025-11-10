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
@Operation(summary = "계약 조회", description = "특정 계약에 대한 내용과 참여하는 회원, 프리랜서 이름을 출력")
@Parameters({
    @Parameter(name = "code", description = "조회할 계약 코드", in = ParameterIn.PATH)
})
public @interface GetContractInternalApi {

}
