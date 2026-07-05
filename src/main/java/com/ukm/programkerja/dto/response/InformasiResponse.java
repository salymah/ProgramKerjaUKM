package com.ukm.programkerja.dto.response;

import com.ukm.programkerja.entity.KategoriInformasi;
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
public class InformasiResponse {
    private Long id;
    private String judul;
    private String isi;
    private KategoriInformasi kategori;
    private LocalDate tanggal;
    private String dibuatOleh;
}
