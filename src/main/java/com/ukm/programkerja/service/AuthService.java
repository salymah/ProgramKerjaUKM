package com.ukm.programkerja.service;

import com.ukm.programkerja.dto.request.LoginRequest;
import com.ukm.programkerja.dto.request.LupaPasswordRequest;
import com.ukm.programkerja.dto.request.ProsesResetPasswordRequest;
import com.ukm.programkerja.dto.response.LoginResponse;
import com.ukm.programkerja.dto.response.ResetPasswordRequestResponse;

import java.util.List;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    /** Mengajukan permintaan reset password (fitur "Lupa Password"), tidak perlu login. */
    void ajukanLupaPassword(LupaPasswordRequest request);

    /** Daftar permintaan reset password yang masih PENDING, khusus WK III. */
    List<ResetPasswordRequestResponse> getDaftarPermintaanReset();

    /** WK III menetapkan password baru atas satu permintaan reset password. */
    void prosesResetPassword(Long requestId, ProsesResetPasswordRequest request);
}
