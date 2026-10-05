package com.familytech.translator.dto;

import java.time.ZonedDateTime;
import java.util.Map;

public record ApiErrorResponse(
    int status,
    String error,
    String message,
    String path,
    Map<String, String> fieldErrors,
    ZonedDateTime timestamp
) {
    public static ApiErrorResponse of(int status, String error, String message, String path, Map<String, String> fieldErrors) {
        return new ApiErrorResponse(status, error, message, path, fieldErrors, ZonedDateTime.now());
    }

    public static ApiErrorResponse of(int status, String error, String message, String path) {
        return of(status, error, message, path, null);
    }
}
