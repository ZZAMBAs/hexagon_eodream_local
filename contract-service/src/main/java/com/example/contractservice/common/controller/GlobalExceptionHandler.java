package com.example.contractservice.common.controller;

import com.example.contractservice.common.util.StringUtil;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.hexagon.core.dto.ResponseDto;
import org.hexagon.core.dto.Empty;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<Empty> handle(MethodArgumentNotValidException e) {
        String allExceptionMessages = e.getBindingResult().getAllErrors().stream().map(err -> err.getDefaultMessage())
                .collect(Collectors.joining(" | "));

        String message = StringUtil.format("?낅젰 媛믪씠 ?щ컮瑜댁? ?딆뒿?덈떎. {}", allExceptionMessages);

        return new ResponseDto<>(4999, HttpStatus.BAD_REQUEST.value(), message, Empty.getInstance());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<Empty> handle(BindException e) {
        String bindingFailObjects = e.getBindingResult().getAllErrors().stream().map(ObjectError::getObjectName)
                .collect(Collectors.joining(", "));

        String message = StringUtil.format("?섎せ????낆쓽 ?낅젰??議댁옱?⑸땲?? ?ㅼ쓬???뺤씤?섏떗?쒖삤: {}", bindingFailObjects);

        return new ResponseDto<>(4999, HttpStatus.BAD_REQUEST.value(), message, Empty.getInstance());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<Empty> handle() {
        String message = "?섎せ????낆쓽 ?낅젰???덇굅???낅젰 援ъ“媛 ?섎せ?섏뿀?듬땲??";

        return new ResponseDto<>(4999, HttpStatus.BAD_REQUEST.value(), message, Empty.getInstance());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto<Empty> handle(Exception e) {
        log.error("?????녿뒗 ?덉쇅 諛쒖깮. 鍮좊Ⅸ ?뺤씤 ?꾩슂!", e);

        return ResponseDto.fail();
    }
}
