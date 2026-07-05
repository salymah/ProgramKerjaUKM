package com.ukm.programkerja.repository;

import com.ukm.programkerja.entity.Catatan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatatanRepository extends JpaRepository<Catatan, Long> {

    List<Catatan> findByProgramKerjaId(Long programKerjaId);
}
