package com.ukm.programkerja.mapper;

import com.ukm.programkerja.dto.response.LaporanResponse;
import com.ukm.programkerja.entity.Laporan;
import org.springframework.stereotype.Component;

@Component
public class LaporanMapper {

    public LaporanResponse toResponse(Laporan laporan) {
        if (laporan == null) {
            return null;
        }
        return LaporanResponse.builder()
                .id(laporan.getId())
                .programKerjaId(laporan.getProgramKerja() != null ? laporan.getProgramKerja().getId() : null)
                .namaProgram(laporan.getProgramKerja() != null ? laporan.getProgramKerja().getNamaProgram() : null)
                .namaFile(laporan.getNamaFile())
                .tanggalUpload(laporan.getTanggalUpload())
                .status(laporan.getStatus())
                .build();
    }
}
