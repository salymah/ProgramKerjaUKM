package com.ukm.programkerja.repository;

import com.ukm.programkerja.entity.Informasi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InformasiRepository extends JpaRepository<Informasi, Long> {
    List<Informasi> findAllByOrderByTanggalDesc();
}
