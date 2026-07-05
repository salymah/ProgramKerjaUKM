package com.ukm.programkerja.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Satu item notifikasi yang dihitung on-the-fly (tidak disimpan permanen
 * di database, sesuai keputusan desain awal sistem ini) berdasarkan
 * kondisi data ProgramKerja terkini, disesuaikan per role yang meminta.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotifikasiItemResponse {
    private String tipe;        // PERLU_VERIFIKASI, PERLU_REVISI, DISETUJUI, PERLU_CATATAN, RESET_PASSWORD
    private String judul;
    private String pesan;
    private Long programKerjaId;
    private String tingkat;     // INFO, WARNING, DANGER
}
