package com.hamidspecial.medihive.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

import lombok.Builder;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {
    private final String code;
    private final String description;
    private final T data;
    private final List<T> list;
    private final Integer pageNumber;
    private final Integer pageSize;
    private final Long noOfRecords;
    private final Long identityValue;
    private final LocalDateTime timestamp;
    private final int status;
    private final List<String> errors;

    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .code("00")
                .description("Success")
                .data(data)
                .timestamp(LocalDateTime.now())
                .status(200)
                .build();
    }

    public static <T> Result<T> success(Long identityValue) {
        return Result.<T>builder()
                .code("00")
                .description("Success")
                .identityValue(identityValue)
                .timestamp(LocalDateTime.now())
                .status(200)
                .build();
    }

    public static <T> Result<T> success(List<T> list, int pageNumber, int pageSize, long noOfRecords) {
        return Result.<T>builder()
                .code("00")
                .description("Success")
                .list(list)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .noOfRecords(noOfRecords)
                .timestamp(LocalDateTime.now())
                .status(200)
                .build();
    }

    public static <T> Result<T> error(String code, String description, int status, List<String> errors) {
        return Result.<T>builder()
                .code(code)
                .description(description)
                .timestamp(LocalDateTime.now())
                .status(status)
                .errors(errors)
                .build();
    }

    public static <T> Result<T> error(String code, String description) {
        return Result.<T>builder()
                .code(code)
                .description(description)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

