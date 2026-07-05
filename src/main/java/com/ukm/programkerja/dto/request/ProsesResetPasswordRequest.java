package com.ukm.programkerja.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Request body WK III untuk menetapkan password baru atas permintaan reset password. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProsesResetPasswordRequest {

    @NotBlank(message = "Password baru wajib diisi")
    @Size(min = 6, message = "Password baru minimal 6 karakter")
    private String passwordBaru;
}
