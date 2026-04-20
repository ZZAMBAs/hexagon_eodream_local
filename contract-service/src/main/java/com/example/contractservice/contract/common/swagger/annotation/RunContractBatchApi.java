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
        summary = "계약 상태 변경 배치 수동 실행",
        description = """
                전달한 날짜를 기준으로 계약 상태 변경 배치(statusChangeJob)를 비동기로 실행합니다.
                요청은 배치 완료까지 대기하지 않고 202 Accepted로 즉시 반환됩니다.
                실제 실행 상태는 계약 상태 변경 배치 실행 상태 조회 API로 확인해야 합니다.
                """
)
@Parameters({
        @Parameter(
                name = "date",
                description = "계약 상태 변경 배치를 실행할 기준 날짜. ISO-8601 날짜 형식입니다. 예: 2026-04-18",
                in = ParameterIn.QUERY,
                required = true,
                example = "2026-04-18"
        )
})
@ApiResponses({
        @ApiResponse(responseCode = "202", description = "계약 상태 변경 배치 실행 요청 접수 성공"),
        @ApiResponse(
                responseCode = "400",
                description = "date 값이 없거나 ISO-8601 날짜 형식이 아닌 경우",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
                                {
                                  "code": 4999,
                                  "httpStatus": 400,
                                  "message": "잘못된 값으로 배치 처리를 시도했습니다.",
                                  "data": {}
                                }
                                """)
                )
        ),
        @ApiResponse(
                responseCode = "409",
                description = "동일한 JobParameter의 배치가 이미 실행 중이거나 이미 완료된 경우",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
                                {
                                  "code": 4999,
                                  "httpStatus": 409,
                                  "message": "이미 해당 배치 처리가 실행 중입니다.",
                                  "data": {}
                                }
                                """)
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "배치 재시작 불가 상태이거나 알 수 없는 서버 오류로 실행 요청에 실패한 경우",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(value = """
                                {
                                  "code": 4999,
                                  "httpStatus": 500,
                                  "message": "예상치 못한 오류로 배치 처리가 불가능합니다.",
                                  "data": {}
                                }
                                """)
                )
        )
})
public @interface RunContractBatchApi {

}
