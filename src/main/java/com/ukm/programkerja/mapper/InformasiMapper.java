package com.ukm.programkerja.mapper;

import com.ukm.programkerja.dto.response.InformasiResponse;
import com.ukm.programkerja.entity.Informasi;
import org.springframework.stereotype.Component;

@Component
public class InformasiMapper {

    public InformasiResponse toResponse(Informasi informasi) {
        if (informasi == null) {
            return null;
        }
        return InformasiResponse.builder()
                .id(informasi.getId())
                .judul(informasi.getJudul())
                .isi(informasi.getIsi())
                .kategori(informasi.getKategori())
                .tanggal(informasi.getTanggal())
                .dibuatOleh(informasi.getDibuatOleh() != null ? informasi.getDibuatOleh().getNama() : null)
                .build();
    }
}
