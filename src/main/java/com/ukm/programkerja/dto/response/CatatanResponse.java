package com.ukm.programkerja.dto.response;

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
public class CatatanResponse {

    private Long id;
    private Long programKerjaId;
    private String namaProgram;
    private Long penulisId;
    private String namaPenulis;
    private String penulisRole;
    private String isiCatatan;
    private LocalDate tanggal;
}
