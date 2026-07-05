package com.ukm.programkerja.mapper;

import com.ukm.programkerja.dto.response.UkmResponse;
import com.ukm.programkerja.entity.Ukm;
import org.springframework.stereotype.Component;

@Component
public class UkmMapper {

    public UkmResponse toResponse(Ukm ukm) {
        if (ukm == null) {
            return null;
        }
        return UkmResponse.builder()
                .id(ukm.getId())
                .namaUkm(ukm.getNamaUkm())
                .bidang(ukm.getBidang())
                .deskripsi(ukm.getDeskripsi())
                .status(ukm.getStatus())
                .createdAt(ukm.getCreatedAt())
                .jumlahAnggota(ukm.getAnggotaList() != null ? ukm.getAnggotaList().size() : 0)
                .build();
    }
}
