package com.ukm.programkerja.controller;

import com.ukm.programkerja.dto.request.LoginRequest;
import com.ukm.programkerja.dto.request.LupaPasswordRequest;
import com.ukm.programkerja.dto.request.ProsesResetPasswordRequest;
import com.ukm.programkerja.dto.response.GlobalResponse;
import com.ukm.programkerja.dto.response.LoginResponse;
import com.ukm.programkerja.dto.response.ResetPasswordRequestResponse;
import com.ukm.programkerja.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Endpoint autentikasi dan reset password")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<GlobalResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(GlobalResponse.success("Login berhasil", authService.login(request)));
    }

    /**
     * Endpoint publik (tidak perlu login) untuk mengajukan permintaan lupa password.
     * WK III akan melihat daftar permintaan ini di endpoint GET /api/auth/reset-requests
     * dan memprosesnya via PATCH /api/auth/reset-requests/{id}.
     */
    @PostMapping("/lupa-password")
    public ResponseEntity<GlobalResponse<Object>> lupaPassword(@Valid @RequestBody LupaPasswordRequest request) {
        authService.ajukanLupaPassword(request);
        return ResponseEntity.ok(GlobalResponse.success(
                "Permintaan reset password berhasil dikirim. Silakan hubungi WK III untuk konfirmasi."));
    }

    @GetMapping("/reset-requests")
    @PreAuthorize("hasRole('WKIII')")
    public ResponseEntity<GlobalResponse<List<ResetPasswordRequestResponse>>> getDaftarPermintaanReset() {
        return ResponseEntity.ok(GlobalResponse.success(
                "Daftar permintaan reset password", authService.getDaftarPermintaanReset()));
    }

    @PatchMapping("/reset-requests/{id}")
    @PreAuthorize("hasRole('WKIII')")
    public ResponseEntity<GlobalResponse<Object>> prosesResetPassword(
            @PathVariable Long id,
            @Valid @RequestBody ProsesResetPasswordRequest request) {
        authService.prosesResetPassword(id, request);
        return ResponseEntity.ok(GlobalResponse.success("Password berhasil diatur ulang"));
    }
}
