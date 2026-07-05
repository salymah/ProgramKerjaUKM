package com.ukm.programkerja.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request body untuk membuat/memperbarui data Tempat.
 * Dipakai untuk create maupun update agar tidak ada duplikasi DTO
 * (field yang dibutuhkan sama persis).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TempatRequest {

    @NotBlank(message = "Nama tempat wajib diisi")
    @Size(max = 100, message = "Nama tempat maksimal 100 karakter")
    private String namaTempat;

    private String alamat;

    @Size(max = 50, message = "Status maksimal 50 karakter")
    private String status;
}
