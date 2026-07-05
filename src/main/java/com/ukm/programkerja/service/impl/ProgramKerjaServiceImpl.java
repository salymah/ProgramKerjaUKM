package com.ukm.programkerja.service.impl;

import com.ukm.programkerja.dto.request.ProgramKerjaCreateRequest;
import com.ukm.programkerja.dto.request.ProgramKerjaUpdateRequest;
import com.ukm.programkerja.dto.request.VerifikasiRequest;
import com.ukm.programkerja.dto.response.DashboardStatistikResponse;
import com.ukm.programkerja.dto.response.ProgramKerjaResponse;
import com.ukm.programkerja.entity.ProgramKerja;
import com.ukm.programkerja.entity.StatusProgramKerja;
import com.ukm.programkerja.entity.Tempat;
import com.ukm.programkerja.entity.User;
import com.ukm.programkerja.exception.DuplicateResourceException;
import com.ukm.programkerja.exception.ResourceNotFoundException;
import com.ukm.programkerja.mapper.ProgramKerjaMapper;
import com.ukm.programkerja.repository.ProgramKerjaRepository;
import com.ukm.programkerja.repository.TempatRepository;
import com.ukm.programkerja.repository.UserRepository;
import com.ukm.programkerja.service.ProgramKerjaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProgramKerjaServiceImpl implements ProgramKerjaService {

    private final ProgramKerjaRepository programKerjaRepository;
    private final UserRepository userRepository;
    private final TempatRepository tempatRepository;
    private final ProgramKerjaMapper programKerjaMapper;

    /**
     * Mengecek apakah tempat pada tanggal tertentu sudah dipakai oleh
     * program kerja lain yang masih aktif (MENUNGGU atau DISETUJUI).
     * excludeId dipakai saat update agar program kerja itu sendiri tidak
     * dianggap bentrok dengan dirinya sendiri.
     */
    private void validasiBentrokJadwal(Long tempatId, LocalDate tanggalKegiatan, Long excludeId) {
        if (tempatId == null || tanggalKegiatan == null) {
            return;
        }
        List<ProgramKerja> bentrok = programKerjaRepository
                .findByTempatIdAndTanggalKegiatanAndStatusInAndIdNot(
                        tempatId, tanggalKegiatan,
                        List.of(StatusProgramKerja.MENUNGGU, StatusProgramKerja.DISETUJUI),
                        excludeId != null ? excludeId : -1L);
        if (!bentrok.isEmpty()) {
            String namaBentrok = bentrok.get(0).getNamaProgram();
            throw new DuplicateResourceException(
                    "Tempat sudah digunakan oleh program kerja \"" + namaBentrok +
                    "\" pada tanggal " + tanggalKegiatan +
                    ". Silakan pilih tempat atau tanggal lain.");
        }
    }

    @Override
    public ProgramKerjaResponse create(Long userId, ProgramKerjaCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User dengan id " + userId + " tidak ditemukan"));

        Tempat tempat = null;
        if (request.getTempatId() != null) {
            tempat = tempatRepository.findById(request.getTempatId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Tempat dengan id " + request.getTempatId() + " tidak ditemukan"));
        }

        // Cek bentrok jadwal tempat sebelum menyimpan
        validasiBentrokJadwal(request.getTempatId(), request.getTanggalKegiatan(), null);

        ProgramKerja programKerja = ProgramKerja.builder()
                .user(user)
                .tempat(tempat)
                .namaProgram(request.getNamaProgram())
                .deskripsi(request.getDeskripsi())
                .tanggalPengajuan(request.getTanggalPengajuan() != null
                        ? request.getTanggalPengajuan() : LocalDate.now())
                .tanggalKegiatan(request.getTanggalKegiatan())
                .status(StatusProgramKerja.MENUNGGU)
                .build();

        ProgramKerja saved = programKerjaRepository.save(programKerja);
        return programKerjaMapper.toResponse(saved);
    }

    @Override
    public ProgramKerjaResponse update(Long id, Long requesterId, ProgramKerjaUpdateRequest request) {
        ProgramKerja programKerja = programKerjaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Program kerja dengan id " + id + " tidak ditemukan"));

        if (!programKerja.getUser().getId().equals(requesterId)) {
            throw new AccessDeniedException("Anda tidak memiliki akses untuk mengubah program kerja ini");
        }

        // Tentukan tempatId yang akan dipakai (bisa berubah dari request, atau tetap yang lama)
        Long tempatIdBaru = request.getTempatId() != null
                ? request.getTempatId()
                : (programKerja.getTempat() != null ? programKerja.getTempat().getId() : null);

        // Cek bentrok jadwal (excludeId = id program kerja ini sendiri agar tidak self-conflict)
        validasiBentrokJadwal(tempatIdBaru, request.getTanggalKegiatan(), id);

        programKerja.setNamaProgram(request.getNamaProgram());
        programKerja.setDeskripsi(request.getDeskripsi());

        if (request.getTanggalKegiatan() != null) {
            programKerja.setTanggalKegiatan(request.getTanggalKegiatan());
        }

        if (request.getTempatId() != null) {
            Tempat tempat = tempatRepository.findById(request.getTempatId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Tempat dengan id " + request.getTempatId() + " tidak ditemukan"));
            programKerja.setTempat(tempat);
        }

        // Jika sebelumnya diminta revisi, ajuan ulang otomatis mengembalikan status ke MENUNGGU
        if (programKerja.getStatus() == StatusProgramKerja.REVISI) {
            programKerja.setStatus(StatusProgramKerja.MENUNGGU);
        }

        ProgramKerja updated = programKerjaRepository.save(programKerja);
        return programKerjaMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        ProgramKerja programKerja = programKerjaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Program kerja dengan id " + id + " tidak ditemukan"));
        programKerjaRepository.delete(programKerja);
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramKerjaResponse getById(Long id) {
        ProgramKerja programKerja = programKerjaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Program kerja dengan id " + id + " tidak ditemukan"));
        return programKerjaMapper.toResponse(programKerja);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramKerjaResponse> getAll() {
        return programKerjaRepository.findAll().stream().map(programKerjaMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramKerjaResponse> getByUserId(Long userId) {
        return programKerjaRepository.findByUserId(userId).stream().map(programKerjaMapper::toResponse).toList();
    }

    @Override
    public ProgramKerjaResponse verifikasi(Long id, VerifikasiRequest request) {
        ProgramKerja programKerja = programKerjaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Program kerja dengan id " + id + " tidak ditemukan"));

        StatusProgramKerja statusBaru = request.getStatus();

        if (statusBaru != StatusProgramKerja.DISETUJUI && statusBaru != StatusProgramKerja.REVISI) {
            throw new IllegalArgumentException("Status verifikasi hanya boleh DISETUJUI atau REVISI");
        }

        programKerja.setStatus(statusBaru);
        programKerja.setTanggalVerifikasi(LocalDate.now());
        programKerja.setKeterangan(request.getKeterangan());

        ProgramKerja updated = programKerjaRepository.save(programKerja);
        return programKerjaMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardStatistikResponse getStatistik() {
        return DashboardStatistikResponse.builder()
                .totalProgramKerja(programKerjaRepository.count())
                .diajukan(programKerjaRepository.countByStatus(StatusProgramKerja.MENUNGGU))
                .disetujui(programKerjaRepository.countByStatus(StatusProgramKerja.DISETUJUI))
                .revisi(programKerjaRepository.countByStatus(StatusProgramKerja.REVISI))
                .selesai(programKerjaRepository.countByStatus(StatusProgramKerja.SELESAI))
                .build();
    }
}
