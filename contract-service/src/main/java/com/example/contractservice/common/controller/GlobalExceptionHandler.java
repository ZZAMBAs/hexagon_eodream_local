package com.example.contractservice.common.controller;

import com.example.contractservice.common.util.StringUtil;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.hexagon.core.dto.Empty;
import org.hexagon.core.dto.ResponseDto;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.launch.NoSuchJobInstanceException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final int GLOBAL_FAIL_STATUS_CODE = 4999;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<Empty> handle(MethodArgumentNotValidException e) {
        String allExceptionMessages = e.getBindingResult().getAllErrors().stream()
                .map(err -> err.getDefaultMessage())
                .collect(Collectors.joining(" | "));

        String message = StringUtil.format("입력 값이 올바르지 않습니다. {}", allExceptionMessages);

        return new ResponseDto<>(GLOBAL_FAIL_STATUS_CODE, HttpStatus.BAD_REQUEST.value(), message, Empty.getInstance());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<Empty> handle(BindException e) {
        String bindingFailObjects = e.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getObjectName)
                .collect(Collectors.joining(", "));

        String message = StringUtil.format("잘못된 타입의 입력이 존재합니다. 다음을 확인하십시오: {}", bindingFailObjects);

        return new ResponseDto<>(GLOBAL_FAIL_STATUS_CODE, HttpStatus.BAD_REQUEST.value(), message, Empty.getInstance());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<Empty> handle() {
        String message = "잘못된 타입의 입력이 있거나 입력 구조가 잘못되었습니다.";

        return new ResponseDto<>(GLOBAL_FAIL_STATUS_CODE, HttpStatus.BAD_REQUEST.value(), message, Empty.getInstance());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<Empty> handle(IllegalArgumentException e) {
        String message = "잘못된 인자를 넣어 요청했습니다.";

        return new ResponseDto<>(GLOBAL_FAIL_STATUS_CODE, HttpStatus.BAD_REQUEST.value(), message, Empty.getInstance());
    }

    @ExceptionHandler(JobExecutionException.class)
    public ResponseEntity<ResponseDto<Empty>> handle(JobExecutionException e) {
        ResponseDto<Empty> body;

        if (e instanceof JobExecutionAlreadyRunningException) {
            body = new ResponseDto<>(GLOBAL_FAIL_STATUS_CODE, HttpStatus.CONFLICT.value(),
                    "이미 해당 배치 처리가 실행 중입니다.", Empty.getInstance());
        }
        else if (e instanceof JobInstanceAlreadyCompleteException) {
            body = new ResponseDto<>(GLOBAL_FAIL_STATUS_CODE, HttpStatus.CONFLICT.value(),
                    "이미 해당 배치 처리는 완료되었습니다.", Empty.getInstance());
        } else if (e instanceof JobRestartException) {
            body = new ResponseDto<>(GLOBAL_FAIL_STATUS_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "알 수 없는 문제로 재시작할 수 없습니다.", Empty.getInstance());
        } else if (e instanceof JobParametersInvalidException) {
            body = new ResponseDto<>(GLOBAL_FAIL_STATUS_CODE, HttpStatus.BAD_REQUEST.value(),
                    "잘못된 값으로 배치 처리를 시도했습니다.", Empty.getInstance());
        } else if (e instanceof NoSuchJobInstanceException || e instanceof NoSuchJobExecutionException) {
            body = new ResponseDto<>(GLOBAL_FAIL_STATUS_CODE, HttpStatus.BAD_REQUEST.value(),
                    e.getMessage(), Empty.getInstance());
        } else {
            body = new ResponseDto<>(GLOBAL_FAIL_STATUS_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "알 수 없는 에러로 배치 처리가 불가능합니다.", Empty.getInstance());
        }

        return ResponseEntity.status(body.httpStatus()).body(body);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto<Empty> handle(Exception e) {
        log.error("알 수 없는 예외 발생. 빠른 확인 필요!", e);

        return ResponseDto.fail();
    }
}
