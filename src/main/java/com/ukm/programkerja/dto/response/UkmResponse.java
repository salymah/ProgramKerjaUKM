package com.ukm.programkerja.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UkmResponse {
    private Long id;
    private String namaUkm;
    private String bidang;
    private String deskripsi;
    private String status;
    private LocalDateTime createdAt;
    private int jumlahAnggota;
}
