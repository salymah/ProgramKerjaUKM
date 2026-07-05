package com.ukm.programkerja.service.impl;

import com.ukm.programkerja.dto.request.InformasiRequest;
import com.ukm.programkerja.dto.response.InformasiResponse;
import com.ukm.programkerja.entity.Informasi;
import com.ukm.programkerja.entity.User;
import com.ukm.programkerja.exception.ResourceNotFoundException;
import com.ukm.programkerja.mapper.InformasiMapper;
import com.ukm.programkerja.repository.InformasiRepository;
import com.ukm.programkerja.repository.UserRepository;
import com.ukm.programkerja.service.InformasiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementasi fitur Informasi/Agenda Kampus (mis. jadwal UAS, akreditasi)
 * yang ditampilkan di Dashboard seluruh role. Dikelola oleh WK III.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class InformasiServiceImpl implements InformasiService {

    private final InformasiRepository informasiRepository;
    private final UserRepository userRepository;
    private final InformasiMapper informasiMapper;

    @Override
    public InformasiResponse create(Long userId, InformasiRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User dengan id " + userId + " tidak ditemukan"));

        Informasi informasi = Informasi.builder()
                .judul(request.getJudul())
                .isi(request.getIsi())
                .kategori(request.getKategori())
                .tanggal(request.getTanggal())
                .dibuatOleh(user)
                .build();
        return informasiMapper.toResponse(informasiRepository.save(informasi));
    }

    @Override
    public InformasiResponse update(Long id, InformasiRequest request) {
        Informasi informasi = informasiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Informasi dengan id " + id + " tidak ditemukan"));
        informasi.setJudul(request.getJudul());
        informasi.setIsi(request.getIsi());
        informasi.setKategori(request.getKategori());
        informasi.setTanggal(request.getTanggal());
        return informasiMapper.toResponse(informasiRepository.save(informasi));
    }

    @Override
    public void delete(Long id) {
        Informasi informasi = informasiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Informasi dengan id " + id + " tidak ditemukan"));
        informasiRepository.delete(informasi);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InformasiResponse> getAll() {
        return informasiRepository.findAllByOrderByTanggalDesc()
                .stream().map(informasiMapper::toResponse).toList();
    }
}
