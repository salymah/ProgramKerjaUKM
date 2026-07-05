package com.ukm.programkerja.service;

import com.ukm.programkerja.dto.response.LaporanResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LaporanService {

    /**
     * Mengunggah laporan kegiatan. Mengubah status ProgramKerja terkait
     * menjadi SELESAI secara otomatis, karena laporan hanya diunggah
     * setelah kegiatan benar-benar terlaksana.
     */
    LaporanResponse upload(Long programKerjaId, MultipartFile file);

    List<LaporanResponse> getByProgramKerjaId(Long programKerjaId);

    List<LaporanResponse> getAll();

    Resource download(Long laporanId);

    String getFilenameById(Long laporanId);

    void delete(Long laporanId);
}
