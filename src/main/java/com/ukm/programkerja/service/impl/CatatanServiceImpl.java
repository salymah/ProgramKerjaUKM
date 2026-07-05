package com.ukm.programkerja.service.impl;

import com.ukm.programkerja.dto.request.CatatanRequest;
import com.ukm.programkerja.dto.response.CatatanResponse;
import com.ukm.programkerja.entity.Catatan;
import com.ukm.programkerja.entity.ProgramKerja;
import com.ukm.programkerja.entity.User;
import com.ukm.programkerja.exception.ResourceNotFoundException;
import com.ukm.programkerja.mapper.CatatanMapper;
import com.ukm.programkerja.repository.CatatanRepository;
import com.ukm.programkerja.repository.ProgramKerjaRepository;
import com.ukm.programkerja.repository.UserRepository;
import com.ukm.programkerja.service.CatatanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CatatanServiceImpl implements CatatanService {

    private final CatatanRepository catatanRepository;
    private final ProgramKerjaRepository programKerjaRepository;
    private final UserRepository userRepository;
    private final CatatanMapper catatanMapper;

    @Override
    public CatatanResponse create(Long programKerjaId, Long penulisId, CatatanRequest request) {
        ProgramKerja programKerja = programKerjaRepository.findById(programKerjaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Program kerja dengan id " + programKerjaId + " tidak ditemukan"));

        User penulis = userRepository.findById(penulisId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User dengan id " + penulisId + " tidak ditemukan"));

        Catatan catatan = Catatan.builder()
                .programKerja(programKerja)
                .penulis(penulis)
                .isiCatatan(request.getIsiCatatan())
                .tanggal(LocalDate.now())
                .build();

        Catatan saved = catatanRepository.save(catatan);
        return catatanMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatatanResponse> getByProgramKerjaId(Long programKerjaId) {
        return catatanRepository.findByProgramKerjaId(programKerjaId)
                .stream()
                .map(catatanMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        Catatan catatan = catatanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catatan dengan id " + id + " tidak ditemukan"));
        catatanRepository.delete(catatan);
    }
}
