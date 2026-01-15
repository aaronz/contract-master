package com.contract.master.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public static class ApiResponse<T> {
        private int status;
        private String message;
        private T data;

        public ApiResponse() {}

        public ApiResponse(int status, String message, T data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }

        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public T getData() { return data; }
        public void setData(T data) { this.data = data; }

        public static <T> ApiResponse<T> success(T data) {
            return new ApiResponse<>(200, "Success", data);
        }

        public static ApiResponse<Object> error(int status, String message) {
            return new ApiResponse<>(status, message, null);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, e.getMessage()));
    }
}
