package com.example.profileservice.common.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ResponseDto<T> {

    private final int code; // 성공: 0, 실패: 3000번대

    @JsonProperty("httpStatus")
    private final int httpStatusCode;

    private final String message;

    private final T data;

    // 성공 응답 코드 정의
    private static final int SUCCESS_CODE = 0;
    private static final String SUCCESS_MESSAGE = "요청에 성공했습니다.";

    // 요청에 성공한 경우 (결과 값 없음)
    public static ResponseDto<Void> success() {
        return new ResponseDto<>(
                SUCCESS_CODE,
                HttpStatus.OK.value(),
                SUCCESS_MESSAGE,
                null
        );
    }

    // 요청에 성공한 경우 (결과 값 있음)
    public static <T> ResponseDto<T> success(T data) {
        // data가 null이 아니지만, Empty.getInstance()인 경우 data: {}로 출력하기 위해 Empty 인스턴스를 유지
        T result = (data instanceof Empty) ? data : data;

        return new ResponseDto<>(
                SUCCESS_CODE,
                HttpStatus.OK.value(),
                SUCCESS_MESSAGE,
                result
        );
    }

    /**
     * 요청 실패 시 ResponseDto를 생성 (GlobalExceptionHandler에서 사용)
     * @param code ErrorCode의 int 값 (3000번대)
     * @param httpStatus HTTP 상태 코드
     * @param message 사용자 친화적인 메시지
     */
    public static ResponseDto<Void> fail(int code, HttpStatus httpStatus, String message) {
        return new ResponseDto<>(
                code,
                httpStatus.value(),
                message,
                null
        );
    }
}
