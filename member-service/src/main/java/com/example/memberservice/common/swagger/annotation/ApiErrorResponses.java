package com.example.memberservice.common.swagger.annotation;

import com.example.memberservice.common.exception.ErrorCode;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiErrorResponses {

    ErrorCode[] exceptions() default {};
}