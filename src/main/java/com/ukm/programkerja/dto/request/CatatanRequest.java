package com.ukm.programkerja.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request body untuk menambahkan catatan/saran pada program kerja.
 * Dipakai oleh WK III (saat verifikasi, UC-09) maupun Pembina
 * (saat evaluasi laporan, UC-16).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CatatanRequest {

    @NotBlank(message = "Isi catatan wajib diisi")
    private String isiCatatan;
}
