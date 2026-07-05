package com.ukm.programkerja.service.impl;

import com.ukm.programkerja.dto.request.TempatRequest;
import com.ukm.programkerja.dto.response.TempatResponse;
import com.ukm.programkerja.entity.Tempat;
import com.ukm.programkerja.exception.ResourceNotFoundException;
import com.ukm.programkerja.mapper.TempatMapper;
import com.ukm.programkerja.repository.TempatRepository;
import com.ukm.programkerja.service.TempatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TempatServiceImpl implements TempatService {

    private final TempatRepository tempatRepository;
    private final TempatMapper tempatMapper;

    @Override
    public TempatResponse create(TempatRequest request) {
        Tempat tempat = Tempat.builder()
                .namaTempat(request.getNamaTempat())
                .alamat(request.getAlamat())
                .status(request.getStatus() != null ? request.getStatus() : "TERSEDIA")
                .build();

        Tempat saved = tempatRepository.save(tempat);
        return tempatMapper.toResponse(saved);
    }

    @Override
    public TempatResponse update(Long id, TempatRequest request) {
        Tempat tempat = tempatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tempat dengan id " + id + " tidak ditemukan"));

        tempat.setNamaTempat(request.getNamaTempat());
        tempat.setAlamat(request.getAlamat());
        if (request.getStatus() != null) {
            tempat.setStatus(request.getStatus());
        }

        Tempat updated = tempatRepository.save(tempat);
        return tempatMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        Tempat tempat = tempatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tempat dengan id " + id + " tidak ditemukan"));
        tempatRepository.delete(tempat);
    }

    @Override
    @Transactional(readOnly = true)
    public TempatResponse getById(Long id) {
        Tempat tempat = tempatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tempat dengan id " + id + " tidak ditemukan"));
        return tempatMapper.toResponse(tempat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TempatResponse> getAll() {
        return tempatRepository.findAll()
                .stream()
                .map(tempatMapper::toResponse)
                .toList();
    }
}
