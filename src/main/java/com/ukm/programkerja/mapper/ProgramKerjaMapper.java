package com.ukm.programkerja.mapper;

import com.ukm.programkerja.dto.response.ProgramKerjaResponse;
import com.ukm.programkerja.entity.ProgramKerja;
import com.ukm.programkerja.entity.Tempat;
import com.ukm.programkerja.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ProgramKerjaMapper {

    public ProgramKerjaResponse toResponse(ProgramKerja programKerja) {
        if (programKerja == null) {
            return null;
        }

        User user = programKerja.getUser();
        Tempat tempat = programKerja.getTempat();

        return ProgramKerjaResponse.builder()
                .id(programKerja.getId())
                .namaProgram(programKerja.getNamaProgram())
                .deskripsi(programKerja.getDeskripsi())
                .tanggalPengajuan(programKerja.getTanggalPengajuan())
                .tanggalKegiatan(programKerja.getTanggalKegiatan())
                .tanggalVerifikasi(programKerja.getTanggalVerifikasi())
                .status(programKerja.getStatus())
                .keterangan(programKerja.getKeterangan())
                .userId(user != null ? user.getId() : null)
                .namaPengaju(user != null ? user.getNama() : null)
                .tempatId(tempat != null ? tempat.getId() : null)
                .namaTempat(tempat != null ? tempat.getNamaTempat() : null)
                .build();
    }
}
