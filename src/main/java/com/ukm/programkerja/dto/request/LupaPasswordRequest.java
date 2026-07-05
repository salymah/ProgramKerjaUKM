package com.ukm.programkerja.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Request body untuk mengajukan permintaan reset password (UC tambahan, tanpa perlu login). */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LupaPasswordRequest {

    @NotBlank(message = "Username wajib diisi")
    private String username;

    private String alasan;
}
