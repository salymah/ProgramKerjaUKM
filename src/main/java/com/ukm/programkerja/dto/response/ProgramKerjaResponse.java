package com.ukm.programkerja.dto.response;

import com.ukm.programkerja.entity.StatusProgramKerja;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramKerjaResponse {

    private Long id;
    private String namaProgram;
    private String deskripsi;
    private LocalDate tanggalPengajuan;
    private LocalDate tanggalKegiatan;
    private LocalDate tanggalVerifikasi;
    private StatusProgramKerja status;
    private String keterangan;

    // Ringkasan data pengaju (tidak mengirim seluruh UserResponse agar payload ringkas)
    private Long userId;
    private String namaPengaju;

    // Ringkasan data tempat
    private Long tempatId;
    private String namaTempat;
}
