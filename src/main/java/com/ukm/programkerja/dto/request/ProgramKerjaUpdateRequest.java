package com.ukm.programkerja.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request body untuk memperbarui data program kerja (oleh UKM pemilik,
 * misal saat menindaklanjuti status REVISI).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramKerjaUpdateRequest {

    @NotBlank(message = "Nama program wajib diisi")
    @Size(max = 150, message = "Nama program maksimal 150 karakter")
    private String namaProgram;

    private String deskripsi;

    /** Tanggal pelaksanaan kegiatan — dipakai untuk cek bentrok jadwal tempat. */
    private java.time.LocalDate tanggalKegiatan;

    private Long tempatId;
}
