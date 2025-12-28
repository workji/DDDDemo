package com.example.userapi.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 統一エラーレスポンス
 */
public record ErrorResponse(
        int status,
        String error,
        String message,
        List<FieldError> fieldErrors,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp
) {
    public ErrorResponse(int status, String error, String message, List<FieldError> fieldErrors) {
        this(status, error, message, fieldErrors, LocalDateTime.now());
    }

    /**
     * フィールドエラー詳細
     */
    public record FieldError(
            String field,
            String rejectedValue,
            String message
    ) {}
}