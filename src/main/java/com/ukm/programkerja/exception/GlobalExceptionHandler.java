package com.ukm.programkerja.exception;

import com.ukm.programkerja.dto.response.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Global exception handler. Menangkap seluruh exception yang terjadi di
 * Controller/Service dan mengubahnya menjadi response JSON yang konsisten
 * melalui GlobalResponse, sehingga frontend selalu menerima format yang sama
 * baik untuk response sukses maupun gagal.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GlobalResponse<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(GlobalResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<GlobalResponse<Object>> handleDuplicateResource(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(GlobalResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<GlobalResponse<Object>> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(GlobalResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<GlobalResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(GlobalResponse.error("Anda tidak memiliki akses untuk melakukan aksi ini"));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<GlobalResponse<Object>> handleMaxUploadSize(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(GlobalResponse.error("Ukuran file melebihi batas maksimal yang diizinkan (5MB)"));
    }

    /**
     * Menangani error validasi dari anotasi @Valid pada DTO request.
     * Mengembalikan map field -> pesan error agar frontend bisa menampilkan
     * pesan validasi per-field secara spesifik.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        GlobalResponse<Object> response = GlobalResponse.builder()
                .success(false)
                .message("Data yang dikirim tidak valid")
                .data(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Fallback handler untuk exception yang tidak terduga / belum ditangani
     * secara spesifik. Tidak menampilkan stack trace mentah ke client.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalResponse<Object>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(GlobalResponse.error("Terjadi kesalahan pada server: " + ex.getMessage()));
    }
}
