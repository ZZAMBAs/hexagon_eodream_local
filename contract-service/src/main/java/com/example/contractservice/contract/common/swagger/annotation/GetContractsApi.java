package com.example.contractservice.contract.common.swagger.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(summary = "계약 목록 조회", description = "로그인한 회원이 자신과 관련된 계약 목록을 커서 기반 페이지네이션으로 조회합니다. 커서는 마지막으로 조회한 엔티티 위치이고, 마지막으로 조회한 위치의 정보를 각각 아래에 넣어주면 됩니다.")
@Parameters({
    @Parameter(name = "X-CODE", description = "로그인 사용자 코드", in = ParameterIn.HEADER, required = true),
    @Parameter(name = "cursor-date", description = "커서 기반 페이징을 위한 날짜(ISO-8601 기반) 커서", in = ParameterIn.QUERY),
    @Parameter(name = "cursor-code", description = "커서 기반 페이징을 위한 계약 코드 커서", in = ParameterIn.QUERY),
    @Parameter(name = "order", description = "정렬 순서", in = ParameterIn.QUERY)
})
public @interface GetContractsApi {

}
