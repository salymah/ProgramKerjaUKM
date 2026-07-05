package com.ukm.programkerja.mapper;

import com.ukm.programkerja.dto.response.CatatanResponse;
import com.ukm.programkerja.entity.Catatan;
import com.ukm.programkerja.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CatatanMapper {

    public CatatanResponse toResponse(Catatan catatan) {
        if (catatan == null) {
            return null;
        }

        User penulis = catatan.getPenulis();

        return CatatanResponse.builder()
                .id(catatan.getId())
                .programKerjaId(catatan.getProgramKerja() != null ? catatan.getProgramKerja().getId() : null)
                .namaProgram(catatan.getProgramKerja() != null ? catatan.getProgramKerja().getNamaProgram() : null)
                .penulisId(penulis != null ? penulis.getId() : null)
                .namaPenulis(penulis != null ? penulis.getNama() : null)
                .penulisRole(penulis != null ? penulis.getRole().name() : null)
                .isiCatatan(catatan.getIsiCatatan())
                .tanggal(catatan.getTanggal())
                .build();
    }
}
