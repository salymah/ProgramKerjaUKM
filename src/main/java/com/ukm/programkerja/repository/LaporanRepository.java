package com.ukm.programkerja.repository;

import com.ukm.programkerja.entity.Laporan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LaporanRepository extends JpaRepository<Laporan, Long> {

    List<Laporan> findByProgramKerjaId(Long programKerjaId);
}
