package com.ukm.programkerja.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Request body untuk mengajukan program kerja baru (UC-02).
 * tempatId opsional karena UKM bisa menentukan tempat belakangan (UC-04).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramKerjaCreateRequest {

    @NotBlank(message = "Nama program wajib diisi")
    @Size(max = 150, message = "Nama program maksimal 150 karakter")
    private String namaProgram;

    private String deskripsi;

    private LocalDate tanggalPengajuan;

    /** Tanggal pelaksanaan kegiatan — dipakai untuk cek bentrok jadwal tempat. */
    private LocalDate tanggalKegiatan;

    private Long tempatId;
}
