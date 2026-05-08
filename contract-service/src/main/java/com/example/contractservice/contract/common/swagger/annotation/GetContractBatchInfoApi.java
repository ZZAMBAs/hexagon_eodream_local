package com.example.contractservice.contract.common.swagger.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "계약 상태 변경 배치 실행 상태 조회",
        description = """
                계약 상태 변경 배치(statusChangeJob)의 최근 실행 상태를 조회합니다.
                date 또는 jobInstanceId 중 하나만 전달해야 합니다.
                date로 조회하면 해당 날짜 JobParameter(dateStr)를 가진 JobInstance의 마지막 실행 정보를 반환합니다.
                jobInstanceId로 조회하면 해당 JobInstance의 마지막 실행 정보를 반환합니다.
                """
)
@Parameters({
        @Parameter(
                name = "date",
                description = "조회할 계약 상태 변경 배치 기준 날짜. ISO-8601 날짜 형식입니다. 예: 2026-04-18",
                in = ParameterIn.QUERY,
                example = "2026-04-18"
        ),
        @Parameter(
                name = "jobInstanceId",
                description = "조회할 Spring Batch JobInstance ID. date와 동시에 사용할 수 없습니다.",
                in = ParameterIn.QUERY,
                example = "1"
        )
})
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "계약 상태 변경 배치 실행 상태 조회 성공"),
        @ApiResponse(
                responseCode = "400",
                description = "date와 jobInstanceId가 모두 없거나 둘 다 전달된 경우, 또는 해당 조건의 배치 실행 이력이 없는 경우",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
                                {
                                  "code": 4999,
                                  "httpStatus": 400,
                                  "message": "확인할 날짜 또는 배치 Job Instance id 중 하나만 넣어 요청해야 합니다.",
                                  "data": {}
                                }
                                """)
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "알 수 없는 서버 오류로 배치 실행 상태 조회에 실패한 경우",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
                                {
                                  "code": 20000,
                                  "httpStatus": 500,
                                  "message": "알 수 없는 오류로 요청을 처리할 수 없습니다.",
                                  "data": {}
                                }
                                """)
                )
        )
})
public @interface GetContractBatchInfoApi {

}
