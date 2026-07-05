package com.ukm.programkerja.dto.request;

import com.ukm.programkerja.entity.StatusProgramKerja;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request body untuk WK III memverifikasi pengajuan program kerja
 * (UC-09, UC-10, UC-11). Hanya status DISETUJUI atau REVISI yang valid
 * dikirim lewat endpoint ini (divalidasi di Service layer).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifikasiRequest {

    @NotNull(message = "Status verifikasi wajib diisi")
    private StatusProgramKerja status;

    /**
     * Wajib diisi jika status = REVISI (alasan/catatan revisi).
     * Opsional jika status = DISETUJUI.
     */
    private String keterangan;
}
