package com.example.profileservice.common.model.vo;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 모든 Controller에서 발생하는 예외를 처리
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @Valid 유효성 검사 실패 시 발생하는 예외 처리 (HTTP 400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        // 유효성 검사 실패 필드와 메시지를 추출하여 사용자 친화적인 메시지를 구성합니다.
        String detailedMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("[%s]: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(", "));

        log.warn("Validation Failed: {}", detailedMessage);

        // TODO: 나중에 구체적인 ErrorCode를 정의하여 "E_VALID_001" 등 사용 예정
        ErrorResponse response = ErrorResponse.of(
                HttpStatus.BAD_REQUEST,
                "E_VALID_001", // 임시 에러 코드
                "요청 데이터가 유효하지 않습니다: " + detailedMessage
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // CustomException 처리 (비즈니스 로직 예외)
//    @ExceptionHandler(CustomException.class)
//    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
//        log.warn("Custom Exception Occurred: Status={}, Message={}", ex.getStatus(), ex.getMessage());
//
//        // TODO: 나중에 CustomException의 status와 code를 사용하도록 개선 예정
//        ErrorResponse response = ErrorResponse.of(
//                ex.getStatus(),
//                "E_BIZ_001", // 임시 에러 코드
//                ex.getMessage()
//        );
//
//        return ResponseEntity.status(ex.getStatus()).body(response);
//    }

    // 기타 모든 예외 처리 (HTTP 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("Internal Server Error: ", ex);

        ErrorResponse response = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "E_SERVER_999", // 임시 에러 코드
                "서버에서 알 수 없는 오류가 발생했습니다."
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
