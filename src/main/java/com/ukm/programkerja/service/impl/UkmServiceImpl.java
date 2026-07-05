package com.ukm.programkerja.service.impl;

import com.ukm.programkerja.dto.request.UkmRequest;
import com.ukm.programkerja.dto.response.UkmResponse;
import com.ukm.programkerja.entity.Ukm;
import com.ukm.programkerja.exception.ResourceNotFoundException;
import com.ukm.programkerja.mapper.UkmMapper;
import com.ukm.programkerja.repository.UkmRepository;
import com.ukm.programkerja.service.UkmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Implementasi UC-08 "Kelola Data UKM". Sebelumnya use case ini disederhanakan
 * menjadi CRUD User biasa (lihat catatan audit di User.java); sekarang data
 * UKM punya tabel & entity sendiri sehingga WK III dapat mengelola profil
 * organisasi (nama, bidang, deskripsi) secara terpisah dari akun login.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UkmServiceImpl implements UkmService {

    private final UkmRepository ukmRepository;
    private final UkmMapper ukmMapper;

    @Override
    public UkmResponse create(UkmRequest request) {
        Ukm ukm = Ukm.builder()
                .namaUkm(request.getNamaUkm())
                .bidang(request.getBidang())
                .deskripsi(request.getDeskripsi())
                .status(StringUtils.hasText(request.getStatus()) ? request.getStatus() : "AKTIF")
                .build();
        return ukmMapper.toResponse(ukmRepository.save(ukm));
    }

    @Override
    public UkmResponse update(Long id, UkmRequest request) {
        Ukm ukm = ukmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UKM dengan id " + id + " tidak ditemukan"));
        ukm.setNamaUkm(request.getNamaUkm());
        ukm.setBidang(request.getBidang());
        ukm.setDeskripsi(request.getDeskripsi());
        if (StringUtils.hasText(request.getStatus())) {
            ukm.setStatus(request.getStatus());
        }
        return ukmMapper.toResponse(ukmRepository.save(ukm));
    }

    @Override
    public void delete(Long id) {
        Ukm ukm = ukmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UKM dengan id " + id + " tidak ditemukan"));
        ukmRepository.delete(ukm);
    }

    @Override
    @Transactional(readOnly = true)
    public UkmResponse getById(Long id) {
        Ukm ukm = ukmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UKM dengan id " + id + " tidak ditemukan"));
        return ukmMapper.toResponse(ukm);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UkmResponse> getAll() {
        return ukmRepository.findAll().stream().map(ukmMapper::toResponse).toList();
    }
}
