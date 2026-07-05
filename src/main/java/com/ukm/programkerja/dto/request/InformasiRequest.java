package com.ukm.programkerja.dto.request;

import com.ukm.programkerja.entity.KategoriInformasi;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InformasiRequest {

    @NotBlank(message = "Judul wajib diisi")
    private String judul;

    private String isi;

    @NotNull(message = "Kategori wajib diisi")
    private KategoriInformasi kategori;

    @NotNull(message = "Tanggal wajib diisi")
    private LocalDate tanggal;
}
