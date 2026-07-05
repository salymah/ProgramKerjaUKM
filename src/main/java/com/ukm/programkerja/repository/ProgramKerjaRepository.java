package com.ukm.programkerja.repository;

import com.ukm.programkerja.entity.ProgramKerja;
import com.ukm.programkerja.entity.StatusProgramKerja;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramKerjaRepository extends JpaRepository<ProgramKerja, Long> {

    /**
     * Daftar program kerja milik satu user (dipakai UKM untuk melihat
     * pengajuan miliknya sendiri, UC-12 Lihat Status Pengajuan).
     */
    List<ProgramKerja> findByUserId(Long userId);

    List<ProgramKerja> findByStatus(StatusProgramKerja status);

    long countByStatus(StatusProgramKerja status);

    /**
     * Mengecek apakah sudah ada program kerja lain (status MENUNGGU atau
     * DISETUJUI, yaitu masih aktif/berpotensi terlaksana) yang memakai
     * tempat & tanggal kegiatan yang sama. Dipakai untuk fitur cek bentrok
     * jadwal tempat saat UKM mengajukan/mengubah program kerja.
     */
    List<ProgramKerja> findByTempatIdAndTanggalKegiatanAndStatusInAndIdNot(
            Long tempatId, java.time.LocalDate tanggalKegiatan,
            List<StatusProgramKerja> statusList, Long excludeId);
}
